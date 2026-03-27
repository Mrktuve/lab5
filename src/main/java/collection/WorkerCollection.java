package collection;

import model.Worker;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.PriorityQueue;

public class WorkerCollection {
    private PriorityQueue<Worker> queue = new PriorityQueue<>();
    private LocalDate initDate = LocalDate.now();
    private int nextId = 1;

    public int generateId() {
        return nextId++;
    }

    public void add(Worker w) {
        if (w.getId() == 0) {  // если id не задан
            w.setId(generateId());
        }
        queue.add(w);
    }

    public void updateNextId() {
        int maxId = queue.stream().mapToInt(Worker::getId).max().orElse(0);
        nextId = Math.max(nextId, maxId + 1);
    }


    public PriorityQueue<Worker> getQueue() {
        return queue;
    }

    public LocalDate getInitDate() {
        return initDate;
    }

    public Worker findById(int id) {
        for (Worker w : queue) {
            if (w.getId() == id) return w;
        }
        return null;
    }

    public boolean removeById(int id) {
        Worker w = findById(id);
        if (w != null) {
            return queue.remove(w);
        }
        return false;
    }

    public void clear() {
        queue.clear();
    }

    public void removeLower(Worker sample) {
        Iterator<Worker> it = queue.iterator();
        while (it.hasNext()) {
            if (it.next().compareTo(sample) < 0) it.remove();
        }
    }
}
