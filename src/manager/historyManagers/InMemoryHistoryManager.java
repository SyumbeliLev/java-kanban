package manager.historyManagers;

import model.Task;

import java.util.*;


public class InMemoryHistoryManager implements HistoryManager {

    private final CustomLinkedList<Task> history = new CustomLinkedList<>();
    private HashMap<Integer, Node> historyMap = new HashMap<>();

    @Override
    public List<Task> getHistory() {
        return (List<Task>) history.getTasks();
    }

    @Override
    public void remove(int id) {
        history.removeNode(historyMap.get(id));

    }

    @Override
    public void add(Task task) {
        if (task != null) {
            Node node = new Node<>(null, task, null);
            for (Map.Entry<Integer, Node> entry : historyMap.entrySet()) {
                if (entry.getKey() == task.getId()) {
                    history.removeNode(node);
                    historyMap.remove(node);
                }
            }
            historyMap.put(task.getId(), node);
            history.linkLast(task);
        }
    }


    public class CustomLinkedList<T> {

        private Node<T> head;
        private Node<T> tail;
        private int size = 0;


        public void linkLast(T element) {
            final Node<T> oldTail = tail;
            final Node<T> newNode = new Node<>(oldTail, element, null);
            tail = newNode;
            if (oldTail == null)
                head = newNode;
            else
                oldTail.next = newNode;
            size++;

        }

        public T getTasks() {
            Node<T> oldHead = head;
            List<T> updateList = new ArrayList<>();

            while (oldHead != null) {
                updateList.add(oldHead.data);
                oldHead = oldHead.next;
            }
            return (T) updateList;
        }

        public void removeNode(Node<T> node) {
            for (Node<T> x = this.head; x != null; x = x.next) {
                if (node.data.equals(x.data)) {
                    Node<T> next = x.next;
                    Node<T> prev = x.prev;
                    if (prev == null) {
                        this.head = next;
                    } else {
                        prev.next = next;
                        x.prev = null;
                    }

                    if (next == null) {
                        this.tail = prev;
                    } else {
                        next.prev = prev;
                        x.next = null;
                    }
                    x.data = null;
                    --this.size;
                }
            }
        }

        public int size() {
            return this.size;
        }
    }
}
