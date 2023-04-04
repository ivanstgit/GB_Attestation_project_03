package model;

import java.sql.Date;

public abstract class PackAnimal extends Animal {

    public PackAnimal(Long id, String name, Date birthDate) {
        super(id, name, birthDate);
        // тут по идее должна быть логика, специфичная для этого типа, но по ТЗ ее нет
    }

}
