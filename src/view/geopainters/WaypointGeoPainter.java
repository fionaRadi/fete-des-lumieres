package view.geopainters;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.WaypointPainter;
import view.waypoint.Waypoint;
import view.waypoint.WaypointGeo;

/**
 * Classe qui gère l'affichage des waypoints dans la map géographique
 * 
 * @author ugola
 */
public class WaypointGeoPainter extends WaypointPainter<WaypointGeo> {         
    
    
    @Override
    protected void doPaint(Graphics2D g, JXMapViewer viewer, int width, int height) {
        for (WaypointGeo waypoint : getWaypoints()) {
            Point2D point = viewer.getTileFactory().geoToPixel(waypoint.getPosition(), viewer.getZoom());
            Rectangle rect = viewer.getViewportBounds();

            int x = (int) (point.getX() - rect.getX());
            int y = (int) (point.getY() - rect.getY());

            waypoint.setLocation(x - Waypoint.getWaypointIcon().getIconWidth() / 2, y - Waypoint.getWaypointIcon().getIconHeight()/ 2);
        }
    }
}
