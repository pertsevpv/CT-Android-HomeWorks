package com.example.myapplication.expression;

public class Const implements Expression {

    private Double value;

    public Const(double d) {
        value = d;
    }

    @Override
    public double evaluate() {
        return value;
    }
}
