package expression.expressions;
import expression.exceptions.ParseException;
import expression.generic.NumberException;
import expression.operations.Operation;

public abstract class UnOperator<T> implements TripleExpression<T> {
    private TripleExpression<T> value;
    protected Operation<T> operation;

    protected UnOperator(TripleExpression<T> value, Operation operation) {
        this.value = value;
        this.operation = operation;
    }

    abstract protected T operator(T val) throws ParseException, NumberException;

    public T evaluate(T x, T y, T z) throws ParseException, NumberException {
        return operator(value.evaluate(x, y, z));
    }
}
