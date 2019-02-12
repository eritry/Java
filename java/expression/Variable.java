package expression;

public class Variable implements AllExpressions {
    private String name;

    public Variable(String name) {
        this.name = name;
    }

    public int evaluate(int val) {
        return val;
    }

    public double evaluate(double val) {
        return val;
    }

    public int evaluate(int x, int y, int z) {
        switch (name) {
            case "x": return x;
            case "y": return y;
            default: return z;
        }
    }
}
