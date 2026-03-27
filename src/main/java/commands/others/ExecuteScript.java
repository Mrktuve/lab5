package commands.others;

import commands.Command;
import collection.WorkerCollection;
import model.Worker;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;

public class ExecuteScript extends Command {
    private final WorkerCollection collection;
    private final String fileName;
    private final Map<String, Command> commandRegistry;

    public ExecuteScript(WorkerCollection collection, String fileName, Map<String, Command> commandRegistry) {
        this.collection = collection;
        this.fileName = fileName;
        this.commandRegistry = commandRegistry;
    }

    @Override public void execute() {
        System.out.println("Нужен путь к файлу: execute_script {file_name}");
    }

    @Override public void execute(String arg) {
        if (arg == null) {
            System.out.println("Нужен путь к файлу");
            return;
        }

        File scriptFile = new File(arg);
        if (!scriptFile.exists()) {
            System.out.println("Файл не найден: " + arg);
            return;
        }

        try (Scanner fileScanner = new Scanner(scriptFile)) {
            System.out.println("Выполняю скрипт: " + arg);
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                System.out.println("> " + line);
                String[] parts = line.split("\\s+", 2);
                String cmd = parts[0];
                String cmdArg = parts.length > 1 ? parts[1].trim() : null;

                processCommand(cmd, cmdArg);
            }
            System.out.println("Скрипт завершён");
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден: " + e.getMessage());
        }
    }

    @Override public void execute(String arg, Worker worker) {
        execute(arg);
    }

    @Override public void execute(Worker worker) {
        System.out.println("Команда требует имя файла");
    }

    @Override public String commandInfo() {
        return "выполнить скрипт из файла";
    }

    private void processCommand(String cmd, String arg) {
        switch (cmd) {
            case "show":
            case "clear":
            case "info":
            case "save":
                Command c = commandRegistry.get(cmd);
                if (c != null) c.execute();
                break;
            case "remove_by_id":
                if (arg != null) {
                    Command rbid = commandRegistry.get("remove_by_id");
                    if (rbid != null) rbid.execute(arg);
                }
                break;
            default:
                System.out.println("  → команда '" + cmd + "' пропущена (нужен интерактивный ввод)");
        }
    }
}