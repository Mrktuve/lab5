package commands.others;

import commands.Command;
import collection.WorkerCollection;
import model.Worker;

import java.util.Scanner;

public class UpdateId extends Command {
    private final WorkerCollection collection;
    private final Scanner scanner;

    public UpdateId(WorkerCollection collection, Scanner scanner) {
        this.collection = collection;
        this.scanner = scanner;
    }

    @Override public void execute() {
        System.out.println("Нужен id: update_id {id}");
    }

    @Override public void execute(String arg) {
        if (arg == null) {
            System.out.println("Нужен id");
            return;
        }
        try {
            int id = Integer.parseInt(arg);
            Worker old = collection.findById(id);
            if (old == null) {
                System.out.println("Элемент с id " + id + " не найден");
                return;
            }
            collection.removeById(id);
            Worker updated = new Add(collection, scanner).readWorker(id);
            collection.add(updated);
            System.out.println("Элемент с id " + id + " обновлён");
        } catch (NumberFormatException e) {
            System.out.println("id должен быть числом");
        }
    }

    @Override public void execute(String arg, Worker worker) {

    }
    @Override public void execute(Worker worker) {
        System.out.println("Команда требует id");
    }
    @Override public String commandInfo() {
        return "обновить элемент по id";
    }
}