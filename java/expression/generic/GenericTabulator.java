package expression.generic;
import expression.exceptions.*;
import expression.expressions.TripleExpression;
import expression.operations.*;

import java.util.Map;


public class GenericTabulator implements Tabulator {
    private int size(int a, int b) {
        return b - a + 1;
    }

    private Map<String, Operation<?>> mode = Map.of(
            "i", new IntegerOperation(true),
            "d", new DoubleOperation(),
            "bi", new BigIntegerOperation(),
            "u", new IntegerOperation(false),
            "f", new FloatOperation(),
            "b", new ByteOperation(),
            "l", new LongOperation(),
            "s", new ShortOperation()
    );

    @Override
    public Object[][][] tabulate(String m, String expression, int x1, int x2, int y1, int y2, int z1, int z2) {
        return tab(expression, x1, x2, y1, y2, z1, z2, mode.get(m));
    }

    private <T> Object[][][] tab(String expression, int x1, int x2, int y1, int y2, int z1, int z2, Operation<T> op) {
        Object[][][] res = new Object[size(x1, x2)][size(y1, y2)][size(z1, z2)];
        try {
            TripleExpression<T> exp = new ExpressionParser<>(op).parse(expression);

            for (int i = 0; i < size(x1, x2); i++) {
                for (int j = 0; j < size(y1, y2); j++)
                    for (int k = 0; k < size(z1, z2); k++)
                        try {
                            res[i][j][k] = exp.evaluate(op.parseNumber(Integer.toString(x1 + i)),
                                                        op.parseNumber(Integer.toString(y1 + j)),
                                                        op.parseNumber(Integer.toString(z1 + k)));
                        } catch (NumberException e) {
                            res[i][j][k] = null;
                        }
            }
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        return res;
    }
}