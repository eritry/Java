package expression.expressions;
import expression.exceptions.ParseException;
import expression.generic.NumberException;
import expression.operations.Operation;

public class Add<T> extends Operator<T> implements TripleExpression<T> {

    public Add(TripleExpression<T> f, TripleExpression<T> s, Operation o) {
        super(f, s, o);
    }

    protected T operator(T a, T b) throws NumberException {
        return operation.add(a, b);
    }
}
