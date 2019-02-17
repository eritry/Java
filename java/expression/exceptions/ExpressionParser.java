package expression.exceptions;
import expression.TripleExpression;

import expression.Add;
import expression.Multiply;
import expression.Divide;
import expression.Subtract;
import expression.Variable;
import expression.Const;
import expression.High;
import expression.Low;

import java.io.IOException;

public class ExpressionParser implements Parser {
    private PSource source;
    public TripleExpression parse(String expression) throws ParseException {
        source = new PSource(expression);

        source.nextChar();

        skipSpaces();
        final Result res = plusMinus();
        expect(PSource.END,"end of file");
        return res.accumulator;
    }

    private Result plusMinus() throws ParseException {
        skipSpaces();
        Result cur = mulDiv();
        skipSpaces();

        TripleExpression acc = cur.accumulator;

        while (test('+') || test('-')) {
            char sign = source.getChar();
            source.nextChar();

            cur = mulDiv();
            skipSpaces();

            if (sign == '+') {
                acc = new Add(acc, cur.accumulator);
            } else {
                acc = new Subtract(acc, cur.accumulator);
            }

        }

        cur.accumulator = acc;
        return cur;
    }

    private Result mulDiv() throws ParseException {
        skipSpaces();
        Result cur = unary();
        skipSpaces();

        TripleExpression acc = cur.accumulator;

        while (test('*') || test('/')) {
            char type = source.getChar();
            source.nextChar();

            cur = unary();
            skipSpaces();

            if (type == '*') {
                acc = new Multiply(acc, cur.accumulator);
            } else {
                acc = new Divide(acc, cur.accumulator);
            }
        }

        cur.accumulator = acc;
        return cur;
    }

    private Result unary() throws ParseException {
        skipSpaces();

        Result cur;
        if (test('-')) {
            source.nextChar();
            skipSpaces();
            boolean isDigit = false;

            if (testDigit()) {
                isDigit = true;
                cur = number('-');
            } else {
                cur = unary();
            }

            if (isDigit) {
                cur.accumulator = new Add(new Const(0), cur.accumulator);
            } else {
                cur.accumulator = new Subtract(new Const(0), cur.accumulator);
            }

        } else if (source.extend("low")) {
            cur = unary();
            cur.accumulator = new Low(cur.accumulator);
        } else if (source.extend("high")) {
            cur = unary();
            cur.accumulator = new High(cur.accumulator);
        } else {
            cur = bracket();
        }
        return cur;
    }

    private Result bracket() throws ParseException {
        skipSpaces();
        if (test('(')) {
            source.nextChar();
            Result cur = plusMinus();
            skipSpaces();

            if (!test(')')) {
                throw source.error("Expected ')', founded '%s'", source.getChar());
            }

            source.nextChar();
            return cur;
        }

        return variable();
    }

    private Result variable() throws ParseException {
        skipSpaces();

        if (test('-') || testDigit()) {
            return number('+');
        }

        if (!test('x') && !test('y') && !test('z')) {
            throw source.error("Invalid name of variable: '%s'", source.getChar());
        }

        char val = source.getChar();
        source.nextChar();
        return new Result(new Variable(Character.toString(val)));
    }

    private Result number(char c) throws ParseException {
        final StringBuilder sb = new StringBuilder();
        sb.append(c);
        readDigits(sb);
        try {
            int res = Integer.parseInt(sb.toString());
            return new Result(new Const(res));

        } catch (final NumberFormatException e){
            throw source.error("Invalid number '%s'", sb);
        }
    }

    private void readDigits(final StringBuilder sb) throws ParseException {
        do {
            sb.append(source.getChar());
        } while (Character.isDigit(source.nextChar()));
    }

    private boolean testNext(final char c) throws ParseException {
        if (source.getChar() == c) {
            source.nextChar();
            return true;
        } else {
            return false;
        }
    }

    private boolean test(final char c) {
        return source.getChar() == c;
    }

    private boolean testDigit() {
        return Character.isDigit(source.getChar());
    }

    private void expect(final char expected, final String errorMessage) throws ParseException {
        final char actual = source.getChar();
        if (actual == expected) {
            if (expected != PSource.END) {
                source.nextChar();
            }
        } else if (actual != PSource.END) {
            throw source.error("Expected %s, found '%s' (%d)", errorMessage, actual, (int) actual);
        } else {
            throw source.error("Expected %s, found EOF", errorMessage);
        }
    }

    private void skipSpaces() throws ParseException {
        while (Character.isWhitespace(source.getChar())) {
            source.nextChar();
        }
    }
}
