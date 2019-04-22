package expression.operations;
import expression.generic.NumberException;

import java.math.BigInteger;

public class BigIntegerOperation implements Operation<BigInteger> {
    public BigInteger parseNumber(final String number) throws NumberException {
        try {
            return new BigInteger(number);
        } catch (NumberFormatException e) {
            throw new NumberException("Wrong BigInteger number format: " + number);
        }
    }

    public BigInteger add(final BigInteger x, final BigInteger y) {
        return x.add(y);
    }

    public BigInteger sub(final BigInteger x, final BigInteger y) {
        return x.subtract(y);
    }

    public BigInteger mul(final BigInteger x, final BigInteger y) {
        return x.multiply(y);
    }

    private void checkZero(final BigInteger x, final String message) throws NumberException {
        if (x.equals(BigInteger.ZERO)) {
            throw new NumberException(message);
        }
    }

    private void checkPositive(final BigInteger y, final String message) throws NumberException {
        if (y.compareTo(BigInteger.ZERO) <= 0) {
            throw new NumberException(message);
        }
    }

    public BigInteger div(final BigInteger x, final BigInteger y) throws NumberException {
        checkZero(y, "Division by zero:" + x + " / " + y);
        return x.divide(y);
    }

    public BigInteger mod(final BigInteger x, final BigInteger y) throws NumberException {
        checkPositive(y, "Division by zero: " + x + "%" + y);
        BigInteger d = x.remainder(y);
        if (d.compareTo(BigInteger.ZERO) < 0) {
            d = d.add(y);
        }
        return d;
    }

    public BigInteger neg(final BigInteger x) {
        return x.negate();
    }

    public BigInteger abs(final BigInteger x) {
        return x.abs();
    }

    public BigInteger sqr(final BigInteger x) {
        return x.multiply(x);
    }
}

