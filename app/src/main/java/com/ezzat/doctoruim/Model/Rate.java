package com.ezzat.doctoruim.Model;

public class Rate {

    private float rate;
    private int total;

    public Rate(){
        rate = 2.5f;
        total = 0;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public void addRate(float rate) {
        this.rate  += (rate - this.rate) / (total + 1);
    }
}
