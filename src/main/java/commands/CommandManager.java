package commands;


import collection.*;
import commands.others.*;

import java.util.*;

public class CommandManager {
    private final WorkerCollection collection;
    private final XmlManager xml;
    private final String fileName;
    //Словарь команд, создание хеш-таблицы. Ключ: имя команды, значение: значение команды
    private final Map<String, Command> commands = new HashMap<>();
    // Deque<String> - двусторонняя очередь строк т.к. добавляем команды в конец, а удаляем лишнии команды в начале
    private final Deque<String> history = new ArrayDeque<>();
    private final Scanner scanner;


    public CommandManager(WorkerCollection collection, XmlManager xml, String fileName) {
        // сохранение параметров
        this.collection = collection;
        this.xml = xml;
        this.fileName = fileName;
        // System.in стандартный ввод
        this.scanner = new Scanner(System.in);
        registerCommands();
    }

    private void registerCommands() {

        // проверяет и возвращает обьект ScriptParser(). Так же сохраняет ссылку на коллекцию и ссылку на словарь команд
        ScriptParser.getInstance().setDependencies(collection, commands);

        // регистрация команд
        commands.put("help", new Help());
        commands.put("info", new Info(collection));
        commands.put("show", new Show(collection));
        commands.put("clear", new Clear(collection));
        commands.put("save", new Save(collection, xml, fileName));
        commands.put("remove_by_id", new RemoveById(collection));
        commands.put("remove_any_by_status", new RemoveAnyByStatus(collection));
        commands.put("filter_starts_with_name", new FilterStartsWithName(collection));
        commands.put("print_descending", new PrintDescending(collection));
        commands.put("add", new Add(collection, scanner));
        commands.put("add_if_max", new AddIfMax(collection, scanner));
        commands.put("update_id", new UpdateId(collection, scanner));
        commands.put("remove_lower", new RemoveLower(collection, scanner));
        commands.put("execute_script", new ExecuteScript(collection, commands));
        commands.put("exit", new Exit());
        commands.put("history", new History(history));
    }


    public void run() {
        while (true) {
            System.out.print("> ");
            // если нет след строки, то выходим
            if (!scanner.hasNextLine()) break;

            // trim() удаляет пробелы
            String line = scanner.nextLine().trim();
            // скип пустой строки
            if (line.isEmpty()) continue;

            // массив parts из введеной строки, в котором команда и аргумент для команды или ток команда
            String[] parts = line.split("\\s+", 2);
            String cmd = parts[0];
            // если массив состоит из 2 частей, то присваеваем 2 часть и удаляем пробелы, иначе пусто
            String arg = parts.length > 1 ? parts[1].trim() : null;
            // сохранение в историю
            remember(cmd);

            // ищем введеную команду в словаре команд. Нету - скип
            Command command = commands.get(cmd);
            if (command == null) {
                System.out.println("Неизвестная команда. Напиши 'help'");
                continue;
            }
            // если есть аргумент, то выполняем команду с ним. Если нет, то без него
            if (arg != null) {
                command.execute(arg);
            } else {
                command.execute();
            }
            // проверяем принадлежит ли наша команда, то есть обьект команды, к классу Exit (instanceof - оператор проверки типа).
            // если да, то создается переменная exit, и вызываем метод shouldExit(), который возвращается true/false
            if (command instanceof Exit exit && exit.shouldExit()) {
                break;
            }
        }
    }


    private void remember(String cmd) {
        // добавляем в конец очереди
        history.addLast(cmd);
        // если длина больше 11 элементов в очереди, то удаляем первый
        if (history.size() > 11) history.removeFirst();
    }
}