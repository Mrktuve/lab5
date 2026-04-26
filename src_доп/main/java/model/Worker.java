package model;

import enums.Status;

import java.time.LocalDate;
import java.util.Date;

/**
 * Класс, представляющий работника в системе.
 * <p>
 * Реализует интерфейс Comparable для сортировки в PriorityQueue.
 * Все поля валидируются согласно требованиям технического задания.
 * </p>
 *
 * @author Student
 * @version 1.0
 * @see Coordinates
 * @see Person
 * @see enums.Status
 */
public class Worker implements Comparable<Worker> {

    /** Уникальный идентификатор работника (автогенерация) */
    private int id;

    /** Имя работника (не может быть null или пустым) */
    private String name;

    /** Координаты работника (не может быть null) */
    private Coordinates coordinates;

    /** Дата создания записи (автогенерация) */
    private LocalDate creationDate;

    /** Зарплата работника (должна быть > 0) */
    private double salary;

    /** Дата начала работы */
    private LocalDate startDate;

    /** Дата окончания работы (может быть null) */
    private Date endDate;

    /** Статус работника из перечисления */
    private Status status;

    /** Дополнительная информация о человеке (может быть null) */
    private Person person;

     /**
     * Возвращает уникальный идентификатор.
     * @return id работника
     */
    public int getId() {
        return id;
    }

     /**
     * Устанавливает идентификатор.
     * @param id новый идентификатор
     */
    public void setId(int id) {
        this.id = id;
    }

     /**
     * Возвращает имя работника.
     * @return имя
     */
    public String getName() {
        return name;
    }

     /**
     * Устанавливает имя работника.
     * @param name новое имя
     */
    public void setName(String name) {
        this.name = name;
    }

     /**
     * Возвращает координаты.
     * @return объект Coordinates
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

     /**
     * Устанавливает координаты.
     * @param coordinates новые координаты
     */
    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

     /**
     * Возвращает дату создания.
     * @return дата создания
     */
    public LocalDate getCreationDate() {
        return creationDate;
    }

     /**
     * Устанавливает дату создания.
     * @param creationDate новая дата
     */
    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

     /**
     * Возвращает зарплату.
     * @return размер зарплаты
     */
    public double getSalary() {
        return salary;
    }

     /**
     * Устанавливает зарплату.
     * @param salary новая зарплата
     */
    public void setSalary(double salary) {
        this.salary = salary;
    }

     /**
     * Возвращает дату начала работы.
     * @return дата начала
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Устанавливает дату начала работы.
     * @param startDate новая дата
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

     /**
     * Возвращает дату окончания работы.
     * @return дата окончания или null
     */
    public Date getEndDate() {
        return endDate;
    }

     /**
     * Устанавливает дату окончания работы.
     * @param endDate новая дата или null
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

     /**
     * Возвращает статус работника.
     * @return статус или null
     */
    public Status getStatus() {
        return status;
    }

     /**
     * Устанавливает статус работника.
     * @param status новый статус или null
     */
    public void setStatus(Status status) {
        this.status = status;
    }

     /**
     * Возвращает информацию о человеке.
     * @return объект Person или null
     */
    public Person getPerson() {
        return person;
    }

     /**
     * Устанавливает информацию о человеке.
     * @param person новый объект или null
     */
    public void setPerson(Person person) {
        this.person = person;
    }
     /**
     * Сравнивает работников для сортировки в PriorityQueue.
     * <p>
     * Сортировка по зарплате (по убыванию), затем по имени.
     * </p>
     *
     * @param other другой работник для сравнения
     * @return отрицательное, нулевое или положительное значение
     */

    @Override
    public int compareTo(Worker other) {
        return Integer.compare(this.id, other.id);
    }
    @Override
    public String toString() {
        return "Worker{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", salary=" + salary +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status=" + status +
                ", person=" + person +
                '}';
    }
}
