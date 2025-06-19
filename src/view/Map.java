/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import model.coord.Coord;
import view.waypoint.Waypoint;

/**
 *
 * @author ugola
 * @param <T>
 * @param <W>
 */
public abstract class Map<T extends Coord, W extends Waypoint> extends JComponent {
    protected List<W> waypoints;
    
    protected List<List<Integer>> circuits;
    
    protected W selectedWaypoint;
    
    protected double scale;

    protected Map() {
        this.waypoints = new ArrayList<>();
        this.circuits = new ArrayList<>();
        scale = 1.0;
        setLayout(null);
    }

    public abstract W addWaypoint(T coord);
        
    public void open(List<T> coords) {
        close();
        
        System.out.println("=> Chargement de la carte");
        
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
    
    public List<W> getWaypoints() {
        return waypoints;
    }
    
    public double getScale() {
        return scale;
    }
}
