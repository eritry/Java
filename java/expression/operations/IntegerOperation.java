package expression.operations;

import expression.generic.NumberException;

public class IntegerOperation implements Operation<Integer> {
    private final boolean needCheck;

    public Integer parseNumber(final String number) throws NumberException {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            throw new NumberException("Wrong Integer number format: " + number);
        }
    }
    public IntegerOperation(final boolean checkOverflow) {
        needCheck = checkOverflow;
    }

    private void checkZero(final Integer y, final String message) throws NumberException {
        if (y == 0) {
            throw new NumberException(message);
        }
    }

    private void checkAdd(final Integer x, final Integer y) throws NumberException {
        if (x > 0 && y > Integer.MAX_VALUE - x) {
            throw new NumberException("Overflowing: " + Integer.toString(x) + "+" + Integer.toString(y));
        }

        if (x < 0 && y < Integer.MIN_VALUE - x) {
            throw new NumberException("Overflowing: " + Integer.toString(x) + "+" + Integer.toString(y));
        }
    }

    private void checkDivide(final Integer x, final Integer y) throws NumberException {
        if (x == Integer.MIN_VALUE && y == -1) {
            throw new NumberException("Overflowing: " + Integer.toString(x) + "/" + Integer.toString(y));
        }
    }

    private void checkMultiply(final Integer x1, final Integer y1) throws NumberException {
        Integer x = x1, y = y1;
        if (x1 > y1) {
            x = y1;
            y = x1;
        }

        if (x < 0) {
            if (y < 0 && x < Integer.MAX_VALUE / y) {
                throw new NumberException("Overflowing: " + Integer.toString(x1) + "*" + Integer.toString(y1));
            }
            if (y > 0 && x < Integer.MIN_VALUE / y) {
                throw new NumberException("Overflowing: " + Integer.toString(x1) + "*" + Integer.toString(y1));
            }
        }
        if (x > 0) {
            if (x > Integer.MAX_VALUE / y) {
                throw new NumberException("Overflowing: " + Integer.toString(x1) + "*" + Integer.toString(y1));
            }
        }
    }

    private void checkSubtract(final Integer x, final Integer y) throws NumberException {
        if (y < 0 && x > Integer.MAX_VALUE + y) {
            throw new NumberException("Overflowing: " + Integer.toString(x) + "+" + Integer.toString(y));
        }

        if (y > 0 && x < Integer.MIN_VALUE + y) {
            throw new NumberException("Overflowing: " + Integer.toString(x) + "+" + Integer.toString(y));
        }
    }

    private void checkNegate(final Integer x) throws NumberException {
        if (x == Integer.MIN_VALUE) {
            throw new NumberException("Overflowing: " + "-" + Integer.toString(x));
        }
    }

    private void checkAbs(final Integer x) throws NumberException {
        if (x == Integer.MIN_VALUE) {
            throw new NumberException("Overflowing: abs(" + x + ")");
        }
    }

    public Integer add(Integer x, Integer y) throws NumberException{
        if (needCheck) {
            checkAdd(x, y);
        }
        return x + y;
    }

    public Integer abs(final Integer x) throws NumberException {
        if (needCheck) {
            checkAbs(x);
        }
        return Math.abs(x);
    }

    public Integer div(Integer x, Integer y) throws NumberException{
        checkZero(y, "Division by zero: " + x + " / " + y);
        if (needCheck) {
            checkDivide(x, y);
        }
        return x / y;
    }

    public Integer mul(Integer x, Integer y) throws NumberException{
        if (needCheck) {
            checkMultiply(x, y);
        }
        return x * y;
    }
    public Integer sub(Integer x, Integer y) throws NumberException{
        if (needCheck) {
            checkSubtract(x, y);
        }
        return x - y;
    }
    public Integer neg(Integer x) throws NumberException{
        if (needCheck) {
            checkNegate(x);
        }
        return -x;
    }

    public Integer mod(final Integer x, final Integer y) throws NumberException {
        checkZero(y, "Division by zero: " + x + " % " + y);
        return x % y;
    }

    public Integer sqr(final Integer x) throws NumberException {
        if (needCheck) {
            checkMultiply(x, x);
        }
        return x * x;
    }
}
