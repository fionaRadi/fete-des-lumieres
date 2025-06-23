/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view.waypoint;

import java.awt.Dimension;
import java.util.Objects;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import model.coord.Coord;

/**
 * Représentation visuelle d'une coordonnée
 *
 * @author ugola
 * @param <T>
 */
public abstract class Waypoint<T extends Coord> extends JButton {
    protected static ImageIcon redIcon;
    
    protected T coord;

    private static final String RED_ICON_PATH = "../../resources/images/location-dot-solid-red.png";

    public Waypoint(T coord) {
        if (redIcon == null) {
            loadImage();
        }

        this.coord = coord;
        
        setContentAreaFilled(false);
        setIcon(redIcon);
        setSize(new Dimension(redIcon.getIconWidth(), redIcon.getIconHeight()));

        setBorder(null);
    }

    /**
     * Charge l'icone du waypoint
     */
    public static void loadImage() {
        System.out.println("=> Chargement des images des waypoints");
        try {
            redIcon = new ImageIcon(Objects.requireNonNull(Waypoint.class.getResource(RED_ICON_PATH)));
            
            System.out.println("=> Images des waypoints chargees");
            
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des fichiers");
        }
    }
    
    /**
     * Renvoie l'icone du waypoint
     * 
     * @return L'icone du waypoint
     */
    public static ImageIcon getWaypointIcon() {
        return redIcon;
    }
    
    /**
     * Renvoie la coordonnée associée au waypoint
     * 
     * @return La coordonnée en question
     */
    public T getCoord() {
        return coord;
    }
}
