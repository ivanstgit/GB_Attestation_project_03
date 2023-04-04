/*
 * Абстрактный класс (модель) животного
 */

package model;

import java.sql.Date;
import java.util.ArrayList;

public abstract class Animal {
    Long id;
    String name;
    Date birthDate;
    ArrayList<String> commandList;
    String animalType;

    public Animal(Long id, String name, Date birthDate) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.commandList = new ArrayList<String>();
    }

    public String getAnimalType() {
        return animalType;
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Date getBirthDate() {
        return this.birthDate;
    }

    public ArrayList<String> getCommandList() {
        return commandList;
    }

    public void addCommand(String command) {
        // по идее логика управления командами сложнее, но по ТЗ только добавление
        // не усложняем (без проверки заполненности, уникальности и т.п.)
        this.commandList.add(command);
    }
}
