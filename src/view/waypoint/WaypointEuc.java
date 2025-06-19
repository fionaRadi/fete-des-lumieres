/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view.waypoint;

import model.coord.CoordEuc;

/**
 *
 * @author ugola
 */
public class WaypointEuc extends Waypoint {
    private final CoordEuc coord;

    public WaypointEuc(CoordEuc place) {
        super();
        this.coord = place;
    }

    public void update(int offsetX, int offsetY, double newScale) {
        int x = (int) (newScale * coord.getX()) + offsetX;
        int y = (int) (newScale * coord.getY()) + offsetY;

        this.setLocation(x, y);
    }
    
    public CoordEuc getCoord() {
        return coord;
    }
}