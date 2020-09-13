package com.example.myapplication.expression;

public class Add extends CommonOperation {

    public Add(Expression l, Expression r) {
        super(l, r);
        sign = "+";
    }

    @Override
    protected double operate(double a, double b) {
        return a + b;
    }
}
