package expression;

public abstract class UnOperator implements TripleExpression {
    private TripleExpression value;

    protected UnOperator(TripleExpression value) {
        this.value = value;
    }

    public int evaluate(int x, int y, int z) throws Exception {
        return operator(value.evaluate(x, y, z));
    }

    abstract protected int operator(int val) throws Exception;
}
