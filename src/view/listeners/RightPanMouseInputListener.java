/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view.listeners;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;
import org.jxmapviewer.JXMapViewer;

/**
 * Version alternative du PanMouseInputListener de JxMapViewer permettant d'effectuer le déplacement avec le clic droit
 * 
 * @author ugola
 */
public class RightPanMouseInputListener extends MouseInputAdapter {
    private Point prev;
    private JXMapViewer viewer;
    private Cursor priorCursor;
    
    /**
     * @param viewer the jxmapviewer
     */
    public RightPanMouseInputListener(JXMapViewer viewer)
    {
        this.viewer = viewer;
    }

    @Override
    public void mousePressed(MouseEvent evt)
    {
        if (!SwingUtilities.isRightMouseButton(evt))
            return;
        if (!viewer.isPanningEnabled())
            return;
        
        prev = evt.getPoint();
        priorCursor = viewer.getCursor();
        viewer.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
    }

    @Override
    public void mouseDragged(MouseEvent evt)
    {
        if (!SwingUtilities.isRightMouseButton(evt))
            return;
        if (!viewer.isPanningEnabled())
            return;
        
        Point current = evt.getPoint();
        double x = viewer.getCenter().getX();
        double y = viewer.getCenter().getY();

        if(prev != null){
                x += prev.x - current.x;
                y += prev.y - current.y;
        }

        int maxHeight = (int) (viewer.getTileFactory().getMapSize(viewer.getZoom()).getHeight() * viewer
                .getTileFactory().getTileSize(viewer.getZoom()));
        if (y > maxHeight)
        {
            y = maxHeight;
        }

        prev = current;
        viewer.setCenter(new Point2D.Double(x, y));
        viewer.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent evt)
    {
        if (!SwingUtilities.isRightMouseButton(evt))
            return;

        prev = null;
        viewer.setCursor(priorCursor);
    }
}
