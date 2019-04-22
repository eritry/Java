package expression.operations;

import expression.generic.NumberException;

public class LongOperation implements Operation<Long> {
    public Long parseNumber(final String number) throws NumberException {
        try {
            return Long.parseLong(number);
        } catch (NumberFormatException e) {
            throw new NumberException("Wrong long number format: " + number);
        }
    }

    private void checkZero(final Long y, final String message) throws NumberException {
        if (y == 0) {
            throw new NumberException(message);
        }
    }

    public Long add(final Long x, final Long y) {
        return x + y;
    }

    public Long sub(final Long x, final Long y) {
        return x - y;
    }

    public Long mul(final Long x, final Long y) {
        return x * y;
    }

    public Long div(final Long x, final Long y) throws NumberException {
        checkZero(y, "Division by zero:" + x + " / " + y);
        return x / y;
    }

    public Long mod(final Long x, final Long y) throws NumberException {
        checkZero(y, "Division by zero: " + x + "%" + y);
        return x % y;
    }

    public Long neg(final Long x) {
        return -x;
    }

    public Long abs(final Long x) {
        return (Math.abs(x));
    }

    public Long sqr(final Long x) {
        return x * x;
    }
}

