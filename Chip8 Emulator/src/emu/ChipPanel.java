/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emu;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import chip.Chip8;

public class ChipPanel extends JPanel {
	
    private int modifier;
    private Chip8 chip;

    public ChipPanel(Chip8 chip, int modifier) {
        this.chip = chip;
        this.modifier = modifier;
    }

    public void paint(Graphics g) {
        char[] display = chip.getDisplay();
        for(int i = 0; i < display.length; i++) {
            if(display[i] == 0) {
                g.setColor(Color.BLACK);
            } else {
                g.setColor(Color.WHITE);
            }
            int x = (i % 64);
            int y = (int)Math.floor(i / 64);

            g.fillRect(x * modifier, y * modifier, modifier, modifier);
        }
    }

}
