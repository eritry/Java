package expression.operations;

import expression.generic.NumberException;

public class ShortOperation implements Operation<Short> {

    public Short parseNumber(final String number) throws NumberException {
        try {
            return (short)Integer.parseInt(number);
        } catch (NumberFormatException e) {
            throw new NumberException("Wrong integer number format: " + number);
        }
    }

    private void checkZero(final Short y, final String message) throws NumberException {
        if (y == 0) {
            throw new NumberException(message);
        }
    }

    public Short add(final Short x, final Short y) {
        return (short)(x + y);
    }

    public Short sub(final Short x, final Short y) {
        return (short)(x - y);
    }

    public Short mul(final Short x, final Short y) {
        return (short)(x * y);
    }

    public Short div(final Short x, final Short y) throws NumberException {
        checkZero(y, "Division by zero:" + x + " / " + y);
        return (short)(x / y);
    }

    public Short mod(final Short x, final Short y) throws NumberException {
        checkZero(y, "Division by zero: " + x + "%" + y);
        return (short)(x % y);
    }

    public Short neg(final Short x) {
        return (short)(-x);
    }

    public Short abs(final Short x) {
        return (short)(Math.abs(x));
    }

    public Short sqr(final Short x) {
        return (short)(x * x);
    }
}




