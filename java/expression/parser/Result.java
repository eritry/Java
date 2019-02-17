package expression.parser;

import expression.TripleExpression;

public class Result {
    TripleExpression accumulator;
    FastString res;

    public Result(TripleExpression a, FastString r) {
        accumulator = a;
        res = r;
    }

    public Result(TripleExpression a, String r) {
        accumulator = a;
        res = new FastString(r);
    }
}
