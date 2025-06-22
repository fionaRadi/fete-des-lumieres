package view.listeners;

import java.awt.event.MouseEvent;

/**
 * Un listener qui sera déclenché au moment où la map est clickée
 * @author ugola
 */
public interface MapClickedListener {
    void onMapClicked(MouseEvent e);
}
