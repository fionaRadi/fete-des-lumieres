/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.coord;

/**
 * Coordonnée euclidienne qui possède un X et un Y
 * 
 * @author ugola
 */
public class CoordEuc extends Coord {
    private final double x;
    private final double y;

    /**
     * Créer une coordonnée à partir d'un ID passée en paramètres et de son X et Y
     * 
     * @param id L'ID de la coordonnée
     * @param x Son composant X
     * @param y Son composant Y
     */
    public CoordEuc(int id, double x, double y) {
        super(id);
        this.x = x;
        this.y = y;
    }
    
    /**
     * Créer une coordonnée à partir de son X et Y. L'ID est généré automatiquement (il est unique)
     * 
     * @param x Son composant X
     * @param y Son composant Y
     */
    public CoordEuc(double x, double y) {
        super(maxId + 1);
        this.x = x;
        this.y = y;
    }

    public double getX() { return x; }
    public double getY() { return y; }
}