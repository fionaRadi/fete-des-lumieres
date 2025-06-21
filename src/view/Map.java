/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JComponent;
import model.circuit.Circuit;
import model.coord.Coord;
import view.waypoint.Waypoint;

/**
 *
 * @author ugola
 * @param <T>
 * @param <W>
 * @param <C>
 */
public abstract class Map<T extends Coord, W extends Waypoint, C extends Circuit> extends JComponent {
    protected Set<W> waypoints;    
    protected C circuit;
        
    protected double scale;
    
    private List<WaypointSelectionListener> waypointListeners = new ArrayList<>();
    protected ActionListener waypointListener;
    
    private List<MapClickedListener> mapClickedListeners = new ArrayList<>();

    protected Map() {
        this.waypoints = new HashSet<>();
        
        waypointListener = (ActionEvent e) -> {
            W selectedWaypoint = (W) e.getSource();
            fireWaypointSelected(selectedWaypoint);
        };
        
        scale = 1.0;
        setLayout(null);
        
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}

            @Override
            public void mousePressed(MouseEvent e) {
                fireMapClicked(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        });
    }

    protected abstract void addCoord(double x, double y);
    protected abstract void addWaypoint(T coord);
    
    public void open(C circuit) {
        close();
        
        System.out.println("=> Chargement de la carte");
        
        this.circuit = circuit;
        
        List<T> coords = circuit.getCoords();
        if (coords != null) {
            for (T coord : coords) {
                addWaypoint(coord);
            }
        }
        
        setVisible(true);
    }

    public void close() {
        for (W waypoint : waypoints) {
            remove(waypoint);
        }
        
        waypoints.clear();
        setVisible(false);
    }

    public boolean isOpen() {
        return isVisible();
    }
    
    public Set<W> getWaypoints() {
        return waypoints;
    }
    
    public double getScale() {
        return scale;
    }
    
    private void fireWaypointSelected(W waypoint) {
        for (WaypointSelectionListener listener : waypointListeners) {
            listener.onWaypointSelected(waypoint);
        }
    }
    
    public void addWaypointSelectionListener(WaypointSelectionListener listener) {
        waypointListeners.add(listener);
    }
    
    private void fireMapClicked(MouseEvent e) {
        for (MapClickedListener listener : mapClickedListeners) {
            listener.onMapClicked(e);
        }
    }
    
    public void addMapClickedListener(MapClickedListener listener) {
        mapClickedListeners.add(listener);
    }
}
