package expression.parser;

import expression.*;

public class ExpressionParser implements Parser {

    public TripleExpression parse(String expression) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < expression.length(); i++) {
            if (!Character.isWhitespace(expression.charAt(i))) s.append(expression.charAt(i));
        }

        Result res = plusMinus(new FastString(s.toString()));
        return res.accumulator;
    }

    private Result plusMinus(FastString expression) {
        Result cur = mulDiv(expression);
        TripleExpression acc = cur.accumulator;

        while(!cur.res.isEmpty()) {
            if (cur.res.first() != '+' && cur.res.first() != '-') {
                break;
            }

            char type = cur.res.first();
            cur.res.deleteFirst();

            cur = mulDiv(cur.res);

            if (type == '+') {
                acc = new Add(acc, cur.accumulator);
            } else {
                acc = new Subtract(acc, cur.accumulator);
            }
        }

        cur.accumulator = acc;
        return cur;
    }

    private Result mulDiv(FastString expression) {
        Result cur = unaryMinus(expression);
        TripleExpression acc = cur.accumulator;

        while(!cur.res.isEmpty()) {
            if (cur.res.first() != '*' && cur.res.first() != '/') {
                break;
            }

            char type = cur.res.first();
            cur.res.deleteFirst();

            cur = unaryMinus(cur.res);

            if (type == '*') {
                acc = new Multiply(acc, cur.accumulator);
            } else {
                acc = new Divide(acc, cur.accumulator);
            }
        }
        cur.accumulator = acc;
        return cur;
    }

    private Result unaryMinus(FastString expression) {

        int cnt = 0;
        while (!expression.isEmpty() && expression.first() == '-') {
            expression.deleteFirst();
            cnt++;
        }

        Result cur = bracket(expression);

        if (cnt % 2 == 1) cur.accumulator = new Subtract(new Const(0), cur.accumulator);
        return cur;
    }

    private Result bracket(FastString expression) {

        if (expression.first() == '(') {
            expression.deleteFirst();
            Result cur = plusMinus(expression);

            if (cur.res.first() != ')') {
                System.err.println("Missing closing bracket");
                System.exit(0);
            }
            cur.res.deleteFirst();
            return cur;
        }

        return variable(expression);
    }

    private Result variable(FastString expression) {
        char first = expression.first();

        if (first == '-' || Character.isDigit(first)) {
            return number(expression);
        }

        if (first != 'x' && first != 'y' && first != 'z') {
            System.err.println("Invalid name of variable");
            System.exit(0);
        }

        expression.deleteFirst();
        return new Result(new Variable(Character.toString(first)), expression);
    }

    private Result number(FastString expression) {
        int res = 0;
        while (!expression.isEmpty() && Character.isDigit(expression.first())) {
            res = res * 10 + expression.first() - '0';
            expression.deleteFirst();
        }
        return new Result(new Const(res), expression);
    }
}
