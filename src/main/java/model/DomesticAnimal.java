package model;

import java.sql.Date;

public abstract class DomesticAnimal extends Animal {

    public DomesticAnimal(Long id, String name, Date birthDate) {
        super(id, name, birthDate);
        // тут по идее должна быть логика, специфичная для этого типа, но по ТЗ ее нет
    }

}
