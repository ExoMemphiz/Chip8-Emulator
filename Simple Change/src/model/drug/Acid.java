/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.drug;

import model.Drug;

/**
 *
 * @author CHRIS
 */
public class Acid implements Drug {

    int price;

    public Acid(int price) {
        this.price = price;
    }
    
    @Override
    public String getName() {
        return "Heroin";
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public void setPrice(int price) {
        this.price = price;
    }
    
}
