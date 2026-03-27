package commands.others;

import commands.Command;
import collection.WorkerCollection;
import collection.XmlManager;
import model.Worker;

public class Save extends Command {
    private final WorkerCollection collection;
    private final XmlManager xml;
    private final String fileName;

    public Save(WorkerCollection collection, XmlManager xml, String fileName) {
        this.collection = collection;
        this.xml = xml;
        this.fileName = fileName;
    }

    @Override public void execute() {
        xml.save(fileName, collection.getQueue());
        System.out.println("Коллекция сохранена.");
    }

    @Override public void execute(String arg) {

    }
    @Override public void execute(String arg, Worker worker) {

    }
    @Override public void execute(Worker worker) {

    }
    @Override public String commandInfo() {
        return "сохранить коллекцию";
    }
}