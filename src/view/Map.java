/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import java.util.HashMap;
import java.util.List;
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
    protected HashMap<Integer, W> waypoints;
    
    protected C circuit;
    
    protected W selectedWaypoint;
    
    protected double scale;

    protected Map() {
        this.waypoints = new HashMap<>();
        scale = 1.0;
        setLayout(null);
    }

    public abstract void addCoord(double x, double y);
    
    public abstract void addWaypoint(T coord);
        
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
        waypoints.clear();
        setVisible(false);
    }

    public boolean isOpen() {
        return isVisible();
    }
    
    public HashMap<Integer, W> getWaypoints() {
        return waypoints;
    }
    
    public double getScale() {
        return scale;
    }
}
