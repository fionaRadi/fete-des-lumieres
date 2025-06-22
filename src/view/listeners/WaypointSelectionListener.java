/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view.listeners;

import view.waypoint.Waypoint;

/**
 * Un listener qui sera déclenché au moment où un waypoint est sélectionné
 *
 * @author ugola
 */
public interface WaypointSelectionListener {
    void onWaypointSelected(Waypoint waypoint);
}