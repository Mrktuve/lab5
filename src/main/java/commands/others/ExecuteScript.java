package commands.others;

import collection.ScriptParser;
import collection.WorkerCollection;
import commands.Command;
import model.Worker;

import java.util.Map;

public class ExecuteScript extends Command {

    private final WorkerCollection collection;
    private final Map<String, Command> commandRegistry;

    public ExecuteScript(WorkerCollection collection, Map<String, Command> commandRegistry) {
        this.collection = collection;
        this.commandRegistry = commandRegistry;

        // Инициализация парсера при первом создании команды
        if (ScriptParser.getInstance() == null) {
            try {
                ScriptParser.initialize();
                ScriptParser.getInstance().setDependencies(collection, commandRegistry);
            } catch (IllegalStateException ignored) {
                // Уже инициализирован в другом месте
            }
        }
    }

    @Override
    public void execute() {
        System.out.println();
        System.out.println("Не поддерживается без аргумента");
        System.out.println("Использование: execute_script {file_name}");
        System.out.println();
    }

    @Override
    public void execute(String fileName) {
        System.out.println();
        System.out.println("Чтение команд из файла: " + fileName);
        System.out.println();

        // Установка зависимостей (на случай, если не были установлены)
        ScriptParser.getInstance().setDependencies(collection, commandRegistry);

        // Выполнение скрипта
        ScriptParser.getInstance().executeScript(fileName);

        // Сброс после выполнения
        ScriptParser.getInstance().reset();
    }

    @Override
    public void execute(String value1, Worker value2) {
        execute(value1);
    }

    @Override
    public void execute(Worker value1) {
        System.out.println("Команда не поддерживает Worker без имени файла");
    }

    @Override
    public String commandInfo() {
        return "считать и исполнить скрипт из указанного файла";
    }
}