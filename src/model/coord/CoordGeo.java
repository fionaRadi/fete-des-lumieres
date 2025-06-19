/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.coord;

/**
 *
 * @author ugola
 */
public class CoordGeo extends Coord {
    private final double latitude;
    private final double longitude;

    public CoordGeo(int id, double latitude, double longitude) {
        super(id);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
}
