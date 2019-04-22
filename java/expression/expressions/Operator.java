package expression.expressions;

import expression.exceptions.ParseException;
import expression.generic.NumberException;
import expression.operations.Operation;

public abstract class Operator<T> implements TripleExpression<T> {
    private final TripleExpression<T> f, s;
    protected final Operation<T> operation;

    Operator(TripleExpression<T> first, TripleExpression<T> second, final Operation op) {
        f = first;
        s = second;
        operation = op;
    }

    abstract protected T operator(T x, T y) throws ParseException, NumberException;

    public T evaluate(T x, T y, T z) throws NumberException, ParseException {
        return operator(f.evaluate(x, y, z), s.evaluate(x, y, z));
    }

}
