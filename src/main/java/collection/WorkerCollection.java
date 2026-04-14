package collection;

import model.Worker;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.PriorityQueue;

/**
 * Класс-обёртка для управления коллекцией работников.
 * <p>
 * Предоставляет удобные методы для работы с PriorityQueue&lt;Worker&gt;,
 * включая генерацию уникальных ID, поиск, удаление и фильтрацию элементов.
 * </p>
 *
 * @author Student
 * @version 1.0
 * @see model.Worker
 */

public class WorkerCollection {
    /** Очередь с приоритетами для хранения работников, отсортированных по compareTo() */
    private PriorityQueue<Worker> queue = new PriorityQueue<>();
    /** Дата инициализации коллекции */
    private LocalDate initDate = LocalDate.now();
    /** Счётчик для генерации уникальных ID */
    private int nextId = 1;

     /**
     * Генерирует следующий уникальный ID для работника.
     *
     * @return следующий доступный ID
     */
    public int generateId() {
        return nextId++;
    }

     /**
     * Добавляет работника в коллекцию.
     * <p>
     * Если у работника не задан ID (равен 0), автоматически генерирует новый.
     * </p>
     *
     * @param w работник для добавления
     */
    public void add(Worker w) {
        if (w.getId() == 0) {  // если id не задан
            w.setId(generateId());
        }
        queue.add(w);
    }
     /**
     * Обновляет счётчик nextId на основе максимального существующего ID.
     * <p>
     * Вызывается после загрузки данных из файла для предотвращения конфликтов ID.
     * </p>
     */
    public void updateNextId() {
        int maxId = queue.stream().mapToInt(Worker::getId).max().orElse(0);
        nextId = Math.max(nextId, maxId + 1);
    }

     /**
     * Возвращает внутреннюю очередь работников.
     *
     * @return PriorityQueue с работниками
     */
    public PriorityQueue<Worker> getQueue() {
        return queue;
    }

    /**
     * Возвращает дату инициализации коллекции.
     *
     * @return дата создания коллекции
     */
    public LocalDate getInitDate() {
        return initDate;
    }

     /**
     * Находит работника по его ID.
     *
     * @param id идентификатор для поиска
     * @return найденный работник или null, если не найден
     */
    public Worker findById(int id) {
        for (Worker w : queue) {
            if (w.getId() == id) return w;
        }
        return null;
    }

    /**
     * Удаляет работника по его ID.
     *
     * @param id идентификатор работника для удаления
     * @return true если работник найден и удалён, false иначе
     */
    public boolean removeById(int id) {
        Worker w = findById(id);
        if (w != null) {
            return queue.remove(w);
        }
        return false;
    }

    /**
     * Очищает коллекцию, удаляя всех работников.
     */
    public void clear() {
        queue.clear();
    }

    /**
     * Удаляет всех работников, которые меньше заданного образца.
     * <p>
     * Использует метод compareTo() для сравнения.
     * </p>
     *
     * @param sample образец для сравнения
     */
    public void removeLower(Worker sample) {
        Iterator<Worker> it = queue.iterator();
        while (it.hasNext()) {
            if (it.next().compareTo(sample) < 0) it.remove();
        }
    }
}
