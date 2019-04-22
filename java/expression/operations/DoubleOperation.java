package expression.operations;

import expression.generic.NumberException;

public class DoubleOperation implements Operation<Double> {

    public Double parseNumber(final String number) throws NumberException {
        try {
            return Double.parseDouble(number);
        } catch (NumberFormatException e) {
            throw new NumberException("Wrong double number format: " + number);
        }
    }

    public Double add(final Double x, final Double y) {
        return x + y;
    }

    public Double sub(final Double x, final Double y) {
        return x - y;
    }

    public Double mul(final Double x, final Double y) {
        return x * y;
    }

    public Double div(final Double x, final Double y) {
        return x / y;
    }

    public Double mod(final Double x, final Double y) {
        return x % y;
    }

    public Double neg(final Double x) {
        return -x;
    }

    public Double abs(final Double x) {
        return Math.abs(x);
    }

    public Double sqr(final Double x) {
        return x * x;
    }
}
