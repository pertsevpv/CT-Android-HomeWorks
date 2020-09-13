package com.example.myapplication.expression;

public class Subtract extends CommonOperation {

    public Subtract(Expression l, Expression r) {
        super(l, r);
        sign = "-";
    }

    @Override
    protected double operate(double a, double b) {
        return a - b;
    }
}
