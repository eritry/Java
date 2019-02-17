package expression;

public class Add extends Operator implements TripleExpression {

    public Add(TripleExpression f, TripleExpression s) {
        super(f, s);
    }

    protected int operator(int a, int b) {
        return a + b;
    }
}
