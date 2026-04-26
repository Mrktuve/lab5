package commands.others;

import commands.Command;
import collection.WorkerCollection;
import model.Worker;

public class Show extends Command {
    private final WorkerCollection collection;

    public Show(WorkerCollection collection) {
        this.collection = collection;
    }

    @Override public void execute() {
        for (Worker w : collection.getQueue()) {
            System.out.println(w);
        }
    }

    @Override public void execute(String arg) {

    }
    @Override public void execute(String arg, Worker worker) {

    }
    @Override public void execute(Worker worker) {

    }
    @Override public String commandInfo() {
        return "вывести все элементы";
    }
}