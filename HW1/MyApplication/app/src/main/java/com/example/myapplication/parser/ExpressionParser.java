package com.example.myapplication.parser;

import com.example.myapplication.Pair;
import com.example.myapplication.R;
import com.example.myapplication.expression.*;

public class ExpressionParser extends BaseParser {

    private boolean isCorrect;
    private static int balance;

    @Override
    public Pair<Expression, Boolean> parse(String expression) {
        this.source = new StringSource(format(expression));
        nextChar();
        isCorrect = true;
        balance = 0;
        if (test('\0')) return new Pair<Expression, Boolean>(new EmptyExpression(), true);
        Expression exp = parseSecondPriority();
        if (!test('\0')) return new Pair<>(null, false);
        if (balance != 0) isCorrect = false;
        return new Pair<>(exp, isCorrect);
    }

    public Expression parseSecondPriority() {
        Expression left = parseFirstPriority();
        if (left == null || !isCorrect) return null;
        while (!test('\0')) {
            if (test('+')) {
                Expression right = parseFirstPriority();
                if (right == null || !isCorrect) return null;
                left = new Add(left, right);
                continue;
            }
            if (test('-')) {
                Expression right = parseFirstPriority();
                if (right == null || !isCorrect) return null;
                left = new Subtract(left, right);
                continue;
            }
            if (test(')')) {
                balance--;
                if (balance < 0) {
                    isCorrect = false;
                    return null;
                }
            }
            break;
        }
        return left;
    }

    public Expression parseFirstPriority() {
        Expression left = parseExpression();
        if (left == null || !isCorrect) return null;
        while (!test('\0')) {
            if (test('*')) {
                Expression right = parseExpression();
                if (right == null || !isCorrect) return null;
                left = new Multiply(left, right);
                continue;
            }
            if (test('/')) {
                Expression right = parseExpression();
                if (right == null || !isCorrect) return null;
                left = new Divide(left, right);
                continue;
            }
            if (test(')')) {
                balance--;
                if (balance < 0) {
                    isCorrect = false;
                    return null;
                }
            }
            break;
        }
        return left;
    }

    public Expression parseExpression() {
        if (test('-')) {
            if (Character.isDigit(cur)) {
                Double val = parseNumber(true);
                if (val == null || !isCorrect) return null;
                return new Const(val);
            }
            Expression exp = parseExpression();
            if (exp == null || !isCorrect) return null;
            return new Negate(parseExpression());
        }
        if (test('(')) {
            balance++;
            return parseSecondPriority();
        }
        if (Character.isDigit(cur)) {
            Double val = parseNumber(false);
            if (val == null || !isCorrect) return null;
            return new Const(val);
        }
        isCorrect = false;
        return null;
    }

    public Double parseNumber(boolean isNegative) {
        StringBuilder sb = new StringBuilder(isNegative ? "-" : "");
        while (Character.isDigit(cur) || cur == '.') {
            sb.append(cur);
            nextChar();
        }
        try {
            return Double.parseDouble(sb.toString());
        } catch (NumberFormatException e) {
            isCorrect = false;
            return null;
        }
    }

    public String format(String str) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                res.append(str.charAt(i));
            }
        }
        return res.toString();
    }
}