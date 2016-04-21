/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chip;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author CHRIS
 */
public class Chip8 {
 
    private char[] memory;
    private char[] V;   //Data Registers - 16
    private char I;   //Address Registers - 16
    private char pc;    //Program counter (Memory address)
    
    private char[] stack;
    private int stackPointer;
    
    private int delayTimer, soundTimer;
    
    private char[] input, display;
    
    private boolean redraw, skipInstruction;
    
    public Chip8() {
        init();
    }
    
    public void init() {
        memory = new char[4096];
        V = new char[16];
        I = 0x0;
        pc = 0x200;
        
        stackPointer = 0;
        stack = new char[16];
        
        input = new char[16];
        display = new char[64 * 32];
    }
    
    public char[] getDisplay() {
        return display;
    }

    public void run() {
        //Fetch Opcode
        char opCode = (char) ((memory[pc] << 8) | memory[pc + 1]);
        System.out.print(Integer.toHexString(opCode).toUpperCase() + ": ");
        //Decode Opcode
        int masked = (opCode & 0xF000);
        switch (masked) {  //Masks out all bits except for the the first, example: 0NNN masks out, so only 0 is there
            case 0x0000: 
                int multicase = opCode & 0x000F;
                switch (multicase) {
                    case 0x0000: //00E0	Clears the screen.
                        
                        break;
                    case 0x000E: //00EE	Returns from a subroutine.
                        pc = stack[--stackPointer];
                        System.out.println("Returning from subroutine to " + Integer.toHexString(pc).toUpperCase());
                        incrementProgramCounter();
                        break;
                    default:
                        System.out.println("Unsupported Opcode");
                        System.exit(0);
                        break;
                }
                break;
                
            case 0x1000: //1NNN	Jumps to address NNN.
                char address = (char) (opCode & 0x0FFF);
                pc = address;
                System.out.println("Jumping to address " + Integer.toHexString(address));
                break;
                
            case 0x2000: //Calls subroutine at NNN.
                address = (char) (opCode & 0x0FFF);
                stack[stackPointer++] = pc;
                pc = address;
                System.out.println("Calling subrutine at " + Integer.toHexString(pc));
                break;
                
            case 0x3000: //3XNN: Skips the next instruction if VX equals NN.
                int x = (opCode & 0x0F00) >> 8;
                int nn = (char) (opCode & 0x00FF);
                if (V[x] == nn) {
                    incrementProgramCounter(2);
                    System.out.println("Skipping next instruction; V[" + x + "] == " + nn);
                } else {
                    incrementProgramCounter();
                    System.out.println("Not skipping next instruction; V[" + x + "] != " + nn);
                }
                //System.out.println("Unsupported Opcode");
                //System.exit(0);
                break;
                
            case 0x4000: 
                System.out.println("Unsupported Opcode");
                System.exit(0);
                break;
                
            case 0x5000: 
                System.out.println("Unsupported Opcode");
                System.exit(0);
                break;
                
            case 0x6000: //6XNN: Sets VX to NN.
                x = (opCode & 0x0F00) >> 8;
                nn = (char) (opCode & 0x00FF);
                V[x] = (char) nn;
                incrementProgramCounter();
                System.out.println("Setting V[" + x + "] to " + (int) nn);
                break;
                
            case 0x7000: //7XNN	Adds NN to VX.
               // System.out.println("Case " + masked);
                x = (opCode & 0x0F00) >> 8;
                nn = (char) (opCode & 0x00FF);
                V[x] += (nn & 0xFF);
                incrementProgramCounter();
                System.out.println("Adding " + nn + " to V[" + x + "], V[" + x + "] == " + (int) V[x]);
                break;
                
            case 0x8000: 
                System.out.println("Unsupported Opcode");
                System.exit(0);
                break;
                
            case 0x9000: 
                System.out.println("Unsupported Opcode");
                System.exit(0);
                break;
                
            case 0xA000: //ANNN: Sets I to the address NNN.
                address = (char) (opCode & 0x0FFF);
                I = address;
                incrementProgramCounter();
                System.out.println("Setting I to address " + Integer.toHexString(I));
                break;
                
            case 0xB000: 
                System.out.println("Unsupported Opcode");
                System.exit(0);
                break;
            
            case 0xD000: //DXYN: Draw Sprite (X, Y) with size (8, N). Sprite is located at I
                V[0xF] = 0;
                x = V[(opCode & 0x0F00) >> 8];
                int y = V[(opCode & 0x00F0) >> 4];
                int height = opCode & 0xF;
                for (int pointY = 0; pointY < height; pointY++) {
                    int line = memory[I + pointY];
                    for (int pointX = 0; pointX < 8; pointX++) {
                        int pixel = line & (0x80 >> pointX);
                        if (pixel != 0) {
                            int totalX = x + pointX;
                            int totalY = y + pointY;
                            int index = totalY * 64 + totalX;
                            if (display[index] == 1) {
                                V[0xF] = 1;
                            }
                            display[index] ^= 1;
                        }
                    }
                }
                incrementProgramCounter();
                setRedraw(true);
                System.out.println("Draw sprite at I " + Integer.toHexString(I));
                break;
            
            case 0xF000: //Multiple cases
                multicase = opCode & 0x00FF;
                switch (multicase) {
                    
                    
                    
                }
                
            default: System.out.println("Unsupported Opcode");
            System.exit(0);
        }
        //Execute Opcode
    }
    
    public void loadProgram(String path) {
        loadFont(ChipData.fontset);
        try {
            DataInputStream input = new DataInputStream(new FileInputStream(path));
            for (int offset = 0; input.available() > 0; offset++) {
                memory[0x200 + offset] = (char) (input.readByte() & 0xFF);
            }
        } catch (FileNotFoundException ex) {
            System.err.println("Error reading file from: " + path);
        } catch (IOException ex) {
            Logger.getLogger(Chip8.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void loadFont(int[] fonts) {
        for (int offset = 0; offset < fonts.length; offset++) {
            memory[0x50 + offset] = (char) fonts[offset];
        }
    }
    
    public boolean shouldRedraw() {
        return redraw;
    }
    
    public void setRedraw(boolean flag) {
        redraw = flag;
    }
    
    public void incrementProgramCounter() {
        incrementProgramCounter(1);
    }
    
    public void incrementProgramCounter(int times) {
        pc += (2 * times);
    }
    
}