package expression.exceptions;

import expression.Const;
import expression.TripleExpression;
import expression.Variable;

public class ExpressionParser implements Parser {
    private PSource source;
    private int cntIn = 0;

    public TripleExpression parse(String expression) throws ParseException {
        source = new PSource(expression);
        source.nextChar();

        final TripleExpression res = addDivide();
        expect(PSource.END);
        return res;
    }

    private TripleExpression addDivide() throws ParseException {
        skipSpaces();

        TripleExpression acc = mulDiv();
        skipSpaces();
        while (test('+') || test('-')) {
            char sign = source.getChar();
            source.nextChar();

            cntIn++;
            TripleExpression cur = mulDiv();
            cntIn--;

            skipSpaces();
            if (sign == '+') {
                acc = new CheckedAdd(acc, cur);
            } else {
                acc = new CheckedSubtract(acc, cur);
            }

        }
        return acc;
    }

    private TripleExpression mulDiv() throws ParseException {
        TripleExpression cur = unary();
        skipSpaces();

        TripleExpression acc = cur;
        while (test('*') || test('/')) {
            char type = source.getChar();
            source.nextChar();

            cntIn++;
            cur = unary();
            cntIn--;

            skipSpaces();

            if (type == '*') {
                acc = new CheckedMultiply(acc, cur);
            } else {
                acc = new CheckedDivide(acc, cur);
            }
        }

        return acc;
    }

    private TripleExpression unary() throws ParseException {
        skipSpaces();

        if (test('-') || test('+')) {
            if (cntIn > 0 && !source.prevIsBracket() && test('+')) {
                throw source.error("Invalid name of variable: '%s'", source.getChar());
            }
            char sign = source.getChar();
            source.nextChar();
            skipSpaces();

            if (testDigit()) {
                return number(sign);
            } else {
                return new CheckedSubtract(new Const(0), unary());
            }

        } else if (source.extend("low")) {
            return new CheckedLow(unary());
        } else if (source.extend("high")) {
            return new CheckedHigh(unary());
        } else if (test('(')) {
            source.nextChar();
            TripleExpression cur = addDivide();
            skipSpaces();

            expect(')');

            return cur;
        } else {
            return variable();
        }
    }

    private TripleExpression variable() throws ParseException {
        skipSpaces();

        if (test('-') || testDigit()) {
            return number('+');
        }
        if (!test('x') && !test('y') && !test('z')) {
            throw source.error("Invalid name of variable: '%s'", source.getChar());
        }

        char val = source.getChar();
        source.nextChar();
        return new Variable(Character.toString(val));
    }

    private TripleExpression number(char c) throws ParseException {
        final StringBuilder sb = new StringBuilder();
        sb.append(c);
        readDigits(sb);
        try {
            int res = Integer.parseInt(sb.toString());
            return new Const(res);
        } catch (final NumberFormatException e){
            throw source.error("Invalid number '%s'", sb);
        }
    }

    private void readDigits(final StringBuilder sb) {
        do {
            sb.append(source.getChar());
        } while (Character.isDigit(source.nextChar()));
    }

    private boolean test(final char c) {
        return source.getChar() == c;
    }

    private boolean testDigit() {
        return Character.isDigit(source.getChar());
    }

    private void expect(final char expected) throws ParseException {
        final char actual = source.getChar();
        if (actual == expected) {
            if (expected != PSource.END) {
                source.nextChar();
            }
        } else if (actual != PSource.END) {
            throw source.error("Expected %s, found '%s' (%d)", "end of file", actual, (int) actual);
        } else {
            throw source.error("Expected %s, found EOF", "end of file");
        }
    }

    private void skipSpaces() {
        while (Character.isWhitespace(source.getChar())) {
            source.nextChar();
        }
    }
}
