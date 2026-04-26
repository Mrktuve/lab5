package commands.others;

import commands.Command;
import collection.WorkerCollection;
import model.Worker;

public class FilterStartsWithName extends Command {
    private final WorkerCollection collection;

    public FilterStartsWithName(WorkerCollection collection) {
        this.collection = collection;
    }

    @Override public void execute() {
        System.out.println("Нужна подстрока имени.");
    }

    @Override public void execute(String arg) {
        if (arg == null) {
            System.out.println("Нужна подстрока имени.");
            return;
        }
        for (Worker w : collection.getQueue()) { // проходим по каждому работнику в коллекции
            if (w.getName() != null && w.getName().startsWith(arg)) { // если имя не равно null и начинается ли имя с того что мы ввели
                System.out.println(w);
            }
        }
    }

    @Override public void execute(String arg, Worker worker) {

    }
    @Override public void execute(Worker worker) {
        System.out.println("Команда требует подстроку");
    }
    @Override public String commandInfo() {
        return "вывести элементы с именем, начинающимся с подстроки";
    }
}