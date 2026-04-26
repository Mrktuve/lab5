package commands.others;

import commands.Command;
import collection.WorkerCollection;
import model.Worker;

import java.util.*;

public class AddIfMax extends Command {
    private final WorkerCollection collection;
    private final Scanner scanner;

    public AddIfMax(WorkerCollection collection, Scanner scanner) {
        this.collection = collection;
        this.scanner = scanner;
    }

    @Override public void execute() {
        Worker newWorker = new Add(collection, scanner).readWorker(-1);
        if (collection.getQueue().isEmpty()) {
            collection.add(newWorker);
            System.out.println("Добавлен (коллекция была пуста)");
            return;
        }
        Worker max = Collections.max(collection.getQueue());
        if (newWorker.compareTo(max) > 0) {
            collection.add(newWorker);
            System.out.println("Добавлен (больше максимального)");
        } else {
            System.out.println("Не добавлен (не больше максимального)");
        }
    }

    @Override public void execute(String arg) {
        System.out.println("Команда требует интерактивного ввода");
    }
    @Override public void execute(String arg, Worker worker) {

    }
    @Override public void execute(Worker worker) {
        if (collection.getQueue().isEmpty() || worker.compareTo(Collections.max(collection.getQueue())) > 0) {
            collection.add(worker);
            System.out.println("Добавлен (больше максимального)");
        } else {
            System.out.println("Не добавлен (не больше максимального)");
        }
    }

    @Override public String commandInfo() {
        return "добавить, если больше максимального";
    }
}