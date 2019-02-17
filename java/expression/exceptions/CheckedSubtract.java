package expression.exceptions;
import expression.TripleExpression;

public class CheckedSubtract extends Operator implements TripleExpression {

    public CheckedSubtract(TripleExpression first, TripleExpression second) {
        super(first, second);
    }

    @Override
    protected int operator(int x, int y) throws Exception{
        if (y < 0 && x > Integer.MAX_VALUE + y) {
            throw new Exception("Overflowing: " + Integer.toString(x) + "+" + Integer.toString(y));
        }

        if (y > 0 && x < Integer.MIN_VALUE + y) {
            throw new Exception("Overflowing: " + Integer.toString(x) + "+" + Integer.toString(y));
        }

        return x - y;
    }
}
