package model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.util.Properties;

import DB.AnimalDao;

public class AnimalFactory {
    public static Properties getAnimalTypes() {
        Properties props = new Properties();
        props.setProperty("camel", "Верблюд");
        props.setProperty("cat", "Кошка");
        props.setProperty("dog", "Собака");
        props.setProperty("donkey", "Осел");
        props.setProperty("hamster", "Хомяк");
        props.setProperty("horse", "Лошадь");
        return props;

    }

    public static Animal get(Long id, String name, Date birthDate, String type) {
        Class<?> animalClass;
        Constructor<?> animalConstructor;
        Object animal;
        try {
            String animalClassName = getClassName(type);
            animalClass = Class.forName(animalClassName);
            animalConstructor = animalClass.getConstructor(Long.class, String.class, java.sql.Date.class);
            animal = animalConstructor.newInstance(new Object[] {
                    id, name, birthDate
            });
            return (Animal) animal;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Animal add(AnimalDao dao, Properties props)
            throws AnimalValidationExceptions, ClassNotFoundException {
        // validation
        String name = props.getProperty("name", null);
        if (name == null || name.isEmpty()) {
            throw new AnimalValidationExceptions("name");
        }
        Date birthDate;
        try {
            birthDate = java.sql.Date.valueOf(props.getProperty("birthDate", null));
        } catch (IllegalArgumentException e) {
            throw new AnimalValidationExceptions("birthDate");
        }

        String type = props.getProperty("type", null);

        if (type == null || type.isEmpty()) {
            throw new AnimalValidationExceptions("type");
        }

        Animal animal = dao.addAnimal(name, birthDate, type);
        return animal;
    }

    public static String getClassName(String animalType) {
        switch (animalType) {
            case "cat":
                return Cat.class.getName();
            case "dog":
                return Dog.class.getName();
            case "hamster":
                return Hamster.class.getName();
            case "camel":
                return Camel.class.getName();
            case "donkey":
                return Donkey.class.getName();
            case "horse":
                return Horse.class.getName();
            default:
                return null;
        }
    }

    public static String getType(String className) {
        switch (className) {
            case "model.Cat":
                return "cat";
            case "model.Dog":
                return "dog";
            case "model.Hamster":
                return "hamster";
            case "model.Camel":
                return "camel";
            case "model.Donkey":
                return "donkey";
            case "model.Horse":
                return "horse";
            default:
                return "";
        }
    }
}
