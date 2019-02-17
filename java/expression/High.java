package expression;

public class High extends UnOperator implements TripleExpression {

    public High(TripleExpression f) {
        super(f);
    }

    protected int operator(int a) {
        if (a == 0) {
            return 0;
        }
        if (a < 0) {
            return Integer.MIN_VALUE;
        }
        int bit = 0;
        for (int i = 0; i <= 31; i++) {
            if ((a & (1 << i)) > 0) {
                bit = i;
            }
        }
        return (1 << bit);
    }
}
