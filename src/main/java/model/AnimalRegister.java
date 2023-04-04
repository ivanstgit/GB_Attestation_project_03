/*
 * Основная модель регистра, совмещена с моделью остовного компонента приложения
 */
package model;

import java.util.ArrayList;
import java.util.Properties;

import javax.swing.table.AbstractTableModel;
import DB.AnimalDao;

public class AnimalRegister extends AbstractTableModel { // DefaultTableModel {//
    private ArrayList<Animal> animalList;
    private AnimalDao animalDao;
    private String[] columnNames = {
            "ID",
            "Тип",
            "Кличка",
            "Дата рождения",
            "Команды"
    };
    private Properties animalTypes = AnimalFactory.getAnimalTypes();

    public AnimalRegister(AnimalDao dao) throws ClassNotFoundException {
        this.animalDao = dao;
        this.animalList = animalDao.getAnimalList();
    }

    public void addAnimal(Animal a) {
        animalList.add(a);
    }

    public Animal getAnimal(int rowIndex) {
        return this.animalList.get(rowIndex);
    }

    @Override
    public int getColumnCount() {
        return this.columnNames.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex].toString();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Long.class;
            case 1:
                return String.class;
            case 2:
                return String.class;
            case 3:
                return java.sql.Date.class;
            case 4:
                return String.class;
            default:
                return null;
        }
    }

    @Override
    public int getRowCount() {
        return this.animalList.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Animal selectedAnimal = this.animalList.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return selectedAnimal.getId();
            case 1:
                return animalTypes.getProperty(selectedAnimal.getAnimalType(), selectedAnimal.getAnimalType());
            case 2:
                return selectedAnimal.getName();
            case 3:
                return selectedAnimal.getBirthDate();
            case 4:
                return selectedAnimal.getCommandList().toString();
            default:
                return null;

        }

    }

}
