package expression.exceptions;
import expression.TripleExpression;

public class Result {
    TripleExpression accumulator;

    public Result(TripleExpression a) throws ParseException {
        accumulator = a;
    }
}
