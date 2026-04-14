package commands;

import model.Worker;

/**
 * Абстрактный базовый класс для всех команд приложения.
 * <p>
 * Реализует паттерн Command, предоставляя единый интерфейс
 * для выполнения команд с разными параметрами.
 * </p>
 *
 * @author Student
 * @version 1.0
 * @see model.Worker
 */
public abstract class Command {

     /**
     * Конструктор по умолчанию.
     */
    public Command() {}

     /**
     * Выполняет команду без аргументов.
     * <p>
     * Используется для команд типа: help, info, show, clear, exit
     * </p>
     */
    public abstract void execute();

     /**
     * Выполняет команду с одним строковым аргументом.
     * <p>
     * Используется для команд типа: remove_by_id 5, update_id 3
     * </p>
     *
     * @param arg строковый аргумент команды
     */
    public abstract void execute(String arg);

     /**
     * Выполняет команду с аргументом и объектом Worker.
     * <p>
     * Используется при выполнении команд из скриптов,
     * когда данные работника читаются из файла.
     * </p>
     *
     * @param arg строковый аргумент команды
     * @param worker объект Worker с данными
     */
    public abstract void execute(String arg, Worker worker);

     /**
     * Выполняет команду с объектом Worker.
     * <p>
     * Используется при выполнении команд из скриптов
     * без дополнительных аргументов.
     * </p>
     *
     * @param worker объект Worker с данными
     */
    public abstract void execute(Worker worker);

     /**
     * Возвращает краткое описание команды для справки.
     *
     * @return строка с описанием команды
     */
    public abstract String commandInfo();
}