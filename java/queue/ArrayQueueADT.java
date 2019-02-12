package queue;

public class ArrayQueueADT {
    private int size = 0, l = 0;
    private Object[] e = new Object[10];

    private static void copy(ArrayQueueADT queue, Object[] ne, int bfrom, int bto, int len ) {
        System.arraycopy(queue.e, bfrom, ne, bto, len);
    }

    //pre: true
    private static int getR(ArrayQueueADT queue) {
        return (queue.l + queue.size + queue.e.length) % queue.e.length;
    }
    //post: R = (l + size - 1 + e.length) % e.length

    //pre: l >= 0 && l < e.length - 1
    private static void incL(ArrayQueueADT queue) {
        queue.l = (queue.l + 1) % queue.e.length;
    }
    //post: l' = (l + 1) % e.length, 0 <= l' < e.length

    //pre: l > 0 && l < e.length
    private static void decL(ArrayQueueADT queue) {
        queue.l = (queue.l - 1 + queue.e.length) % queue.e.length;
    }
    //post: l' = (l - 1) mod e.length, 0 <= l' < e.length

    //pre: size > 0
    private static int decR(ArrayQueueADT queue) {
        int r = getR(queue);
        return (r - 1 + queue.e.length) % queue.e.length;
    }
    //post: R = (r - 1) mod e.length


    //pre: true
    private static void ensureCapacity(ArrayQueueADT queue, int s) {
        if (s < queue.e.length) {
            return;
        }

        Object[] ne = new Object[s * 2];

        if (queue.l >= getR(queue)) {
            copy(queue, ne, queue.l, 0, s - queue.l);
            copy(queue, ne, 0, s - queue.l, getR(queue));
        } else {
            copy(queue, ne, queue.l, 0, s);
        }
        queue.e = ne;
        queue.l = 0;
    }
    //post: (elements in e, l, r immutable) or (e'.length = e.length * 2 && l = 0 && r = size && size' = size)

    //pre: true
    public static void enqueue(ArrayQueueADT queue, Object el) {
        ensureCapacity(queue, queue.size + 1);
        int r = getR(queue);
        queue.size++;
        queue.e[r] = el;
    }
    //post: size' = size + 1 && e' = e for all i in [0..size]
    //&& e[r] = el && (l' = l or l' = 0) && (r' = incR())

    //pre: size > 0
    public static Object element(ArrayQueueADT queue)  {
        assert queue.size > 0 : "Size is less than zero";
        return queue.e[queue.l];
    }
    //post: R = e[l] && e, r, l, size is immutable

    //pre: true
    public static void push(ArrayQueueADT queue, Object el) {
        ensureCapacity(queue, queue.size + 1);
        queue.size++;
        decL(queue);
        queue.e[queue.l] = el;
    }
    //post: size' = size + 1 && e' = e for all i in [0..size]
    //&& e[decL] = el && (l' = incL()) && (r' = size')

    //pre: size > 0
    public static Object dequeue(ArrayQueueADT queue) {
        assert queue.size > 0 : "Size is less than zero";

        Object result = queue.e[queue.l];
        queue.e[queue.l] = null;
        incL(queue);
        queue.size--;

        return result;
    }
    //post: size' = size - 1 && e is immutable for all i in [0..size'] && e[size] = null
    //&& R = e[l] && r = r' && l' = incL()

    //pre: size > 0
    public static Object peek(ArrayQueueADT queue) {
        assert queue.size > 0 : "Size is less than zero";
        return queue.e[(getR(queue) + queue.e.length - 1) % queue.e.length];
    }
    //post: e, l, size is immutable && R = e[decR()]

    //pre: size > 0
    public static Object remove(ArrayQueueADT queue) {
        assert queue.size > 0 : "Size is less than zero";

        Object result = queue.e[decR(queue)];
        queue.e[decR(queue)] = null;
        queue.size--;

        return result;
    }
    //post: size' = size - 1 && e is immutable for all i in [0..size'] && e[size] = null
    //&& R = e[decR()] && r = decR() && l' = l

    //pre: true
    public static int size(ArrayQueueADT queue) {
        return queue.size;
    }
    //post: R = size && e, size, l, r is immutable

    //pre: true
    public static boolean isEmpty(ArrayQueueADT queue) {
        return queue.size == 0;
    }
    //post: R = (size == 0) && e, size, l, r is immutable

    //pre: true
    public static void clear(ArrayQueueADT queue) {
        Object[] ne = new Object[10];
        queue.e = ne;
        queue.size = 0;
        queue.l = 0;
    }
    //post: size' = 0, l' = 0, r' = 0, e[0] = e[1] = .. = e[9] = null
    //&& e is empty
    public static class Test {
        ArrayQueueADT queue = new ArrayQueueADT();

        public void add() {
            for (int i = 0; i < 10; i++) {
                ArrayQueueADT.push(queue, i);
            }
        }

        public void get() {
            while(!ArrayQueueADT.isEmpty(queue)) {
                System.out.println(
                        ArrayQueueADT.size(queue) + " " +
                                ArrayQueueADT.element(queue) + " " +
                                ArrayQueueADT.peek(queue) + " " +
                                ArrayQueueADT.dequeue(queue)
                );
            }
        }
    }
}
