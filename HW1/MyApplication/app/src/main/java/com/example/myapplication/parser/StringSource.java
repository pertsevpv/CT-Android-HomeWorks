package com.example.myapplication.parser;

public class StringSource implements ExpressionSource {

    protected final String data;
    public int pos;

    public StringSource(String data) {
        this.data = data;
    }

    @Override
    public boolean hasNext() {
        return pos < data.length();
    }

    @Override
    public char next() {
        return data.charAt(pos++);
    }

    @Override
    public ExpressionException error(String message) {
        return new ExpressionException(pos + ": " + message);
    }

}
