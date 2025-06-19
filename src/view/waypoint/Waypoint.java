/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view.waypoint;

import java.awt.Dimension;
import java.util.Objects;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author ugola
 */
public abstract class Waypoint extends JButton {
    protected static ImageIcon redIcon;

    private static final String RED_ICON_PATH = "../../resources/images/location-dot-solid-red.png";

    public Waypoint() {
        if (redIcon == null) {
            loadImage();
        }

        setContentAreaFilled(false);
        setIcon(redIcon);
        setSize(new Dimension(redIcon.getIconWidth(), redIcon.getIconHeight()));

        setBorder(null);
    }

    public static void loadImage() {
        System.out.println("=> Chargement des images des waypoints");
        try {
            redIcon = new ImageIcon(Objects.requireNonNull(Waypoint.class.getResource(RED_ICON_PATH)));
            
            System.out.println("=> Images des waypoints chargees");
            
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des fichiers");
        }
    }
    
    public static ImageIcon getWaypointIcon() {
        return redIcon;
    }
}
