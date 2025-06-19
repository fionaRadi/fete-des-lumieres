/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.coord;

/**
 *
 * @author ugola
 */
public class CoordEuc extends Coord {
    private final double x;
    private final double y;

    public CoordEuc(int id, double x, double y) {
        super(id);
        this.x = x;
        this.y = y;
    }
    
    public CoordEuc(double x, double y) {
        super(maxId + 1);
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}