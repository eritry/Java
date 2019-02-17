package expression.exceptions;
import expression.TripleExpression;

public class CheckedAdd extends Operator implements TripleExpression {

    public CheckedAdd(TripleExpression first, TripleExpression second) {
        super(first, second);
    }

    @Override
    protected int operator(int x, int y) throws Exception{
        if (x > 0 && y > Integer.MAX_VALUE - x) {
            throw new Exception("Overflowing: " + Integer.toString(x) + "+" + Integer.toString(y));
        }

        if (x < 0 && y < Integer.MIN_VALUE - x) {
            throw new Exception("Overflowing: " + Integer.toString(x) + "+" + Integer.toString(y));
        }

        return x + y;
    }
}
