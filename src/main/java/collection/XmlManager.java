package collection;

import enums.*;
import model.*;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.PriorityQueue;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;

public class XmlManager {

    private static final DateTimeFormatter DATE = DateTimeFormatter.ISO_LOCAL_DATE;

    public PriorityQueue<Worker> load(String fileName) {
        PriorityQueue<Worker> queue = new PriorityQueue<>();
        File file = new File(fileName);
        if (!file.exists()) return queue;

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            NodeList workers = doc.getElementsByTagName("worker");
            for (int i = 0; i < workers.getLength(); i++) {
                Element e = (Element) workers.item(i);
                Worker w = parseWorker(e);
                queue.add(w);
            }
        } catch (Exception e) {
            System.out.println("Ошибка чтения XML: " + e.getMessage());
        }
        return queue;
    }

    private Worker parseWorker(Element e) {
        Worker w = new Worker();
        w.setId(Integer.parseInt(e.getAttribute("id")));
        w.setName(text(e, "name"));

        Coordinates coords = new Coordinates();
        coords.setX(Double.parseDouble(text(e, "x")));
        coords.setY(Float.parseFloat(text(e, "y")));
        w.setCoordinates(coords);

        w.setCreationDate(LocalDate.parse(text(e, "creationDate"), DATE));
        w.setSalary(Double.parseDouble(text(e, "salary")));
        w.setStartDate(LocalDate.parse(text(e, "startDate"), DATE));

        String end = text(e, "endDate");
        if (!end.isEmpty()) {
            w.setEndDate(java.sql.Date.valueOf(LocalDate.parse(end, DATE)));
        }

        w.setStatus(Status.valueOf(text(e, "status")));

        Person p = new Person();
        p.setPassportID(text(e, "passportID"));
        p.setEyeColor(EyeColor.valueOf(text(e, "eyeColor")));
        p.setHairColor(HairColor.valueOf(text(e, "hairColor")));
        p.setNationality(Country.valueOf(text(e, "nationality")));
        w.setPerson(p);

        return w;
    }

    private String text(Element parent, String tag) {
        NodeList list = parent.getElementsByTagName(tag);
        if (list.getLength() == 0) return "";
        return list.item(0).getTextContent();
    }

    public void save(String fileName, PriorityQueue<Worker> queue) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();

            Element root = doc.createElement("workers");
            doc.appendChild(root);

            for (Worker w : queue) {
                Element el = doc.createElement("worker");
                el.setAttribute("id", String.valueOf(w.getId()));

                append(doc, el, "name", w.getName());
                append(doc, el, "x", String.valueOf(w.getCoordinates().getX()));
                append(doc, el, "y", String.valueOf(w.getCoordinates().getY()));
                append(doc, el, "creationDate", w.getCreationDate().format(DATE));
                append(doc, el, "salary", String.valueOf(w.getSalary()));
                append(doc, el, "startDate", w.getStartDate().format(DATE));
                append(doc, el, "endDate",
                        w.getEndDate() == null ? "" :
                                ((java.sql.Date) new java.sql.Date(
                                        w.getEndDate().getTime())).toLocalDate().format(DATE));
                append(doc, el, "status", w.getStatus().name());

                Person p = w.getPerson();
                if (p != null) {
                    append(doc, el, "passportID", p.getPassportID());
                    append(doc, el, "eyeColor", p.getEyeColor().name());
                    append(doc, el, "hairColor", p.getHairColor().name());
                    append(doc, el, "nationality", p.getNationality().name());
                }
                root.appendChild(el);
            }

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer t = tf.newTransformer();
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            t.transform(new DOMSource(doc), new StreamResult(new File(fileName)));

        } catch (Exception e) {
            System.out.println("Ошибка записи XML: " + e.getMessage());
        }
    }

    private void append(Document doc, Element parent, String tag, String value) {
        Element el = doc.createElement(tag);
        el.setTextContent(value);
        parent.appendChild(el);
    }
}
