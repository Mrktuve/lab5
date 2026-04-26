package model;

import enums.*;

/**
 * Класс, представляющий дополнительную информацию о человеке.
 * <p>
 * Используется как вложенный объект в классе Worker.
 * </p>
 *
 * @author Student
 * @version 1.0
 * @see enums.EyeColor
 * @see enums.HairColor
 * @see enums.Country
 */
public class Person {
    /** Идентификатор паспорта (не может быть пустым) */
    private String passportID;

    /** Цвет глаз из перечисления */
    private EyeColor eyeColor;

    /** Цвет волос из перечисления */
    private HairColor hairColor;

    /** Страна из перечисления */
    private Country country;

    public String getPassportID() {
        return passportID;
    }

    public void setPassportID(String passportID) {
        this.passportID = passportID;
    }

    public EyeColor getEyeColor() {
        return eyeColor;
    }

    public void setEyeColor(EyeColor eyeColor) {
        this.eyeColor = eyeColor;
    }

    public HairColor getHairColor() {
        return hairColor;
    }

    public void setHairColor(HairColor hairColor) {
        this.hairColor = hairColor;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "Person{" +
                "passportID='" + passportID + '\'' +
                ", eyeColor=" + eyeColor +
                ", hairColor=" + hairColor +
                ", country=" + country +
                '}';
    }
}
