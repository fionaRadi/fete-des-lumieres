/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view.waypoint;

import model.coord.CoordGeo;
import org.jxmapviewer.viewer.GeoPosition;

/**
 *
 * @author ugola
 */
public class WaypointGeo extends Waypoint implements org.jxmapviewer.viewer.Waypoint {
    private final CoordGeo coord;

    public WaypointGeo(CoordGeo place) {
        super();
        this.coord = place;
    }

    @Override
    public GeoPosition getPosition() {
        return new GeoPosition(coord.getLatitude(), coord.getLongitude());
    }
    
    
    public CoordGeo getCoord() {
        return coord;
    }
}
