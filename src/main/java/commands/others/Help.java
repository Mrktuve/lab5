package commands.others;

import commands.Command;
import model.Worker;

public class Help extends Command {

    @Override
    public void execute() {
        System.out.println("Доступные команды:");
        System.out.println("help                         : инфа о командах");
        System.out.println("info                         : инфа о коллекции");
        System.out.println("show                         : вывести все элементы");
        System.out.println("add                          : добавить элемент");
        System.out.println("update_id {id}               : обновить элемент по id");
        System.out.println("remove_by_id {id}            : удалить элемент по id");
        System.out.println("clear                        : очистить коллекцию");
        System.out.println("save                         : сохранить коллекцию");
        System.out.println("execute_script file_name     : выполнить скрипт из файла");
        System.out.println("exit                         : завершить программу");
        System.out.println("add_if_max {element}         : добавить, если больше максимального");
        System.out.println("remove_lower {element}       : удалить элементы меньше заданного");
        System.out.println("history                      : последние 11 команды");
        System.out.println("remove_any_by_status status  : удалить элемент с заданным статусом");
        System.out.println("filter_starts_with_name name : вывести элементы с именем, начинающимся с подстроки");
        System.out.println("print_descending             : вывести элементы в порядке убывания");
    }

    @Override public void execute(String arg) {

    }
    @Override public void execute(String arg, Worker worker) {

    }
    @Override public void execute(Worker worker) {

    }
    @Override public String commandInfo() {
        return "инфа о командах";
    }
}