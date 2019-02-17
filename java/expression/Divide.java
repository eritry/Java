package expression;

public class Divide extends Operator implements TripleExpression {

    public Divide(TripleExpression f, TripleExpression s) {
        super(f, s);
    }

    protected int operator(int a, int b) {
        return a / b;
    }

}
