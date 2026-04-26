package commands.others;

import commands.Command;
import collection.WorkerCollection;
import model.Worker;

import java.util.Scanner;

public class RemoveLower extends Command {
    private final WorkerCollection collection;
    private final Scanner scanner;

    public RemoveLower(WorkerCollection collection, Scanner scanner) {
        this.collection = collection;
        this.scanner = scanner;
    }

    @Override public void execute() {
        System.out.println("Создание элемента для remove_lower.");
        Worker w = new Add(collection, scanner).readWorker(-1);
        collection.removeLower(w);
        System.out.println("Элементы, меньшие заданного, удалены.");
    }

    @Override public void execute(String arg) {
        System.out.println("Команда требует интерактивного ввода");
    }
    @Override public void execute(String arg, Worker worker) {

    }
    @Override public void execute(Worker worker) {
        collection.removeLower(worker);
        System.out.println("Элементы удалены");
    }
    @Override public String commandInfo() {
        return "удалить элементы меньше заданного";
    }
}