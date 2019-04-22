package expression.expressions;

import expression.exceptions.ParseException;
import expression.generic.NumberException;
import expression.operations.Operation;

public class Divide<T> extends Operator<T> implements TripleExpression<T> {

    public Divide(TripleExpression<T> f, TripleExpression<T> s, Operation o) {
        super(f, s, o);
    }

    protected T operator(T a, T b) throws NumberException {
        return operation.div(a, b);
    }
}
