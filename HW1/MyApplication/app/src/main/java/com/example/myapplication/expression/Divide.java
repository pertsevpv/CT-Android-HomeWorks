package com.example.myapplication.expression;

public class Divide extends CommonOperation {

    public Divide(Expression l, Expression r) {
        super(l, r);
        sign = "/";
    }

    @Override
    protected double operate(double a, double b) {
        return a / b;
    }
}
