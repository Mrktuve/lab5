package commands.others;

import commands.Command;
import model.Worker;

public class Exit extends Command {
    private boolean shouldExit = false;

    @Override public void execute() {
        shouldExit = true;
    }

    @Override public void execute(String arg) {

    }
    @Override public void execute(String arg, Worker worker) {

    }
    @Override public void execute(Worker worker) {

    }
    @Override public String commandInfo() {
        return "завершить программу (без сохранения)";
    }

    public boolean shouldExit() {
        return shouldExit;
    }
}