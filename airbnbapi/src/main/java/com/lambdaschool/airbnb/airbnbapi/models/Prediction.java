package com.lambdaschool.airbnb.airbnbapi.models;

public class Prediction {

    private double prediction;

    public Prediction() {
    }

    public Prediction(double prediction) {
        this.prediction = prediction;
    }

    public double getPrediction() {
        return prediction;
    }

    public void setPrediction(double prediction) {
        this.prediction = prediction;
    }
}
