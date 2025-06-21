/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
import model.circuit.CircuitEuc;
import model.coord.CoordEuc;
import view.waypoint.Waypoint;
import view.waypoint.WaypointEuc;

/**
 *
 * @author ugola
 */
public class MapEuc extends Map<CoordEuc, WaypointEuc, CircuitEuc> {
    private int offsetX, offsetY;

    public MapEuc() {        
        super();
        
        System.out.println("=> Creation de la carte euclidienne");

        offsetX = 0;
        offsetY = 0;
        

        ToolTipManager.sharedInstance().registerComponent(this);
        
        setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        
        initListeners();
        
        System.out.println("=> Carte euclidienne creee");
        
        setVisible(false);
    }
    
    @Override
    public void open(CircuitEuc circuit) {
        super.open(circuit);
        
        for (WaypointEuc waypoint : waypoints) {
            waypoint.update(offsetX, offsetY, scale);
        }
            
        repaint();
    }
    
    private void initListeners(){       
        // Gestion du déplacement sur la carte
        addMouseMotionListener(new MouseMotionListener() {
            private int lastX = -1;
            private int lastY = -1;

            @Override
            public void mouseDragged(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
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

        // Gestion du zoom
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
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(3));
        
        drawCircuit(circuit.getInsertionCircuit(), g2d, Color.BLUE);
        drawCircuit(circuit.getGreedyCircuit(), g2d, Color.YELLOW);
        drawCircuit(circuit.getRandomCircuit(), g2d, Color.GREEN);        
    }

    @Override
    protected void addCoord(double x, double y) {
        CoordEuc coord = new CoordEuc(x, y);
        circuit.addCoord(coord);
        addWaypoint(coord);
    }
    
    @Override
    protected void addWaypoint(CoordEuc coord) {
        WaypointEuc waypoint = new WaypointEuc(coord);
        waypoint.addActionListener(waypointListener);
        waypoints.add(waypoint);
        add(waypoint);
        waypoint.update(offsetX, offsetY, scale);
    }
    
    public void addCoord(MouseEvent e) {
        int x = (int) (e.getX() / scale - offsetX / scale - Waypoint.getWaypointIcon().getIconWidth() / 2 / scale);
        int y = (int) (e.getY() / scale - offsetY / scale - Waypoint.getWaypointIcon().getIconHeight() / 2 / scale);

        addCoord(x, y);

        repaint();
    }
        
    public void drawCircuit(List<CoordEuc> coords, Graphics2D graphics, Color color) {
        if (coords != null) {
            graphics.setColor(color);
            
            for (int i = 0; i < coords.size() - 1; i++) {                
                CoordEuc c1 = coords.get(i);
                CoordEuc c2 = coords.get(i + 1);
                
                int x1 = (int) (scale * c1.getX()) + offsetX + Waypoint.getWaypointIcon().getIconWidth() / 2;
                int y1 = (int) (scale * c1.getY()) + offsetY + Waypoint.getWaypointIcon().getIconHeight() / 2;
                
                int x2 = (int) (scale * c2.getX()) + offsetX + Waypoint.getWaypointIcon().getIconWidth() / 2;
                int y2 = (int) (scale * c2.getY()) + offsetY + Waypoint.getWaypointIcon().getIconHeight() / 2;
                
                graphics.drawLine(x1, y1, x2, y2);
            }
        }
    }
}
