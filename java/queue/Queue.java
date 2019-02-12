package queue;
import java.util.function.Predicate;
import java.util.function.Function;

public interface Queue {

    //pre: true
    void enqueue(Object el);
    //post: size' = size + 1 && e' = e for all i in [0..size]
    //&& end of queue = el

    //pre: size > 0
    Object element();
    //post: R = first in queue && size, queue is immutable

    //pre: size > 0
    public Object dequeue();
    //post: size' = size - 1 &&
    //&& R = first of queue && new first = next of first && queue without first element not changed

    //pre: true
    public int size();
    //post: R = size && size, queue is immutable

    //pre: true
    public boolean isEmpty();
    //post: R = (size == 0) && size, queue is immutable

    //pre: true
    public void clear();
    //post: size' = 0 && queue is empty

    //pre: queue exists
    Queue filter(Predicate<Object> predicate);
    //post: for all element in queue': if predicate(element) = true && size' >= 0 && size' <= size
    //&& order in queue' not changed && R = queue'

    //pre: queue exists
    Queue map(Function<Object, Object> function);
    //post: for all element' in queue': element' = function(element) && size' = size >= 0
    //&& order in queue' not changed && R = queue'
}
