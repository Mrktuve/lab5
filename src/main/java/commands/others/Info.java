package commands.others;

import commands.Command;
import collection.WorkerCollection;
import model.Worker;

public class Info extends Command {
    private final WorkerCollection collection;

    public Info(WorkerCollection collection) {
        this.collection = collection;
    }

    @Override
    public void execute() {
        System.out.println("Тип коллекции: " + collection.getQueue().getClass().getName());
        System.out.println("Дата инициализации: " + collection.getInitDate());
        System.out.println("Количество элементов: " + collection.getQueue().size());
    }

    @Override public void execute(String arg) {

    }
    @Override public void execute(String arg, Worker worker) {

    }
    @Override public void execute(Worker worker) {

    }
    @Override public String commandInfo() {
        return "инфа о коллекции";
    }
}