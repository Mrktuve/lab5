package commands.others;

import commands.Command;
import collection.WorkerCollection;
import model.Worker;

public class Clear extends Command {
    private final WorkerCollection collection;

    public Clear(WorkerCollection collection) {
        this.collection = collection;
    }

    @Override public void execute() {
        collection.clear();
    }

    @Override public void execute(String arg) {

    }
    @Override public void execute(String arg, Worker worker) {

    }
    @Override public void execute(Worker worker) {

    }
    @Override public String commandInfo() {
        return "очистить коллекцию";
    }
}