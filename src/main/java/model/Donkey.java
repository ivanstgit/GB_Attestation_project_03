package model;

import java.sql.Date;

public class Donkey extends PackAnimal {

    public Donkey(Long id, String name, Date birthDate) {
        super(id, name, birthDate);
        // тут по идее должна быть логика, специфичная для этого типа, но по ТЗ ее нет
        this.animalType = AnimalFactory.getType(this.getClass().getName());
    }

}
