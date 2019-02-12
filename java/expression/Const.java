package expression;

public class Const implements AllExpressions {
    private double value;

    public Const(int val) {
        value = val;
    }

    public Const(double val) {
        value = val;
    }

    public int evaluate(int val) {
        return (int) value;
    }

    public double evaluate(double val) {
        return value;
    }

    public int evaluate(int x, int y, int z) {
        return (int)value;
    }
}
