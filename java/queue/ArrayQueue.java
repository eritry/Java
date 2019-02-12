package queue;

public class ArrayQueue extends AbstractQueue implements Queue {
    private int l = 0;
    private Object[] e = new Object[10];

    private void copy(Object[] e, Object[] ne, int bfrom, int bto, int len ) {
        System.arraycopy(e, bfrom, ne, bto, len);
    }

    private int getR() {
        return (l + size + e.length) % e.length;
    }

    private void incL() {
        l = (l + 1) % e.length;
    }

    private void decL() {
        l = (l - 1 + e.length) % e.length;
    }

    private int decR() {
        int r = getR();
        return (r - 1 + e.length) % e.length;
    }

    private void ensureCapacity(int s) {
        if (s < e.length) {
            return;
        }

        Object[] ne = new Object[s * 2];

        if (l >= getR()) {
            copy(e, ne, l, 0, s - l);
            copy(e, ne, 0, s - l, getR());
        } else {
            copy(e, ne, l, 0, s);
        }
        e = ne;
        l = 0;
    }

    public void enqueueImpl(Object el) {
        ensureCapacity(size + 1);
        int r = getR();
        e[r] = el;
    }

    protected Object elementImpl()  {
        return e[l];
    }

    protected Object dequeueImpl() {
        Object result = e[l];
        e[l] = null;
        incL();

        return result;
    }

    public void clearImpl() {
        Object[] ne = new Object[10];
        e = ne;
        size = 0;
        l = 0;
    }

    protected Queue copyQueue() {
        ArrayQueue copyQ = new ArrayQueue();
        copyQ.e = new Object[e.length];

        copy(e, copyQ.e, 0, 0, e.length);

        copyQ.size = size;
        copyQ.l = l;

        return copyQ;
    }
}
