/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emu;

import chip.Chip8;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author CHRIS
 */
public class Main extends Thread {

    int MODIFIER = 5;
    String programPath = "programs/pong2.c8";
    
    Chip8 chip;
    ChipFrame frame;
    
    public Main() {
        chip = new Chip8();
        frame = new ChipFrame(chip, MODIFIER);
        chip.loadProgram(programPath);
        run();
    }
    
    public static void main(String[] args) {
        Main main = new Main();
    }

    @Override
    public void run() {
        while (true) {
            try {
                chip.run();
                if (chip.shouldRedraw()) {
                    frame.repaint();
                    chip.setRedraw(false);
                }
                Thread.sleep(16);
            } catch (InterruptedException ex) {
            }
        }
    }
    
}