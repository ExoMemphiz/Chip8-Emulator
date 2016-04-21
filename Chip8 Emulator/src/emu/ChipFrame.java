/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emu;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import chip.Chip8;

public class ChipFrame extends JFrame {

    int modifier;
    private final ChipPanel panel;

    public ChipFrame(Chip8 c, int modifier) {
        this.modifier = modifier;
        setPreferredSize(new Dimension(64 * modifier, 32 * modifier));
        pack();
        setPreferredSize(new Dimension((64 * modifier) + getInsets().left + getInsets().right, (32 * modifier) + getInsets().top + getInsets().bottom));
        panel = new ChipPanel(c, modifier);
        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Chip 8 Emulator");
        pack();
        setVisible(true);
    }
	
}
