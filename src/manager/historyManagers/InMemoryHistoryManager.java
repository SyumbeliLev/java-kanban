package manager.historyManagers;

import model.Task;

import java.util.*;


public class InMemoryHistoryManager implements HistoryManager {

    private final CustomLinkedList<Task> history = new CustomLinkedList<>();
    private final Map<Integer, Node> historyMap = new HashMap<>();

    @Override
    public List<Task> getHistory() {
        return history.getTasks();
    }


    @Override
    public void remove(int id) {
        history.removeNode(historyMap.remove(id));
    }

    @Override
    public void add(Task task) {
        if (task != null) {
            remove(task.getId());
            historyMap.put(task.getId(), history.linkLast(task));
        }
    }


    private class CustomLinkedList<T> {
        private Node<T> head;
        private Node<T> tail;
        private int size = 0;


        private Node<T> linkLast(T element) {
            final Node<T> oldTail = tail;
            final Node<T> newNode = new Node<>(oldTail, element, null);
            tail = newNode;
            if (oldTail == null) head = newNode;
            else oldTail.next = newNode;
            size++;
            return newNode;
        }


        private List<T> getTasks() {
            List<T> returnList = new ArrayList<>();
            Node<T> bufferHead = head;


            while (bufferHead != null) {
                returnList.add(bufferHead.data);
                bufferHead = bufferHead.next;
            }
            return returnList;
        }

        private void removeNode(Node<T> node) {
            if (node == null) {
                return;
            }
            if (node == head) {
                head = node.next;
                if (this.size > 1) head.prev = null;
            }
            if (node == tail) {
                tail = node.prev;
                if (this.size > 1) tail.next = null;

            }
            if (node.prev != null) {
                node.prev.next = node.next;
            }
            if (node.next != null) {
                node.next.prev = node.prev;
            }
            size--;
        }
    }
}
