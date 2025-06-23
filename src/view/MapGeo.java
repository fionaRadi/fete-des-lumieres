/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import view.geopainters.WaypointGeoPainter;
import view.geopainters.CircuitGeoPainter;
import view.listeners.RightPanMouseInputListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.beans.Beans;
import java.util.ArrayList;
import java.util.List;
import model.circuit.CircuitGeo;
import model.coord.CoordGeo;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import view.waypoint.WaypointGeo;
import org.jxmapviewer.painter.Painter;

/**
 * Une map de type géographique.
 * Permet l'affichage de waypoints, le déplacement et le zoom.
 * @author ugola
 */
public class MapGeo extends Map<CoordGeo, WaypointGeo, CircuitGeo> {
    private JXMapViewer viewer;
    
    private CircuitGeoPainter circuitPainter;
    private WaypointGeoPainter waypointPainter;
    
    public MapGeo() {
        super();
        
        System.out.println("=> Creation de la carte geographique");
        
        if (!Beans.isDesignTime()) {
            TileFactoryInfo info = new OSMTileFactoryInfo();
            DefaultTileFactory tileFactory = new DefaultTileFactory(info);

            viewer = new JXMapViewer();
            viewer.setTileFactory(tileFactory);
            setLayout(new BorderLayout());
            add(viewer, BorderLayout.CENTER);

            GeoPosition position = new GeoPosition(45.772077, 4.882189);
            viewer.setAddressLocation(position);

            viewer.setZoom(10);
            
            RightPanMouseInputListener panListener = new RightPanMouseInputListener(viewer) {
                @Override
                public void mousePressed(MouseEvent evt) {
                    fireMapClicked(evt);
                    super.mousePressed(evt);
                }
            };

            viewer.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            viewer.addMouseListener(panListener);
            viewer.addMouseMotionListener(panListener);            
            
            addMouseWheelListener((MouseWheelEvent e) -> {
                viewer.setZoom(viewer.getZoom() + e.getWheelRotation());
                // Formule pour mettre un nombre d'un intervalle à un autre
                // newValue = (((oldValue - oldMin) * (newMax - newMin)) / (oldMax - oldMin)) + newMin
                double jxMapScale = viewer.getZoom() * 1.9 / 19 + 0.1; // Zoom de JxMapViewer convertit dans un intervalle voulu
                scale = 2.1 - jxMapScale; // Inversion de l'echelle pour correspondre à celle voulue (maxValue + minValue) - number
            });
            
            waypointPainter = new WaypointGeoPainter();
            circuitPainter = new CircuitGeoPainter(Color.BLUE, Color.YELLOW, Color.GREEN, Color.RED);
            
            List<Painter<JXMapViewer>> painters = new ArrayList<>();
            
            painters.add(waypointPainter);
            painters.add(circuitPainter);
            
            viewer.setOverlayPainter(new CompoundPainter(painters));
            
            System.out.println("=> Carte geographique creee");
            
            setVisible(false);
        }
    }

    @Override
    public void addWaypoint(CoordGeo coord) {
        WaypointGeo waypoint = new WaypointGeo(coord);
        waypoint.addActionListener(waypointListener);
        waypoints.add(waypoint);
        viewer.add(waypoint);
        waypointPainter.setWaypoints(waypoints);
    }
    
    public void addCoord(MouseEvent e) {
        Point point = e.getPoint();
        GeoPosition geoPos = viewer.convertPointToGeoPosition(point);
        CoordGeo coord = new CoordGeo(geoPos);
        circuit.addCoord(coord);
        addWaypoint(coord);
        viewer.repaint();
    }
    
    public void addCoord(double latitude, double longitude) {
        CoordGeo coord = new CoordGeo(latitude, longitude);
        circuit.addCoord(coord);
        addWaypoint(coord);
        viewer.repaint();
    }
    
    public void removeCoord(WaypointGeo waypoint) {
        circuit.removeCoord(waypoint.getCoord());
        waypoints.remove(waypoint);
        viewer.remove(waypoint);
        repaint();
    } 

    @Override
    public void load(CircuitGeo circuit) {
        super.load(circuit);
        waypointPainter.setWaypoints(waypoints);
        circuitPainter.setCircuit(circuit);
    }
    
    @Override
    public void close() {
        for (WaypointGeo waypoint : waypoints) {
            viewer.remove(waypoint);
        }
        
        super.close();
    }
}
