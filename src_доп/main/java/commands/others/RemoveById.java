package commands.others;

import commands.Command;
import collection.WorkerCollection;
import model.Worker;

public class RemoveById extends Command {
    private final WorkerCollection collection;

    public RemoveById(WorkerCollection collection) {
        this.collection = collection;
    }

    @Override public void execute() {
        System.out.println("Нужен id: remove_by_id {id}");
    }

    @Override public void execute(String arg) {
        if (arg == null) {
            System.out.println("Нужен id");
            return;
        }
        try {
            int id = Integer.parseInt(arg);
            if (collection.removeById(id)) {
                System.out.println("Удалено");
            } else {
                System.out.println("Элемент не найден");
            }
        } catch (NumberFormatException e) {
            System.out.println("id должен быть числом");
        }
    }

    @Override public void execute(String arg, Worker worker) {

    }
    @Override public void execute(Worker worker) {
        System.out.println("Команда требует id в качестве аргумента");
    }
    @Override public String commandInfo() {
        return "удалить элемент по id";
    }
}