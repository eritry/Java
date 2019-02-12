package queue;

import java.util.function.Function;
import java.util.function.Predicate;

public abstract class AbstractQueue {
    protected int size = 0;

    public void enqueue(Object el) {
        enqueueImpl(el);
        size++;
    }

    protected abstract void enqueueImpl(Object value);

    public Object element() {
        assert size > 0 : "Size is less than zero";
        return elementImpl();
    }

    protected abstract Object elementImpl();

    public Object dequeue() {
        assert size > 0 : "Size is less than zero";
        size--;
        return dequeueImpl();
    }

    protected abstract Object dequeueImpl();

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        clearImpl();
        size = 0;
    }

    protected abstract void clearImpl();

    protected abstract Queue copyQueue();

    public Queue filter(Predicate<Object> predicate) {
        Queue copyQ = copyQueue();
        int s = copyQ.size();
        for (int i = 0; i < s; i++) {
            Object copyE = copyQ.dequeue();
            if (predicate.test(copyE)) {
                copyQ.enqueue(copyE);
            }
        }
        return copyQ;
    }

    public Queue map(Function<Object, Object> function) {
        Queue copyQ = copyQueue();
        int s = copyQ.size();
        for (int i = 0; i < s; i++) {
            Object copyE = copyQ.dequeue();
            copyQ.enqueue(function.apply(copyE));
        }
        return copyQ;
    }
}
