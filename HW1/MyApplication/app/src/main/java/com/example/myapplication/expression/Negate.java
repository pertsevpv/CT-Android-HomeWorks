package com.example.myapplication.expression;

public class Negate implements Expression {

    private Expression a;

    public Negate(Expression a) {
        this.a = a;
    }

    @Override
    public double evaluate() {
        return -a.evaluate();
    }
}
