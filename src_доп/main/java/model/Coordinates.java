package model;

/**
 * Класс для хранения координат работника.
 * <p>
 * Координата x должна быть больше -26 согласно ТЗ.
 * </p>
 *
 * @author Student
 * @version 1.0
 */
public class Coordinates {
    /** Координата X (обёрточный тип, может быть null, должен быть > -26) */
    private Double x;

    /** Координата Y (примитивный тип, не может быть null) */
    private float y;

    /**
     * Возвращает координату X.
     * @return значение x или null
     */
    public Double getX() {
        return x;
    }

     /**
     * Устанавливает координату X с валидацией.
     * @param x новое значение (должно быть > -26)
     */
    public void setX(Double x) {
        this.x = x;
    }

     /**
     * Возвращает координату Y.
     * @return значение y
     */
    public float getY() {
        return y;
    }

     /**
     * Устанавливает координату Y.
     * @param y новое значение
     */
    public void setY(float y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Coordinates{x=" + x + ", y=" + y + '}';
    }
}
