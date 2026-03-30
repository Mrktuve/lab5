package collection;

import commands.Command;
import model.Worker;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;






public class ScriptParser {

    private static ScriptParser instance;

    private static final Set<String> executedFiles = new HashSet<>();

    private WorkerCollection collection;
    private Map<String, Command> commandRegistry;


    private ScriptParser() {}

    public static void initialize() {
        if (instance == null) {
            instance = new ScriptParser();
        } else {
            throw new IllegalStateException("ScriptParser already initialized");
        }
    }


    public static ScriptParser getInstance() {
        if (instance == null) {
            throw new IllegalStateException("ScriptParser not initialized. Call initialize() first.");
        }
        return instance;
    }


    public void setDependencies(WorkerCollection collection, Map<String, Command> commandRegistry) {
        this.collection = collection;
        this.commandRegistry = commandRegistry;
    }


    public void executeScript(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            System.out.println("Нужен путь к файлу");
            return;
        }

        // Проверка на рекурсию
        if (executedFiles.contains(fileName)) {
            System.out.println("Обнаружена рекурсия! Файл '" + fileName + "' уже выполняется.");
            return;
        }

        // Добавляем файл в список выполняемых
        executedFiles.add(fileName);

        System.out.println("Выполняю скрипт: " + fileName);

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName.trim()))) {
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                // Пропуск пустых строк и комментариев
                if (line.isEmpty() || line.startsWith("#")) continue;

                System.out.println("> " + line);

                // Парсинг команды
                String[] parts = line.split("\\s+", 2);
                String cmdName = parts[0];
                String arg = parts.length > 1 ? parts[1].trim() : null;

                // Проверка на рекурсивный вызов execute_script
                if (checkRecursion(cmdName, arg)) {
                    continue;
                }

                Worker worker = readWorkerIfNeeded(cmdName, arg, reader);

                try {
                    executeCommand(cmdName, arg, worker);
                } catch (Exception e) {
                    System.out.println("Ошибка при выполнении команды '" + cmdName + "': " + e.getMessage());
                }
            }

            System.out.println("Скрипт завершён");

        } catch (IOException e) {
            System.out.println("Файл не найден: " + fileName);
        } finally {
            executedFiles.remove(fileName);
        }
    }


    private boolean checkRecursion(String cmdName, String arg) {
        if ("execute_script".equals(cmdName) && arg != null && !arg.isEmpty()) {
            if (executedFiles.contains(arg)) {
                System.out.println("Рекурсия: файл '" + arg + "' уже в стеке выполнения");
                return true;
            }
        }
        return false;
    }


    private Worker readWorkerIfNeeded(String cmdName, String arg, BufferedReader reader) {
        if (!needsWorkerInput(cmdName, arg)) {
            return null;
        }

        try {
            return readWorkerFromReader(reader, cmdName, arg);
        } catch (Exception e) {
            System.out.println("Ошибка чтения Worker из файла: " + e.getMessage());
            return null;
        }
    }


    private boolean needsWorkerInput(String cmdName, String arg) {
        return switch (cmdName) {
            case "add", "add_if_max","remove_lower" -> true;
            case "update_id" -> arg != null && !arg.isEmpty();
            default -> false;
        };
    }


    private Worker readWorkerFromReader(BufferedReader reader, String cmdName, String arg) throws Exception {
        Worker w = new Worker();

        if ("update_id".equals(cmdName) && arg != null) {
            w.setId(Integer.parseInt(arg));
        } else {
            int maxId = collection.getQueue().stream()
                    .mapToInt(Worker::getId)
                    .max()
                    .orElse(0);
            w.setId(maxId + 1);
        }




        String name = readNonEmptyLine(reader, "name");
        w.setName(name);

        double x = readBoundedDouble(reader, "x", -26);
        float y = readFloat(reader, "y");
        model.Coordinates coords = new model.Coordinates();
        coords.setX(x);
        coords.setY(y);
        w.setCoordinates(coords);

        w.setCreationDate(java.time.LocalDate.now());
        double salary = readPositiveDouble(reader, "salary");
        w.setSalary(salary);

        java.time.LocalDate startDate = readLocalDate(reader, "startDate");
        w.setStartDate(startDate);

        String endDateStr = readOptionalLine(reader);
        if (endDateStr != null && !endDateStr.isEmpty()) {
            w.setEndDate(java.sql.Date.valueOf(java.time.LocalDate.parse(endDateStr)));
        }

        enums.Status status = readEnum(reader, "status", enums.Status.class);
        w.setStatus(status);

        model.Person p = new model.Person();
        p.setPassportID(readNonEmptyLine(reader, "passportID"));
        p.setEyeColor(readEnum(reader, "eyeColor", enums.EyeColor.class));
        p.setHairColor(readEnum(reader, "hairColor", enums.HairColor.class));
        p.setCountry(readEnum(reader, "country", enums.Country.class));
        w.setPerson(p);

        return w;
    }



    private String readLine(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        if (line == null) throw new IOException("Unexpected end of file");
        return line.trim();
    }

    private String readNonEmptyLine(BufferedReader reader, String fieldName) throws IOException {
        String value;
        while (true) {
            value = readLine(reader);
            if (!value.isEmpty()) return value;
        }
    }

    private String readOptionalLine(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        if (line == null) return null;
        return line.trim();
    }

    private double readDouble(BufferedReader reader, String fieldName) throws IOException {
        try {
            return Double.parseDouble(readLine(reader));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Неверное значение для " + fieldName);
        }
    }

    private double readBoundedDouble(BufferedReader reader, String fieldName, double min) throws IOException {
        double value = readDouble(reader, fieldName);
        if (value <= min) {
            throw new IllegalArgumentException(fieldName + " должен быть > " + min);
        }
        return value;
    }

    private double readPositiveDouble(BufferedReader reader, String fieldName) throws IOException {
        double value = readDouble(reader, fieldName);
        if (value <= 0) {
            throw new IllegalArgumentException(fieldName + " должен быть > 0");
        }
        return value;
    }

    private float readFloat(BufferedReader reader, String fieldName) throws IOException {
        try {
            return Float.parseFloat(readLine(reader));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Неверное значение для " + fieldName);
        }
    }

    private java.time.LocalDate readLocalDate(BufferedReader reader, String fieldName) throws IOException {
        try {
            return java.time.LocalDate.parse(readLine(reader));
        } catch (java.time.format.DateTimeParseException e) {
            throw new IllegalArgumentException("Неверный формат даты для " + fieldName);
        }
    }

    private <T extends Enum<T>> T readEnum(BufferedReader reader, String fieldName, Class<T> enumClass) throws IOException {
        try {
            return Enum.valueOf(enumClass, readLine(reader).toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Неверное значение для " + fieldName);
        }
    }

    private void executeCommand(String cmdName, String arg, Worker worker) {
        Command command = commandRegistry.get(cmdName);

        if (command == null) {
            System.out.println("Неизвестная команда: " + cmdName);
            return;
        }

        if (worker != null) {
            if (arg != null && !arg.isEmpty()) {
                command.execute(arg, worker);
            } else {
                command.execute(worker);
            }
        } else if (arg != null && !arg.isEmpty()) {
            command.execute(arg);
        } else {
            command.execute();
        }
    }

    public void reset() {
        executedFiles.clear();
    }
}