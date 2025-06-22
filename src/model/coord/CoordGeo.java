/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.coord;

import org.jxmapviewer.viewer.GeoPosition;

/**
 *
 * @author ugola
 */
public class CoordGeo extends Coord {    
    private GeoPosition position;

    public CoordGeo(int id, double latitude, double longitude) {
    super(id);

    this.position = new GeoPosition(convertToDecimal(latitude), convertToDecimal(longitude)
    );
}

    private static double convertToDecimal(double coord) {
        int deg = (int) coord;
        double min = Math.abs((coord - deg) * 100);

        if (coord < 0) {
            return deg - (min / 60.0);
        } else {
            return deg + (min / 60.0);
        }
    }

    public CoordGeo(double latitude, double longitude) {
        this(maxId + 1, latitude, longitude);
    }

    public double getLatitude() { return position.getLatitude(); }
    public double getLongitude() { return position.getLongitude(); }
    
    public GeoPosition getPosition() { return position; }
}
