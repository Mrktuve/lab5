package commands.others;

import commands.Command;
import collection.WorkerCollection;
import enums.Status;
import model.Worker;

import java.util.Iterator;

public class RemoveAnyByStatus extends Command {
    private final WorkerCollection collection;

    public RemoveAnyByStatus(WorkerCollection collection) {
        this.collection = collection;
    }

    @Override public void execute() {
        System.out.println("Нужен статус: remove_any_by_status {status}");
    }

    @Override public void execute(String arg) {
        if (arg == null) {
            System.out.println("Нужен статус");
            return;
        }
        try {
            Status status = Status.valueOf(arg);
            Iterator<Worker> it = collection.getQueue().iterator();
            while (it.hasNext()) {
                if (it.next().getStatus() == status) {
                    it.remove();
                    System.out.println("Удалён один элемент со статусом " + status);
                    return;
                }
            }
            System.out.println("Элемент со статусом " + status + " не найден.");
        } catch (IllegalArgumentException e) {
            System.out.println("Такого статуса нет.");
        }
    }

    @Override public void execute(String arg, Worker worker) {

    }
    @Override public void execute(Worker worker) {
        System.out.println("Команда требует статус");
    }
    @Override public String commandInfo() {
        return "удалить элемент с заданным статусом";
    }
}