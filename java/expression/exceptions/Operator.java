package expression.exceptions;
import expression.TripleExpression;

public abstract class Operator implements TripleExpression {
    private TripleExpression f, s;

    protected Operator (TripleExpression first, TripleExpression second) {
        f = first;
        s = second;
    }

    public int evaluate(int x, int y, int z) throws Exception {
        return operator(f.evaluate(x, y, z), s.evaluate(x, y, z));
    }

    abstract protected int operator(int x, int y) throws Exception;

}
