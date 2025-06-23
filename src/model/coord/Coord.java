/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.coord;

/**
 * Classe abstraite qui représente une coordonnée munie d'un ID
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

    /**
     * Renvoie l'ID maximum utilisé (Permet de créer des points qui possèdent un ID unique)
     * @return L'ID en question
     */
    public static int getMaxId() { return maxId; }
}