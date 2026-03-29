package collection;

import enums.*;
import model.*;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * Класс для работы с XML файлами через Scanner и PrintWriter
 */
public class XmlManager {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

    /**
     * Загружает коллекцию Workers из XML файла
     * @param fileName имя файла
     * @return PriorityQueue с Worker объектами
     */
    public PriorityQueue<Worker> load(String fileName) {
        PriorityQueue<Worker> queue = new PriorityQueue<>();

        File file = new File(fileName);
        if (!file.exists()) {
            return queue;
        }

        try (Scanner scanner = new Scanner(file)) {
            scanner.useDelimiter("\\Z"); // Читаем весь файл
            String content = scanner.hasNext() ? scanner.next() : "";

            queue = parseXML(content);

        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден. Создана новая коллекция.");
        } catch (Exception e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
        }

        return queue;
    }

    /**
     * Парсит XML строку и создает коллекцию Workers
     */
    private PriorityQueue<Worker> parseXML(String xml) {
        PriorityQueue<Worker> queue = new PriorityQueue<>();

        // Разбиваем на отдельные worker элементы
        String[] workers = xml.split("<worker");

        for (String workerStr : workers) {
            if (workerStr.trim().isEmpty()) {
                continue;
            }

            try {
                Worker worker = parseWorker(workerStr);
                // Проверяем, что worker валидный (не null и имеет имя)
                if (worker != null && worker.getName() != null && !worker.getName().isEmpty()) {
                    queue.add(worker);
                }
            } catch (Exception e) {
                // Пропускаем невалидные элементы
            }
        }

        return queue;
    }

    /**
     * Парсит один элемент worker
     */
    private Worker parseWorker(String xml) {
        Worker worker = new Worker();

        // Извлекаем id из атрибута
        int idStart = xml.indexOf("id=\"") + 4;
        int idEnd = xml.indexOf("\"", idStart);
        if (idStart > 3 && idEnd > idStart) {
            try {
                int id = Integer.parseInt(xml.substring(idStart, idEnd));
                worker.setId(id);
            } catch (NumberFormatException e) {
                // Оставляем автогенерацию id
            }
        }

        // Извлекаем простые текстовые поля
        String name = getTagContent(xml, "name");
        if (name == null || name.isEmpty()) {
            return null; // Worker без имени невалиден
        }
        worker.setName(name);

        // Coordinates
        Coordinates coords = new Coordinates();
        String xStr = getTagContent(xml, "x");
        String yStr = getTagContent(xml, "y");
        if (!xStr.isEmpty()) {
            try {
                coords.setX(Double.parseDouble(xStr));
            } catch (NumberFormatException e) {
                coords.setX(null);
            }
        }
        if (!yStr.isEmpty()) {
            try {
                coords.setY(Float.parseFloat(yStr));
            } catch (NumberFormatException e) {
                coords.setY(0.0f);
            }
        }
        worker.setCoordinates(coords);

        // Даты
        String creationDateStr = getTagContent(xml, "creationDate");
        if (!creationDateStr.isEmpty()) {
            try {
                worker.setCreationDate(LocalDate.parse(creationDateStr, DATE_FORMAT));
            } catch (Exception e) {
                worker.setCreationDate(LocalDate.now());
            }
        } else {
            worker.setCreationDate(LocalDate.now());
        }

        // Salary
        String salaryStr = getTagContent(xml, "salary");
        if (!salaryStr.isEmpty()) {
            try {
                worker.setSalary(Double.parseDouble(salaryStr));
            } catch (NumberFormatException e) {
                worker.setSalary(0.0);
            }
        }

        // StartDate
        String startDateStr = getTagContent(xml, "startDate");
        if (!startDateStr.isEmpty()) {
            try {
                worker.setStartDate(LocalDate.parse(startDateStr, DATE_FORMAT));
            } catch (Exception e) {
                worker.setStartDate(LocalDate.now());
            }
        } else {
            worker.setStartDate(LocalDate.now());
        }

        // EndDate (может быть null)
        String endDateStr = getTagContent(xml, "endDate");
        if (!endDateStr.isEmpty()) {
            try {
                LocalDate localDate = LocalDate.parse(endDateStr, DATE_FORMAT);
                worker.setEndDate(java.sql.Date.valueOf(localDate));
            } catch (Exception e) {
                worker.setEndDate(null);
            }
        }

        // Status
        String statusStr = getTagContent(xml, "status");
        if (!statusStr.isEmpty()) {
            try {
                worker.setStatus(Status.valueOf(statusStr.trim()));
            } catch (Exception e) {
                worker.setStatus(null);
            }
        }

        // Person
        Person person = new Person();

        String passportID = getTagContent(xml, "passportID");
        if (!passportID.isEmpty()) {
            person.setPassportID(passportID);
        }

        String eyeColorStr = getTagContent(xml, "eyeColor");
        if (!eyeColorStr.isEmpty()) {
            try {
                person.setEyeColor(EyeColor.valueOf(eyeColorStr.trim()));
            } catch (Exception e) {
                person.setEyeColor(null);
            }
        }

        String hairColorStr = getTagContent(xml, "hairColor");
        if (!hairColorStr.isEmpty()) {
            try {
                person.setHairColor(HairColor.valueOf(hairColorStr.trim()));
            } catch (Exception e) {
                person.setHairColor(null);
            }
        }

        String nationalityStr = getTagContent(xml, "nationality");
        if (!nationalityStr.isEmpty()) {
            try {
                person.setNationality(Country.valueOf(nationalityStr.trim()));
            } catch (Exception e) {
                person.setNationality(null);
            }
        }

        worker.setPerson(person);

        return worker;
    }

