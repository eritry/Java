package expression.exceptions;
import expression.TripleExpression;


public class CheckedMultiply extends Operator implements TripleExpression {

    public CheckedMultiply (TripleExpression first, TripleExpression second) {
        super(first, second);
    }

    @Override
    protected int operator(int x1, int y1) throws Exception {
        int x = x1, y = y1;
        if (x1 > y1) {
            x = y1;
            y = x1;
        }

        if (x < 0) {
            if (y < 0 && x < Integer.MAX_VALUE / y) {
                throw new Exception("Overflowing: " + Integer.toString(x1) + "*" + Integer.toString(y1));
            }
            if (y > 0 && x < Integer.MIN_VALUE / y) {
                throw new Exception("Overflowing: " + Integer.toString(x1) + "*" + Integer.toString(y1));
            }
        }
        if (x > 0) {
            if (x > Integer.MAX_VALUE / y) {
                throw new Exception("Overflowing: " + Integer.toString(x1) + "*" + Integer.toString(y1));
            }
        }

        return x1 * y1;
    }
}
