package expression.operations;
import expression.generic.NumberException;

public interface Operation<T> {
    T parseNumber(final String number) throws NumberException;

    T add(final T x, final T y) throws NumberException;

    T sub(final T x, final T y) throws NumberException;

    T mul(final T x, final T y) throws NumberException;

    T div(final T x, final T y) throws NumberException;

    T mod(final T x, final T y) throws NumberException;

    T neg(final T x) throws NumberException;

    T abs(final T x) throws NumberException;

    T sqr(final T x) throws NumberException;

}
