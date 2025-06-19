/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.coord;

/**
 *
 * @author ugola
 */
public abstract class Coord {
    protected final int id;

    protected Coord(int id) {
        this.id = id;
    }

    public int getId() { return id; }
}