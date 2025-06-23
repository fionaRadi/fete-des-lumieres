/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view.geopainters;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.List;
import model.Constants;
import model.circuit.CircuitGeo;
import model.coord.CoordGeo;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.GeoPosition;

/**
 * Classe qui se charge de dessiner des circuits sur la map géographique
 * 
 * @author ugola
 */
public class CircuitGeoPainter implements Painter<JXMapViewer> {
    private CircuitGeo circuit;
    private Color greedyColor;
    private Color insertionColor;
    private Color randomColor;
    private Color ameliorateColor;

    public CircuitGeoPainter(Color greedyColor, Color insertionColor, Color randomColor, Color ameliorateColor) {
        this.greedyColor = greedyColor;
        this.insertionColor = insertionColor;
        this.randomColor = randomColor;
        this.ameliorateColor = ameliorateColor;
    }

    public void setCircuit(CircuitGeo circuit) {
        this.circuit = circuit;
    }
    
    @Override
    public void paint(Graphics2D g, JXMapViewer map,  int w, int h) {
        g.setStroke(new BasicStroke(3));

        drawCircuit(circuit.getGreedyCircuit(), g, map, greedyColor);
        drawCircuit(circuit.getInsertionCircuit(), g, map, insertionColor);
        drawCircuit(circuit.getRandomCircuit(), g, map, randomColor);
        drawCircuit(circuit.getAmeliorateCircuit(), g, map, ameliorateColor);

        g.dispose();
    }
    
    private void drawCircuit(List<CoordGeo> circuitToDraw, Graphics2D g, JXMapViewer map, Color color) {
        if (circuitToDraw != null) {            
            for (int i = 0; i < circuitToDraw.size() - 1; i++) {
                g.setColor(color);
                            
                CoordGeo c1 = circuitToDraw.get(i);
                CoordGeo c2 = circuitToDraw.get(i + 1);

                GeoPosition pos1 = c1.getPosition();
                GeoPosition pos2 = c2.getPosition();
                
                Point2D p1 = map.convertGeoPositionToPoint(pos1);
                Point2D p2 = map.convertGeoPositionToPoint(pos2);
                
                g.draw(new Line2D.Double(p1, p2));
                
                if (Constants.DISPLAY_DISTANCE) {
                    double distance = circuit.calculateDistance(c1, c2);

                    int xm = (int) ((p1.getX() + p2.getX()) / 2);
                    int ym = (int) ((p1.getY() + p2.getY()) / 2);

                    g.setColor(Color.BLACK);
                    g.drawString(String.format("%.1f", distance), xm + 15, ym + 15);
                }
            }
        }
    }
}
