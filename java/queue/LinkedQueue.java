package queue;

public class LinkedQueue extends AbstractQueue implements Queue {
    private Node head, tail;

    private class Node {
        private Node next;
        private Object value;

        public Node(Object el) {
            next = null;
            value = el;
        }
    }

    protected void enqueueImpl(Object el) {
        Node cur = new Node(el);
        if (size == 0) {
            tail = cur;
            head = tail;
        } else {
            tail.next = cur;
            tail = cur;
        }
    }

    protected Object elementImpl() {
        return head.value;
    }

    protected Object dequeueImpl() {
        Object res = head.value;
        if (head.next != null) {
            head = head.next;
        } else {
            head = null;
            tail = head;
        }
        return res;
    }

    protected void clearImpl() {
        tail = null;
        head = null;
    }

    protected Queue copyQueue() {
        LinkedQueue copyQ = new LinkedQueue();
        Node copy = head;

        while (copy != null) {
            copyQ.enqueue(copy.value);
            copy = copy.next;
        }

        copyQ.size = size;
        copyQ.head = head;
        copyQ.tail = tail;

        return copyQ;
    }
}
