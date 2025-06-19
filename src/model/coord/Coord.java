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
    
    protected static int maxId;

    protected Coord(int id) {
        this.id = id;
        maxId = Math.max(maxId, id);
    }

    public int getId() { return id; }
    
    public static int getMaxId() { return maxId; }
}