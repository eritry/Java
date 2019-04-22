package expression.expressions;
import expression.exceptions.ParseException;
import expression.generic.NumberException;
import expression.operations.Operation;

public class Sqr<T> extends UnOperator<T> {
    public Sqr(final TripleExpression<T> x, final Operation y) {
        super(x, y);
    }

    protected T operator(final T x) throws NumberException {
        return operation.sqr(x);
    }
}