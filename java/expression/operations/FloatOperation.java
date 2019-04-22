package expression.operations;
import expression.generic.NumberException;

public class FloatOperation implements Operation<Float> {
    public Float parseNumber(final String number) throws NumberException {
        try {
            return Float.parseFloat(number);
        } catch (NumberFormatException e) {
            throw new NumberException("Wrong float number format: " + number);
        }
    }

    public Float add(final Float x, final Float y) {
        return x + y;
    }

    public Float sub(final Float x, final Float y) {
        return x - y;
    }

    public Float mul(final Float x, final Float y) {
        return x * y;
    }

    public Float div(final Float x, final Float y) {
        return x / y;
    }

    public Float mod(final Float x, final Float y) {
        return x % y;
    }

    public Float neg(final Float x) {
        return -x;
    }

    public Float abs(final Float x) {
        return Math.abs(x);
    }

    public Float sqr(final Float x) {
        return x * x;
    }
}
