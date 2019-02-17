package expression.exceptions;
import expression.TripleExpression;


public class CheckedNegate extends UnOperator implements TripleExpression
{
    public CheckedNegate(TripleExpression first) {
        super(first);
    }

    @Override
    protected int operator(int val) throws Exception {
        if (val == Integer.MIN_VALUE) {
            throw new Exception("Overflowing: " + "-" + Integer.toString(val));
        }
        return -val;
    }
}