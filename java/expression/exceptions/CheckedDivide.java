package expression.exceptions;
import expression.TripleExpression;

public class CheckedDivide extends Operator implements TripleExpression{
    public CheckedDivide (TripleExpression first, TripleExpression second) {
        super(first, second);
    }

    @Override
    protected int operator(int x, int y) throws Exception {
        if (y == 0) {
            throw new Exception("Division by zero: " + Integer.toString(x) + "/0");
        }
        if (x == Integer.MIN_VALUE && y == -1) {
            throw new Exception("Overflowing: " + Integer.toString(x) + "/" + Integer.toString(y));
        }
        return x / y;
    }
}