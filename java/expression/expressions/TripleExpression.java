package expression.expressions;
import expression.exceptions.ParseException;
import expression.generic.NumberException;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public interface TripleExpression<T> {
    T evaluate(T x, T y, T z) throws NumberException, ParseException;
}
