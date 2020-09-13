package com.example.myapplication.parser;

import com.example.myapplication.Pair;
import com.example.myapplication.expression.Expression;

public interface Parser {
    Pair<Expression, Boolean> parse(String expression);
}
