package expression.expressions;
import expression.generic.NumberException;
import expression.operations.Operation;

public class Abs<T> extends UnOperator<T> {
    public Abs(final TripleExpression<T> x, final Operation y) {
        super(x, y);
    }

    protected T operator(final T x) throws NumberException {
        return operation.abs(x);
    }
}