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

        this.position = new GeoPosition(convertToDecimal(latitude), convertToDecimal(longitude));
    }
    
    public CoordGeo(GeoPosition position) {
        super(maxId + 1);
        
        this.position = position;
    }

    private static double convertToDecimal(double degreeMinute) {
        int deg = (int) degreeMinute;
        double min = Math.abs((degreeMinute - deg) * 100);

        if (degreeMinute < 0) {
            return deg - (min / 60.0);
        } else {
            return deg + (min / 60.0);
        }
    }
    
    private static double convertToDegreeMinute(double decimalDegree) {
        int deg = (int) decimalDegree;
        double fractional = Math.abs(decimalDegree - deg);
        int min = (int) Math.round(fractional * 60);

        return deg >= 0 ? deg + (min / 100.0) : deg - (min / 100.0);
    }

    public CoordGeo(double latitude, double longitude) {
        this(maxId + 1, latitude, longitude);
    }

    public double getDecimalLatitude() { return position.getLatitude(); }
    public double getDecimalLongitude() { return position.getLongitude(); }
    
    public double getDegreeMinuteLatitude() { return convertToDegreeMinute(position.getLatitude()); }
    public double getDegreeMinuteLongitude() { return convertToDegreeMinute(position.getLongitude()); }
    
    public GeoPosition getPosition() { return position; }
}
