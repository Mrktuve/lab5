


import collection.*;
import commands.CommandManager;

public class Main {
    public static void main(String[] args) {
        String fileName = System.getenv("Worker");
        if (fileName == null) fileName = "lab5.xml";

        XmlManager xml = new XmlManager();
        WorkerCollection collection = new WorkerCollection();
        collection.getQueue().addAll(xml.load(fileName));

        System.out.println("Запущена коллекция WORKER");
        System.out.println("help — список команд");

        CommandManager manager = new CommandManager(collection, xml, fileName);
        manager.run();
    }
}