    /**
     * Извлекает содержимое тега
     */
    private String getTagContent(String xml, String tagName) {
        String openTag = "<" + tagName + ">";
        String closeTag = "</" + tagName + ">";

        int startIndex = xml.indexOf(openTag);
        if (startIndex == -1) {
            return "";
        }

        startIndex += openTag.length();
        int endIndex = xml.indexOf(closeTag, startIndex);

        if (endIndex == -1) {
            return "";
        }

        return xml.substring(startIndex, endIndex).trim();
    }

    /**
     * Сохраняет коллекцию Workers в XML файл
     * @param fileName имя файла
     * @param queue коллекция для сохранения
     */
    public void save(String fileName, PriorityQueue<Worker> queue) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            writer.println("<workers>");

            for (Worker worker : queue) {
                writeWorker(writer, worker);
            }

            writer.println("</workers>");

        } catch (IOException e) {
            System.out.println("Ошибка при записи в файл: " + e.getMessage());
        }
    }

    /**
     * Записывает один объект Worker в XML формат
     */
    private void writeWorker(PrintWriter writer, Worker worker) {
        writer.println("  <worker id=\"" + worker.getId() + "\">");

        writer.println("    <name>" + escapeXml(worker.getName()) + "</name>");

        // Coordinates
        if (worker.getCoordinates() != null) {
            writer.println("    <x>" + worker.getCoordinates().getX() + "</x>");
            writer.println("    <y>" + worker.getCoordinates().getY() + "</y>");
        }

        // Dates - с проверкой на null!
        if (worker.getCreationDate() != null) {
            writer.println("    <creationDate>" + worker.getCreationDate().format(DATE_FORMAT) + "</creationDate>");
        } else {
            writer.println("    <creationDate>" + LocalDate.now().format(DATE_FORMAT) + "</creationDate>");
        }

        writer.println("    <salary>" + worker.getSalary() + "</salary>");

        if (worker.getStartDate() != null) {
            writer.println("    <startDate>" + worker.getStartDate().format(DATE_FORMAT) + "</startDate>");
        } else {
            writer.println("    <startDate>" + LocalDate.now().format(DATE_FORMAT) + "</startDate>");
        }

        // EndDate (может быть null)
        if (worker.getEndDate() != null) {
            LocalDate endDate = ((java.sql.Date) worker.getEndDate()).toLocalDate();
            writer.println("    <endDate>" + endDate.format(DATE_FORMAT) + "</endDate>");
        } else {
            writer.println("    <endDate></endDate>");
        }

        // Status
        if (worker.getStatus() != null) {
            writer.println("    <status>" + worker.getStatus().name() + "</status>");
        } else {
            writer.println("    <status></status>");
        }

        // Person
        Person person = worker.getPerson();
        if (person != null) {
            writer.println("    <passportID>" + escapeXml(person.getPassportID()) + "</passportID>");

            if (person.getEyeColor() != null) {
                writer.println("    <eyeColor>" + person.getEyeColor().name() + "</eyeColor>");
            } else {
                writer.println("    <eyeColor></eyeColor>");
            }

            if (person.getHairColor() != null) {
                writer.println("    <hairColor>" + person.getHairColor().name() + "</hairColor>");
            } else {
                writer.println("    <hairColor></hairColor>");
            }

            if (person.getNationality() != null) {
                writer.println("    <nationality>" + person.getNationality().name() + "</nationality>");
            } else {
                writer.println("    <nationality></nationality>");
            }
        }

        writer.println("  </worker>");
    }

    /**
     * Экранирует специальные XML символы
     */
    private String escapeXml(String str) {
        if (str == null) {
            return "";
        }
        return str.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}