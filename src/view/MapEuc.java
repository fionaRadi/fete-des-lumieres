/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import model.coord.CoordEuc;
import view.waypoint.Waypoint;
import view.waypoint.WaypointEuc;

/**
 *
 * @author ugola
 */
public class MapEuc extends Map<CoordEuc, WaypointEuc> {
    private int offsetX, offsetY;
    private boolean isRightClickDragging;
    
    private ActionListener waypointListener;

    public MapEuc() {        
        super();
        
        System.out.println("=> Creation de la carte euclidienne");
        
        isRightClickDragging = false;

        waypoints = new ArrayList<>();

        offsetX = 0;
        offsetY = 0;
        
        waypointListener = (ActionEvent e) -> {
            selectedWaypoint = (WaypointEuc) e.getSource();
        };

        ToolTipManager.sharedInstance().registerComponent(this);
        
        setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        
        initListeners();
        
        System.out.println("=> Carte euclidienne creee");
        
        setVisible(false);
    }
    
    @Override
    public void open(List<CoordEuc> coords) {
        super.open(coords);
        
        for (WaypointEuc waypoint : waypoints) {
            waypoint.update(offsetX, offsetY, scale);
        }
            
        repaint();
    }
    
    private void initListeners(){
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}

            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    isRightClickDragging = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    isRightClickDragging = false;
                }
                
                else if (SwingUtilities.isLeftMouseButton(e)) {
                    int x = (int) (e.getX() / scale - offsetX / scale - Waypoint.getWaypointIcon().getIconWidth() / 2 / scale);
                    int y = (int) (e.getY() / scale - offsetY / scale - Waypoint.getWaypointIcon().getIconHeight() / 2 / scale);
                    
                    WaypointEuc waypoint = addWaypoint(new CoordEuc(2, x, y));
                    waypoint.update(offsetX, offsetY, scale);
                    
                    repaint();
                }
            }


            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
            
        });
        
        addMouseMotionListener(new MouseMotionListener() {
            private int lastX = -1;
            private int lastY = -1;

            @Override
            public void mouseDragged(MouseEvent e) {
                if (isRightClickDragging) {
                    if (lastX != -1 && lastY != -1) {
                        offsetX += e.getX() - lastX;
                        offsetY += e.getY() - lastY;
                    }
                    lastX = e.getX();
                    lastY = e.getY();

                    for (Waypoint waypoint : waypoints) {
                        WaypointEuc waypointEuc = (WaypointEuc) waypoint;
                        waypointEuc.update(offsetX, offsetY, scale);
                    }

                    repaint();
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                lastX = e.getX();
                lastY = e.getY();
            }
        });

        addMouseWheelListener((MouseWheelEvent e) -> {
            double screenMouseX = e.getX();
            double screenMouseY = e.getY();
            
            double mapMouseX = (screenMouseX - offsetX) / scale;
            double mapMouseY = (screenMouseY - offsetY) / scale;
            
            scale = scale + -e.getWheelRotation() * 0.1;
            if (scale < 0.5) scale = 0.5; else if (scale > 2) scale = 2;
            
            offsetX = (int) (screenMouseX - mapMouseX * scale);
            offsetY = (int) (screenMouseY - mapMouseY * scale);
            
            for (WaypointEuc waypoint : waypoints) {
                waypoint.update(offsetX, offsetY, scale);
            }
            
            repaint();
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        for (List<Integer> circuit : circuits) {
            for (int i = 0; i < circuit.size() - 1; i++) {
                
            }
        
        super.paintComponent(g);
    }
}

    @Override
    public WaypointEuc addWaypoint(CoordEuc coord) {
        WaypointEuc waypoint = new WaypointEuc(coord);
        waypoint.addActionListener(waypointListener);
        waypoints.add(waypoint);
        add(waypoint);
        return waypoint;
    }
    
    public WaypointEuc getSelectedWaypoint() {
        return selectedWaypoint;
    }
    
    public void drawCircuit() {
        
    }
}
