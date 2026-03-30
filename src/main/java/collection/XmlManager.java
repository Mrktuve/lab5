package collection;

import enums.*;
import model.*;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.PriorityQueue;
import java.util.Scanner;




public class XmlManager {

    private static final DateTimeFormatter DF = DateTimeFormatter.ISO_LOCAL_DATE;




    public PriorityQueue<Worker> load(String fileName) {
        PriorityQueue<Worker> queue = new PriorityQueue<>();
        File file = new File(fileName);

        if (!file.exists()) {
            return queue;
        }

        try (Scanner sc = new Scanner(file)) {
            sc.useDelimiter("\\Z");
            String xml = sc.hasNext() ? sc.next() : "";

            // Разбиваем по тегам <worker
            String[] parts = xml.split("<worker");
            for (String part : parts) {
                if (part.trim().isEmpty()) continue;

                Worker w = parseWorker(part);
                if (w != null && w.getName() != null && !w.getName().isEmpty()) {
                    queue.add(w);
                }
            }
        } catch (Exception e) {
            System.out.println("Ошибка чтения: " + e.getMessage());
        }

        return queue;
    }



    private Worker parseWorker(String xml) {
        Worker w = new Worker();


        try {
            int idStart = xml.indexOf("id=\"") + 4;
            int idEnd = xml.indexOf("\"", idStart);
            if (idStart > 3 && idEnd > idStart) {
                w.setId(Integer.parseInt(xml.substring(idStart, idEnd)));
            }
        } catch (Exception ignored) {}

        // Простые поля
        w.setName(get(xml, "name"));
        if (w.getName() == null || w.getName().isEmpty()) return null;

        // Coordinates
        Coordinates c = new Coordinates();
        String x = get(xml, "x");
        String y = get(xml, "y");
        if (!x.isEmpty()) {
            try { c.setX(Double.parseDouble(x)); } catch (Exception ignored) {}
        }
        if (!y.isEmpty()) {
            try { c.setY(Float.parseFloat(y)); } catch (Exception ignored) {}
        }
        w.setCoordinates(c);


        try {
            String cd = get(xml, "creationDate");
            w.setCreationDate(cd.isEmpty() ? LocalDate.now() : LocalDate.parse(cd, DF));
        } catch (Exception e) { w.setCreationDate(LocalDate.now()); }

        try {
            String sal = get(xml, "salary");
            w.setSalary(sal.isEmpty() ? 0 : Double.parseDouble(sal));
        } catch (Exception ignored) {}

        try {
            String sd = get(xml, "startDate");
            w.setStartDate(sd.isEmpty() ? LocalDate.now() : LocalDate.parse(sd, DF));
        } catch (Exception e) { w.setStartDate(LocalDate.now()); }


        String ed = get(xml, "endDate");
        if (!ed.isEmpty()) {
            try {
                w.setEndDate(java.sql.Date.valueOf(LocalDate.parse(ed, DF)));
            } catch (Exception ignored) {}
        }

        // Status
        String st = get(xml, "status");
        if (!st.isEmpty()) {
            try { w.setStatus(Status.valueOf(st.trim())); } catch (Exception ignored) {}
        }

        // Person
        Person p = new Person();
        p.setPassportID(get(xml, "passportID"));

        String ec = get(xml, "eyeColor");
        if (!ec.isEmpty()) {
            try { p.setEyeColor(EyeColor.valueOf(ec.trim())); } catch (Exception ignored) {}
        }

        String hc = get(xml, "hairColor");
        if (!hc.isEmpty()) {
            try { p.setHairColor(HairColor.valueOf(hc.trim())); } catch (Exception ignored) {}
        }

        String nc = get(xml, "country");
        if (!nc.isEmpty()) {
            try { p.setCountry(Country.valueOf(nc.trim())); } catch (Exception ignored) {}
        }

        w.setPerson(p);
        return w;
    }


    private String get(String xml, String tag) {
        String open = "<" + tag + ">";
        String close = "</" + tag + ">";
        int start = xml.indexOf(open);
        if (start == -1) return "";
        start += open.length();
        int end = xml.indexOf(close, start);
        if (end == -1) return "";
        return xml.substring(start, end).trim();
    }


    public void save(String fileName, PriorityQueue<Worker> queue) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(fileName))) {
            pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            pw.println("<workers>");

            for (Worker w : queue) {
                writeWorker(pw, w);
            }

            pw.println("</workers>");
        } catch (IOException e) {
            System.out.println("Ошибка записи: " + e.getMessage());
        }
    }


    private void writeWorker(PrintWriter pw, Worker w) {
        pw.println("  <worker id=\"" + w.getId() + "\">");

        pw.println("    <name>" + esc(w.getName()) + "</name>");


        if (w.getCoordinates() != null) {
            pw.println("    <x>" + w.getCoordinates().getX() + "</x>");
            pw.println("    <y>" + w.getCoordinates().getY() + "</y>");
        }


        LocalDate cd = w.getCreationDate();
        pw.println("    <creationDate>" + (cd != null ? cd : LocalDate.now()).format(DF) + "</creationDate>");

        pw.println("    <salary>" + w.getSalary() + "</salary>");

        LocalDate sd = w.getStartDate();
        pw.println("    <startDate>" + (sd != null ? sd : LocalDate.now()).format(DF) + "</startDate>");


        if (w.getEndDate() != null) {
            LocalDate ed = ((java.sql.Date) w.getEndDate()).toLocalDate();
            pw.println("    <endDate>" + ed.format(DF) + "</endDate>");
        } else {
            pw.println("    <endDate></endDate>");
        }

        // Status
        if (w.getStatus() != null) {
            pw.println("    <status>" + w.getStatus().name() + "</status>");
        } else {
            pw.println("    <status></status>");
        }

        // Person
        Person p = w.getPerson();
        if (p != null) {
            pw.println("    <passportID>" + esc(p.getPassportID()) + "</passportID>");

            pw.println("    <eyeColor>" + (p.getEyeColor() != null ? p.getEyeColor().name() : "") + "</eyeColor>");
            pw.println("    <hairColor>" + (p.getHairColor() != null ? p.getHairColor().name() : "") + "</hairColor>");
            pw.println("    <country>" + (p.getCountry() != null ? p.getCountry().name() : "") + "</country>");
        }

        pw.println("  </worker>");
    }


    private String esc(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}