package expression.expressions;

import expression.exceptions.ParseException;
import expression.generic.NumberException;
import expression.operations.Operation;

public class Multiply<T> extends Operator<T> implements TripleExpression<T> {

    public Multiply(TripleExpression<T> f, TripleExpression<T> s, Operation o) {
        super(f, s, o);
    }

    protected T operator(T a, T b) throws NumberException {
        return operation.mul(a, b);
    }

}
