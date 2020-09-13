package com.example.myapplication.expression;

public class EmptyExpression implements Expression {

    public EmptyExpression(){}

    @Override
    public double evaluate() {
        return 0;
    }
}
