package commands.others;

import commands.Command;
import collection.WorkerCollection;
import model.Worker;

import java.util.*;

public class PrintDescending extends Command {
    private final WorkerCollection collection;

    public PrintDescending(WorkerCollection collection) {
        this.collection = collection;
    }

    @Override public void execute() {
        List<Worker> list = new ArrayList<>(collection.getQueue());
        list.sort(Collections.reverseOrder());
        for (Worker w : list) System.out.println(w);
    }

    @Override public void execute(String arg) {

    }
    @Override public void execute(String arg, Worker worker) {

    }
    @Override public void execute(Worker worker) {

    }
    @Override public String commandInfo() {
        return "вывести элементы в порядке убывания";
    }
}