/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import view.listeners.MapClickedListener;
import view.listeners.WaypointSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JComponent;
import model.circuit.Circuit;
import model.coord.Coord;
import view.waypoint.Waypoint;

/**
 * La représentation d'une carte permettant entre autres d'afficher des coordonnées via des Waypoints (représentation graphique) 
 * 
 * @author ugola
 * @param <T> Le type de coordonnées
 * @param <W> Le type de waypoints
 * @param <C> Le type de circuits
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
    }

    protected abstract void addWaypoint(T coord);
    
    /**
     * Charge un circuit dans la map afin de l'afficher (Waypoints)
     * 
     * @param circuit Le circuit à charger
     */
    public void load(C circuit) {
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

    /**
     * Renvoie si la carte est affichée
     * 
     * @return true si la carte est à l'écran, false sinon
     */
    public boolean displayed() {
        return isVisible();
    }
    
    public Set<W> getWaypoints() {
        return waypoints;
    }
    
    public double getScale() {
        return scale;
    }
    
    /**
     * Est déclenché lors qu'un waypoint est sélectionné, déclenche par la suite les listeners associés (WaypointSelectionListener)
     * Permet une bonne encapsulation
     * 
     * @param waypoint Le waypoint sélectionné
     */
    private void fireWaypointSelected(W waypoint) {
        for (WaypointSelectionListener listener : waypointListeners) {
            listener.onWaypointSelected(waypoint);
        }
    }
    
    /**
     * Ajout un WaypointSelectionListener à la map
     * 
     * @param listener Le listener à ajouter
     */
    public void addWaypointSelectionListener(WaypointSelectionListener listener) {
        waypointListeners.add(listener);
    }
    
    /**
     * Est déclenché lors que la map est cliqué, déclenche par la suite les listeners associés (MapClickedListener)
     * Permet une bonne encapsulation
     * 
     * @param e Le MouseEvent généré lorsque la map est cliquée
     */
    protected void fireMapClicked(MouseEvent e) {
        for (MapClickedListener listener : mapClickedListeners) {
            listener.onMapClicked(e);
        }
    }
    
    /**
     * Ajout un MapClickedListener à la map
     * 
     * @param listener 
     */
    protected void addMapClickedListener(MapClickedListener listener) {
        mapClickedListeners.add(listener);
    }
}
