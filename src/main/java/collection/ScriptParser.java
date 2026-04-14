package collection;

import commands.Command;
import model.*;
import java.io.*;
import java.util.*;

/**
 * парсер ExecuteScript
 */
public class ScriptParser {
    // хранит обьект парсера
    private static ScriptParser instance;
    // множество выполняемых файлов для защиты от рекурсии
    private static Set<String> executed = new HashSet<>();
    private WorkerCollection collection;
    // словарь с названием команд и обьектами команд
    private Map<String, Command> commands;

    // конструктор для создания обьекта
    private ScriptParser() {}

    public static ScriptParser getInstance() {
        // проверяет создан ли обьект, если нет, то создает обьект и возвращаем обьект парсера
        if (instance == null) {
            instance = new ScriptParser();
        }
        return instance;
    }

    public void setDependencies(WorkerCollection c, Map<String, Command> cmd) {
        // сохраняем ссылки на коллекцию и словарь команд
        collection = c;
        commands = cmd;
    }

    /**
     * Выполняет скрипт из файла
     */
    public void executeScript(String fileName) {
        // если название = null или там пустая строка, то выходим из метода
        if (fileName == null || fileName.isEmpty()) return;

        // Защита от рекурсии, executed.contains() проверяет есть ли файл во множестве и выходит из метода
        if (executed.contains(fileName)) {
            System.out.println("Рекурсия: " + fileName);
            return;
        }
        // добавляет файл во множество
        executed.add(fileName);
        System.out.println("Выполняю: " + fileName);

        // создаем обьект для чтения и открываем файл
        try (BufferedReader r = new BufferedReader(new FileReader(fileName))) {
            String line; // переменная для строки
            // читает каждую строку из файла, пока не конец файла
            while ((line = r.readLine()) != null) {
                line = line.trim(); // удаляем пробелы по краям строки
                // если строка пустая или начинается со знака комментария, то пропускаем
                if (line.isEmpty() || line.startsWith("#")) continue;

                System.out.println("> " + line); // вывод команды

                // как и в XmlManager. Разбиваем на 2 части/элемента в массив по пробелам
                String[] parts = line.split("\\s+", 2);
                String cmd = parts[0]; // имя команды
                String arg = parts.length > 1 ? parts[1].trim() : null; //аргумент команды, если массив из 2 элементов, если так то удаляем пробелы по краме, иначе присуждаем null

                // Проверяем есть ли команда execute_script в скрипте, есть ли у нее аргумент, и есть ли аргумент в множестве executed
                if ("execute_script".equals(cmd) && arg != null && executed.contains(arg)) {
                    System.out.println("Рекурсия: " + arg);
                    continue;
                }

                Worker w = null;
                // проверяем есть ли нужные команда и аргумент, если да, то читаем данные работника из файла
                if (needsWorker(cmd, arg)) {
                    w = readWorker(r, cmd, arg);
                }


                Command c = commands.get(cmd); // ищем команду в словаре
                // 4 варианта вызова execute, в зависимости от команды и данных из файла будет вызов нужного execute для команды
                if (c != null) {
                    if (w != null) {
                        if (arg != null) c.execute(arg, w);
                        else c.execute(w);
                    } else if (arg != null) {
                        c.execute(arg);
                    } else {
                        c.execute();
                    }
                } else {
                    System.out.println("Неизвестная команда: " + cmd);
                }
            }
            System.out.println("Скрипт завершён");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        } finally { // всегда выполняется
            executed.remove(fileName); // удаляем файл из множества
        }
    }

    /**
     * Проверяем какие ввели команду и в зависимости от этого читаем  Worker
     */
    private boolean needsWorker(String cmd, String arg) {
        return switch (cmd) {
            case "add", "add_if_max", "remove_lower" -> true;
            case "update_id" -> arg != null && !arg.isEmpty();
            default -> false;
        };
    }

    /**
     * Читает Worker из файла
     */
    // throws Exception метод не ловит ошибки парсинга
    private Worker readWorker(BufferedReader r, String cmd, String arg) throws Exception {
        Worker w = new Worker();

        // проверяем команду по словарю, и есть ли аргумент
        if ("update_id".equals(cmd) && arg != null) {
            w.setId(Integer.parseInt(arg));// присуждаем ID из аргумента
        } else {
            int maxId = collection.getQueue().stream() // stream() создает поток из коллекции
                    .mapToInt(Worker::getId) // извлекает все ID
                    .max() // находит максимальный
                    .orElse(0); // если пусто, то возвращает 0
            w.setId(maxId + 1); // устанавливает ID
        }


        w.setName(read(r)); // читает след строку

        Coordinates c = new Coordinates();
        try { c.setX(Double.parseDouble(read(r))); } catch (Exception e) { c.setX(-25.0); }
        try { c.setY(Float.parseFloat(read(r))); } catch (Exception e) { c.setY(0.0f); }
        w.setCoordinates(c);

        w.setCreationDate(java.time.LocalDate.now());
        try { w.setSalary(Double.parseDouble(read(r))); } catch (Exception e) { w.setSalary(0.0); }
        try { w.setStartDate(java.time.LocalDate.parse(read(r))); } catch (Exception e) { w.setStartDate(java.time.LocalDate.now()); }

        String ed = readOpt(r);
        if (ed != null && !ed.isEmpty()) {
            try { w.setEndDate(java.sql.Date.valueOf(java.time.LocalDate.parse(ed))); }
            catch (Exception e) {}
        }

        try { w.setStatus(enums.Status.valueOf(read(r).toUpperCase())); }
        catch (Exception e) { w.setStatus(null); }

        Person p = new Person();
        p.setPassportID(read(r));
        try { p.setEyeColor(enums.EyeColor.valueOf(read(r).toUpperCase())); } catch (Exception e) {}
        try { p.setHairColor(enums.HairColor.valueOf(read(r).toUpperCase())); } catch (Exception e) {}
        try { p.setCountry(enums.Country.valueOf(read(r).toUpperCase())); } catch (Exception e) {}
        w.setPerson(p);

        return w;
    }

    /**
     * Читает строку (обязательную)
     */
    private String read(BufferedReader r) throws IOException {
        String s = r.readLine();
        return s == null ? "" : s.trim(); // возвращаем "", если строка null, иначе удаляем пробелы в строке и возвращаем ее
    }

    /**
     * Читает строку (опциональную)
     */
    private String readOpt(BufferedReader r) throws IOException {
        String s = r.readLine();
        return s == null ? null : s.trim();
    }
}