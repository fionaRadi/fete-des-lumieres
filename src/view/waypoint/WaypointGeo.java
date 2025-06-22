/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view.waypoint;

import model.coord.CoordGeo;
import org.jxmapviewer.viewer.GeoPosition;

/**
 * Représentation visuelle d'une coordonnée géographique
 * 
 * @author ugola
 */
public class WaypointGeo extends Waypoint<CoordGeo> implements org.jxmapviewer.viewer.Waypoint {
    public WaypointGeo(CoordGeo coord) {
        super(coord);
    }

    /**
     * Renvoie la position de la coordonnée associée au waypoint
     * 
     * @return La position de la coordonnée
     */
    @Override
    public GeoPosition getPosition() {
        return coord.getPosition();
    }
}
