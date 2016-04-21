/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.drug;

import model.*;

/**
 *
 * @author CHRIS
 */
public class Heroin implements Drug {

    int price;

    public Heroin(int price) {
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
