package com.example.myapplication.parser;

public abstract class BaseParser implements Parser {

    public StringSource source;
    protected char cur;
    protected char prev;

    public BaseParser(){

    }

    private BaseParser(StringSource source) {
        this.source = source;
    }

    protected void nextChar() {
        prev = cur;
        cur = source.hasNext() ? source.next() : '\0';
    }

    protected boolean test(char expected) {
        if (cur == expected) {
            nextChar();
            return true;
        }
        return false;
    }

    protected boolean test(String expected) {
        for (int i = 0; i < expected.length(); i++) {
            if (source.data.charAt(source.pos - 1 + i) != expected.charAt(i)) return false;
        }
        source.pos += expected.length() - 1;
        nextChar();
        return true;
    }

    protected void expect(final char c) {
        if (cur != c) {
            throw new ExpressionException("Incorrect Character '" + cur + "'");
        }
        nextChar();
    }

    protected void expect(final String value) {
        for (char c : value.toCharArray()) {
            expect(c);
        }
    }

    public void skipWhiteSpace() {
        while (true) {
            if (Character.isWhitespace(cur)) nextChar();
            else return;
        }
    }
}
