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
        List<Worker> list = new ArrayList<>(collection.getQueue()); // создаем список, в который передаем очередь работников
        list.sort(Collections.reverseOrder()); // делаем сортировку наоборот в  очереди
        for (Worker w : list) System.out.println(w); //выводим каждого работника по одному из списка
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