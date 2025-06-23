/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view.waypoint;

import model.coord.CoordEuc;

/**
 * Représentation visuelle d'une coordonnée euclidienne
 * 
 * @author ugola
 */
public class WaypointEuc extends Waypoint<CoordEuc> {
    public WaypointEuc(CoordEuc coord) {
        super(coord);
    }
    
    /**
     * Met à jour la position des waypoints en fonction du déplacement effectué et du zoom
     * 
     * @param offsetX Le décalage en X (dû au déplacement)
     * @param offsetY Le décalage en Y (dû au déplacement)
     * @param newScale Le nouveau niveau de zoom
     */
    public void update(int offsetX, int offsetY, double newScale) {
        int x = (int) (newScale * coord.getX()) + offsetX;
        int y = (int) (newScale * coord.getY()) + offsetY;

        this.setLocation(x, y);
    }
}