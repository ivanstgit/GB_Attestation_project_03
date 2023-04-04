package GUI;

import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Properties;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import controller.AnimalRegisterController;

public class AnimalDialog extends JOptionPane {
    /* Диалог для добавления нового животного */
    private JFrame parent;

    private JTextField nameField;
    private JComboBox<String> typeField;
    private JTextField birthDateField;
    private Properties fieldNames;
    private String[] animalTypesKeys;
    private String[] animalTypesTexts;

    public AnimalDialog(JFrame parent, AnimalRegisterController controller, Properties animalTypes) {
        super(new BorderLayout());
        this.parent = parent;

        animalTypesKeys = new String[animalTypes.size()];
        animalTypesTexts = new String[animalTypes.size()];
        int index = 0;
        for (Object key : animalTypes.keySet()) {
            animalTypesKeys[index] = (String) key;
            animalTypesTexts[index] = animalTypes.getProperty(key.toString());
            index++;
        }

        this.nameField = new JTextField();
        this.typeField = new JComboBox<String>(animalTypesTexts);
        this.birthDateField = new JTextField();
        this.fieldNames = new Properties();
        this.fieldNames.setProperty("name", "Кличка");
        this.fieldNames.setProperty("type", "Тип");
        this.fieldNames.setProperty("birthDate", "Дата рождения");
    }

    public Properties ShowAddDialog(Properties defaultProps) {
        nameField.setText(defaultProps.getProperty("name", ""));
        typeField.setSelectedItem(defaultProps.getProperty("type", animalTypesTexts[0]));
        birthDateField.setText(defaultProps.getProperty("birthDate", ""));
        Object[] inputFields = {
                fieldNames.getProperty("name"), nameField,
                fieldNames.getProperty("type"), typeField,
                fieldNames.getProperty("birthDate"), birthDateField };
        Object[] options = { "Сохранить", "Отменить" };

        addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent ce) {
                nameField.requestFocusInWindow();
            }
        });

        // setVisible(true);

        int option = JOptionPane.showOptionDialog(
                this,
                inputFields,
                "Добавить животное",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);
        if (option == JOptionPane.OK_OPTION) {
            Properties props = new Properties();
            props.setProperty("name", nameField.getText());
            props.setProperty("type", animalTypesKeys[typeField.getSelectedIndex()]);
            props.setProperty("birthDate", birthDateField.getText());
            return props;
        }
        return null;

    }

    public String ShowAddCommandDialog() {
        return JOptionPane.showInputDialog(parent, "Введите новую команду:", "Добавление команды",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void ShowResult(String successTxt, String errorTxt, String errorField) {
        if (errorTxt.isEmpty() && errorField.isEmpty()) {
            JOptionPane.showMessageDialog(parent, successTxt, "Успех",
                    JOptionPane.INFORMATION_MESSAGE);
        } else if (errorField.isEmpty()) {
            JOptionPane.showMessageDialog(parent, errorTxt, "Ошибка", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(parent, "Неверно заполнено поле " + fieldNames.getProperty(errorField),
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
        }

    }

}
