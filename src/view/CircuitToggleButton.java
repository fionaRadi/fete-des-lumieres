/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author ugola
 */

public class CircuitToggleButton extends JToggleButton {
    private final JLabel titleLabel = new JLabel("Itinéraire");
    private final JLabel subtitleLabel = new JLabel("Distance : 0 km");

    public CircuitToggleButton() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.GRAY));
        setBackground(Color.LIGHT_GRAY);
        setFocusPainted(false);

        // Police plus grande (taille 16)
        Font fontTitle = titleLabel.getFont().deriveFont(Font.PLAIN, 16f);
        titleLabel.setFont(fontTitle);

        Font fontSubtitle = subtitleLabel.getFont().deriveFont(Font.PLAIN, 12f);
        subtitleLabel.setFont(fontSubtitle);

        // Centrage horizontal + vertical
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setVerticalAlignment(SwingConstants.CENTER);

        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        subtitleLabel.setVerticalAlignment(SwingConstants.CENTER);

        // Panel pour contenir les 2 labels verticalement
        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false); // transparent pour voir le fond du bouton

        // Ajout des labels avec marge entre eux (ex : 5px)
        titleLabel.setBorder(new EmptyBorder(0, 0, 5, 0));
        subtitleLabel.setBorder(new EmptyBorder(5, 0, 0, 0));

        textPanel.add(titleLabel);
        textPanel.add(subtitleLabel);

        add(textPanel, BorderLayout.CENTER);
    }

    public String getTitleText() {
        return titleLabel.getText();
    }

    public void setTitleText(String text) {
        titleLabel.setText(text);
    }

    public String getSubtitleText() {
        return subtitleLabel.getText();
    }

    public void setSubtitleText(String text) {
        subtitleLabel.setText(text);
    }
}


