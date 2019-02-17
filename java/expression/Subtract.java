package expression;

public class Subtract extends Operator implements TripleExpression {

    public Subtract(TripleExpression f, TripleExpression s) {
        super(f, s);
    }

    protected int operator(int a, int b) {
        return a - b;
    }
}
