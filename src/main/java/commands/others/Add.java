package commands.others;

import collection.WorkerCollection;
import commands.Command;
import enums.Country;
import enums.EyeColor;
import enums.HairColor;
import enums.Status;
import model.Coordinates;
import model.Person;
import model.Worker;

import java.util.Scanner;

public class Add extends Command {
    private final WorkerCollection collection;
    private final Scanner scanner;
    private static int nextId = 1;

    public Add(WorkerCollection collection, Scanner scanner) {
        this.collection = collection;
        this.scanner = scanner;
        for (Worker w : collection.getQueue()) {
            nextId = Math.max(nextId, w.getId() + 1);
        }
    }

    @Override public void execute() {
        Worker w = readWorker(-1);
        collection.add(w);
        System.out.println("Элемент добавлен (id = " + w.getId() + ")");
    }

    @Override public void execute(String arg) {
        System.out.println("Команда требует интерактивного ввода");
    }
    @Override public void execute(String arg, Worker worker) {

    }
    @Override public void execute(Worker worker) {
        collection.add(worker);
        System.out.println("Элемент добавлен (id = " + worker.getId() + ")");
    }

    @Override public String commandInfo() {
        return "добавить элемент";
    }

    protected Worker readWorker(int fixedId) {
        Worker w = new Worker();
        int id = fixedId > 0 ? fixedId : nextId++;
        w.setId(id);

        System.out.print("Введите имя: ");
        String name;
        while (true) {
            name = scanner.nextLine().trim();
            if (!name.isEmpty()) break;
            System.out.print("Имя не может быть пустым: ");
        }
        w.setName(name);

        Double x;
        while (true) {
            System.out.print("Введите координату x: ");
            try {
                x = Double.parseDouble(scanner.nextLine().trim());
                if (x > -26) break;
            } catch (NumberFormatException ignored) { }
            System.out.println("Неверное значение x.");
        }

        float y;
        while (true) {
            System.out.print("Введите координату y: ");
            try {
                y = Float.parseFloat(scanner.nextLine().trim());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Неверное значение y.");
            }
        }
        Coordinates coords = new Coordinates();
        coords.setX(x);
        coords.setY(y);
        w.setCoordinates(coords);

        w.setCreationDate(java.time.LocalDate.now());

        double salary;
        while (true) {
            System.out.print("Введите зарплату: ");
            try {
                salary = Double.parseDouble(scanner.nextLine().trim());
                if (salary > 0) break;
            } catch (NumberFormatException ignored) { }
            System.out.println("Неверное значение зарплаты.");
        }
        w.setSalary(salary);

        java.time.LocalDate startDate;
        while (true) {
            System.out.print("Введите дату начала работы (yyyy-mm-dd): ");
            try {
                startDate = java.time.LocalDate.parse(scanner.nextLine().trim());
                break;
            } catch (java.time.format.DateTimeParseException e) {
                System.out.println("Неправильный формат даты.");
            }
        }
        w.setStartDate(startDate);

        System.out.print("Введите дату окончания работы (yyyy-mm-dd) или пусто: ");
        String endStr = scanner.nextLine().trim();
        if (!endStr.isEmpty()) {
            try {
                java.time.LocalDate d = java.time.LocalDate.parse(endStr);
                w.setEndDate(java.sql.Date.valueOf(d));
            } catch (Exception e) {
                System.out.println("Дата окончания проигнорирована.");
            }
        }

        Status status;
        while (true) {
            System.out.print("Введите статус (FIRED, RECOMMENDED_FOR_PROMOTION, REGULAR, PROBATION): ");
            try {
                status = Status.valueOf(scanner.nextLine().trim());
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Нет такого статуса.");
            }
        }
        w.setStatus(status);

        Person p = new Person();

        System.out.print("Введите passportID: ");
        String pid;
        while (true) {
            pid = scanner.nextLine().trim();
            if (!pid.isEmpty()) break;
            System.out.print("Не может быть пустым: ");
        }
        p.setPassportID(pid);

        EyeColor eye;
        while (true) {
            System.out.print("Введите цвет глаз (RED, YELLOW, ORANGE, BROWN): ");
            try {
                eye = EyeColor.valueOf(scanner.nextLine().trim());
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Нет такого цвета.");
            }
        }
        p.setEyeColor(eye);

        HairColor hair;
        while (true) {
            System.out.print("Введите цвет волос (GREEN, BLACK, BLUE, WHITE): ");
            try {
                hair = HairColor.valueOf(scanner.nextLine().trim());
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Нет такого цвета.");
            }
        }
        p.setHairColor(hair);

        Country country;
        while (true) {
            System.out.print("Введите страну (FRANCE, NORTH_KOREA, JAPAN, RUSSIAN, BELARUS): ");
            try {
                country = Country.valueOf(scanner.nextLine().trim());
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Нет такой страны.");
            }
        }
        p.setNationality(country);

        w.setPerson(p);
        return w;
    }
}