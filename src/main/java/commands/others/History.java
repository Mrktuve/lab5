package commands.others;

import commands.Command;
import model.Worker;
import java.util.Deque;

public class History extends Command {
    private final Deque<String> history;

    public History(Deque<String> history) {
        this.history = history;
    }

    @Override public void execute() {
        for (String h : history) System.out.println(h);
    }

    @Override public void execute(String arg) {

    }
    @Override public void execute(String arg, Worker worker) {

    }
    @Override public void execute(Worker worker) {

    }
    @Override public String commandInfo() {
        return "последние 11 команды";
    }
}