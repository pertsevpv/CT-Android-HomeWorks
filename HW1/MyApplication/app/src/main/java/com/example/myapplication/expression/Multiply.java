package com.example.myapplication.expression;

public class Multiply extends CommonOperation {

    public Multiply(Expression l, Expression r) {
        super(l, r);
        sign = "*";
    }

    @Override
    protected double operate(double a, double b) {
        return a * b;
    }
}
