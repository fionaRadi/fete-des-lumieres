/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.List;
import model.circuit.CircuitGeo;
import model.coord.CoordGeo;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.GeoPosition;

/**
 *
 * @author ugola
 */
public class CircuitGeoPainter implements Painter<JXMapViewer> {
    private CircuitGeo circuit;
    private Color greedyColor;
    private Color insertionColor;
    private Color randomColor;

    public CircuitGeoPainter(Color greedyColor, Color insertionColor, Color randomColor) {
        this.greedyColor = greedyColor;
        this.insertionColor = insertionColor;
        this.randomColor = randomColor;
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

        g.dispose();
    }
    
    private void drawCircuit(List<CoordGeo> circuit, Graphics2D g, JXMapViewer map, Color color) {
        if (circuit != null) {
            g.setColor(color);
            
            for (int i = 0; i < circuit.size() - 1; i++) {
                CoordGeo coord1 = circuit.get(i);
                CoordGeo coord2 = circuit.get(i + 1);

                GeoPosition pos1 = new GeoPosition(coord1.getLatitude(), coord1.getLongitude());
                GeoPosition pos2 = new GeoPosition(coord2.getLatitude(), coord2.getLongitude());
                
                Point2D p1 = map.convertGeoPositionToPoint(pos1);
                Point2D p2 = map.convertGeoPositionToPoint(pos2);
                
                g.draw(new Line2D.Double(p1, p2));
            }
        }
    }
}
