/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.beans.Beans;
import javax.swing.event.MouseInputListener;
import model.circuit.CircuitGeo;
import model.coord.CoordGeo;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.WaypointPainter;
import view.waypoint.WaypointGeo;

/**
 *
 * @author ugola
 */
public class MapGeo extends Map<CoordGeo, WaypointGeo, CircuitGeo> {
    private JXMapViewer viewer;

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
            MouseInputListener listener = new PanMouseInputListener(viewer);
            viewer.addMouseListener(listener);
            viewer.addMouseMotionListener(listener);
            viewer.addMouseWheelListener(new ZoomMouseWheelListenerCenter(viewer) {
                @Override
                public void mouseWheelMoved(MouseWheelEvent e) {
                    super.mouseWheelMoved(e);
                    // Formule pour mettre un nombre d'un intervalle à un autre
                    // newValue = (((oldValue - oldMin) * (newMax - newMin)) / (oldMax - oldMin)) + newMin 
                    double jxMapScale = viewer.getZoom() * 1.5 / 19 + 0.5; // Zoom de JxMapViewer convertit dans un intervalle voulu
                    scale = 2.5 - jxMapScale; // Inversion de l'echelle pour correspondre à celle voulue (maxValue + minValue) - number
                }
            });

            viewer.setOverlayPainter(new WaypointPainter<WaypointGeo>() {
                @Override
                protected void doPaint(Graphics2D g, JXMapViewer map, int width, int height) {
                    for (WaypointGeo waypoint : waypoints.values()) {
                        Point2D point = viewer.getTileFactory().geoToPixel(waypoint.getPosition(), viewer.getZoom());
                        Rectangle rect = viewer.getViewportBounds();

                        int x = (int) (point.getX() - rect.getX());
                        int y = (int) (point.getY() - rect.getY());

                        waypoint.setLocation(x - waypoint.getWidth() / 2, y - waypoint.getHeight() / 2);
                    }
                }
            });
            
            System.out.println("=> Carte geographique creee");
            
            setVisible(false);
        }
    }

    @Override
    public void addCoord(double x, double y) {
        
    }
    
    @Override
    public void addWaypoint(CoordGeo coord) {

    }
}
