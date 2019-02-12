package expression;

public abstract class Operator implements AllExpressions {
    private AllExpressions f, s;

    protected Operator (AllExpressions first, AllExpressions second) {
        f = first;
        s = second;
    }

    public int evaluate(int val) {
        return operator(f.evaluate(val), s.evaluate(val));
    }

    public double evaluate(double val) {
        return operator(f.evaluate(val), s.evaluate(val));
    }

    public int evaluate(int x, int y, int z) {
        return operator(f.evaluate(x, y, z), s.evaluate(x, y, z));
    }

    abstract protected int operator(int x, int y);

    abstract protected double operator(double x, double y);

}
