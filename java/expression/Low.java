package expression;

public class Low extends UnOperator implements TripleExpression {

    public Low(TripleExpression f) {
        super(f);
    }

    protected int operator(int a) {
        if (a == 0) {
            return 0;
        }
        int bit = 0;
        for (int i = 31; i >= 0; i--) {
            if (((a >> i) & 1) > 0) bit = i;
        }
        return (1 << bit);
    }
}
