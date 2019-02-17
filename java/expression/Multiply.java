package expression;

public class Multiply extends Operator implements TripleExpression {

    public Multiply(TripleExpression f, TripleExpression s) {
        super(f, s);
    }

    protected int operator(int a, int b) {
        return a * b;
    }

}
