package queue;

public class ArrayQueueModule {
    private static int size = 0, l = 0;
    private static Object[] e = new Object[10];

    private static void copy(Object[] ne, int bfrom, int bto, int len ) {
        System.arraycopy(e, bfrom, ne, bto, len);
    }
    //pre: true
    private static int getR() {
        return (l + size + e.length) % e.length;
    }
    //post: R = (l + size - 1 + e.length) % e.length

    //pre: l >= 0 && l < e.length - 1
    private static void incL() {
        l = (l + 1) % e.length;
    }
    //post: l' = (l + 1) % e.length, 0 <= l' < e.length

    //pre: l > 0 && l < e.length
    private static void decL() {
        l = (l - 1 + e.length) % e.length;
    }
    //post: l' = (l - 1) mod e.length, 0 <= l' < e.length

    //pre: size > 0
    private static int decR() {
        int r = getR();
        return (r - 1 + e.length) % e.length;
    }
    //post: R = (r - 1) mod e.length

    //pre: true
    private static void ensureCapacity(int s) {
        if (s < e.length) {
            return;
        }

        Object[] ne = new Object[s * 2];

        if (l >= getR()) {
            copy(ne, l, 0, s - l);
            copy(ne, 0, s - l, getR());
        } else {
            copy(ne, l, 0, s);
        }
        e = ne;
        l = 0;
    }
    //post: (elements in e, l, r immutable) or (e'.length = e.length * 2 && l = 0 && r = size && size' = size)

    //pre: true
    public static void enqueue(Object el) {
        ensureCapacity(size + 1);
        int r = getR();
        size++;
        e[r] = el;
    }//post: size' = size + 1 && e' = e for all i in [0..size]
    //&& e[r] = el && (l' = l or l' = 0) && (r' = incR())

    //pre: size > 0
    public static Object element()  {
        assert size > 0 : "Size is less than zero";
        return e[l];
    }
    //post: R = e[l] && e, r, l, size is immutable

    //pre: true
    public static void push(Object el) {
        ensureCapacity(size + 1);
        size++;
        decL();
        e[l] = el;
    }
    //post: size' = size + 1 && e' = e for all i in [0..size]
    //&& e[decL] = el && (l' = incL()) && (r' = size')

    //pre: size > 0
    public static Object dequeue() {
        assert size > 0 : "Size is less than zero";

        Object result = e[l];
        e[l] = null;
        incL();
        size--;

        return result;
    }
    //post: size' = size - 1 && e is immutable for all i in [0..size'] && e[size] = null
    //&& R = e[l] && r = r' && l' = incL()

    //pre: size > 0
    public static Object peek() {
        assert size > 0 : "Size is less than zero";
        return e[(getR() + e.length - 1) % e.length];
    }
    //post: e, l, size is immutable && R = e[decR()]

    //pre: size > 0
    public static Object remove() {
        assert size > 0 : "Size is less than zero";

        Object result = e[decR()];
        e[decR()] = null;
        size--;

        return result;
    }
    //post: size' = size - 1 && e is immutable for all i in [0..size'] && e[size] = null
    //&& R = e[decR()] && r = decR() && l' = l

    //pre: true
    public static int size() {
        return size;
    }
    //post: R = size && e, size, l, r is immutable

    //pre: true
    public static boolean isEmpty() {
        return size == 0;
    }
    //post: R = (size == 0) && e, size, l, r is immutable

    //pre: true
    public static void clear() {
        Object[] ne = new Object[10];
        e = ne;
        size = 0;
        l = 0;
    }
    //post: size' = 0, l' = 0, r' = 0, e[0] = e[1] = .. = e[9] = null
    //&& e is empty

    public static class Test {
        public static void add() {
            for (int i = 0; i < 10; i++) {
                ArrayQueueModule.push(i);
            }
        }

        public static void get() {
            while(!ArrayQueueModule.isEmpty()) {
                System.out.println(
                        ArrayQueueModule.size() + " " +
                                ArrayQueueModule.element() + " " +
                                ArrayQueueModule.peek() + " " +
                                ArrayQueueModule.dequeue()
                );
            }
        }
    }
}
