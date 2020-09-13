package com.example.myapplication.expression;

public abstract class CommonOperation implements Expression {

    private Expression a;
    private Expression b;
    protected String sign;

    protected abstract double operate(double a, double b);

    public CommonOperation(Expression l, Expression r) {
        this.a = l;
        this.b = r;
    }

    @Override
    public double evaluate() {
        return this.operate(a.evaluate(), b.evaluate());
    }
}
