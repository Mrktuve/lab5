package commands;

import collection.*;
import commands.others.*;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.util.*;
import java.util.Scanner;

public class CommandManager {
    private final WorkerCollection collection;
    private final XmlManager xml;
    private final String fileName;
    private final Map<String, Command> commands = new HashMap<>();
    private final Deque<String> history = new ArrayDeque<>();
    private final Scanner scanner;
    private LineReader reader;

    public CommandManager(WorkerCollection collection, XmlManager xml, String fileName) {
        this.collection = collection;
        this.xml = xml;
        this.fileName = fileName;

        this.scanner = new Scanner(System.in);

        try {
            Terminal terminal = TerminalBuilder.builder()
                    .system(true)
                    .jna(true)
                    .build();

            this.reader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .build();

            reader.setVariable(LineReader.HISTORY_SIZE, 10);

        } catch (Exception e) {
            e.printStackTrace();
        }

        registerCommands();
    }

    private void registerCommands() {
        ScriptParser.getInstance().setDependencies(collection, commands);

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
            String line = reader.readLine("> ");
            if (line == null) break;

            line = line.trim();

            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+", 2);
            String cmd = parts[0];
            String arg = parts.length > 1 ? parts[1].trim() : null;

            remember(cmd);

            Command command = commands.get(cmd);
            if (command == null) {
                System.out.println("Неизвестная команда. Напиши 'help'");
                continue;
            }

            if (arg != null) {
                command.execute(arg);
            } else {
                command.execute();
            }

            if (command instanceof Exit exit && exit.shouldExit()) {
                break;
            }
        }
    }

    private void remember(String cmd) {
        history.addLast(cmd);
        if (history.size() > 11) history.removeFirst();
    }
}