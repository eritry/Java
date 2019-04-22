package expression.operations;
import expression.generic.NumberException;

public class ByteOperation implements Operation<Byte> {
    public Byte parseNumber(final String number) throws NumberException {
        try {
            return (byte)Integer.parseInt(number);
        } catch (NumberFormatException e) {
            throw new NumberException("Wrong integer number format: " + number);
        }
    }

    private void checkZero(final Byte x, final String message) throws NumberException {
        if (x == 0) {
            throw new NumberException(message);
        }
    }
    public Byte add(final Byte x, final Byte y) {
        return (byte)(x + y);
    }

    public Byte sub(final Byte x, final Byte y) {
        return (byte)(x - y);
    }

    public Byte mul(final Byte x, final Byte y) {
        return (byte)(x * y);
    }

    public Byte div(final Byte x, final Byte y) throws NumberException {
        checkZero(y, "Division by zero:" + x + " / " + y);
        return (byte)(x / y);
    }

    public Byte mod(final Byte x, final Byte y) throws NumberException {
        checkZero(y, "Division by zero: " + x + "%" + y);
        return (byte)(x % y);
    }

    public Byte neg(final Byte x) {
        return (byte)(-x);
    }

    public Byte abs(final Byte x) {
        return (byte)(Math.abs(x));
    }

    public Byte sqr(final Byte x) {
        return (byte)(x * x);
    }
}
