/*
 * controller
 */
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import DB.AnimalDBDuplicateException;
import DB.AnimalDBException;
import DB.AnimalDao;
import GUI.AnimalDialog;
import GUI.AnimalRegisterMain;
import model.Animal;
import model.AnimalCommand;
import model.AnimalFactory;
import model.AnimalRegister;
import model.AnimalValidationExceptions;

public class AnimalRegisterController implements ActionListener {
    AnimalRegisterMain mainFrame;
    AnimalRegister mainModel;
    AnimalDao animalDAO;

    public AnimalRegisterController() {
    }

    public String process(String reqDB) {
        animalDAO = new AnimalDao(reqDB);
        try {
            mainModel = new AnimalRegister(animalDAO);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return "model Error";
        }
        mainFrame = new AnimalRegisterMain(mainModel, this);
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();

        switch (command) {
            case "add_animal":
                // Создайте класс Счетчик, у которого есть метод add(), увеличивающий̆
                // значение внутренней̆ int переменной̆ на 1 при нажатии “Завести новое
                // животное” Сделайте так, чтобы с объектом такого типа можно было работать в
                // блоке try-with-resources.
                Counter counterBad = new Counter();
                try (
                        Counter counterGood = new Counter();) {
                    counterGood.add();
                    counterBad.add();

                    AnimalDialog addDialog = new AnimalDialog(mainFrame, this, AnimalFactory.getAnimalTypes());
                    Properties defaultProps = new Properties();

                    defaultProps.setProperty("name", "");
                    defaultProps.setProperty("type", "horse");
                    defaultProps.setProperty("birthDate", "2020-01-01");

                    Properties props = addDialog.ShowAddDialog(defaultProps);
                    int cnt = 1;
                    while (props != null) {
                        cnt++;
                        if (cnt > 10) {
                            break;
                        }

                        try {
                            Animal animal = AnimalFactory.add(animalDAO, props);
                            if (animal == null) {
                                addDialog.ShowResult("", "При добавлении животного возникла ошибка!", "");
                            } else {
                                addDialog.ShowResult("Животное добавлено в реестр", "", "");
                                mainModel.addAnimal(animal);
                                mainModel.fireTableDataChanged();
                            }

                            break;
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                            addDialog.ShowResult("", "Ошибка базы данных", "");
                            break;
                        } catch (AnimalValidationExceptions e) {
                            // e.printStackTrace();
                            addDialog.ShowResult("", "", e.getMessage());
                            defaultProps = props;
                            props = addDialog.ShowAddDialog(defaultProps);
                        } catch (AnimalDBDuplicateException e) {
                            // e.printStackTrace();
                            addDialog.ShowResult("", "Животное уже существует!", "");
                            break;
                        }
                    }
                }
                // Нужно бросить исключение, если работа с объектом типа счетчик была не в
                // ресурсном try и/или ресурс остался открыт. Значение считать в ресурсе try,
                // если при заведении животного заполнены все поля.
                try {
                    counterBad.get();
                } catch (CounterException e) {
                    e.printStackTrace();
                }
                break;
            case "add_command":

                AnimalDialog addCommandDialog = new AnimalDialog(mainFrame, this, AnimalFactory.getAnimalTypes());
                Integer index = mainFrame.getSelectedIndex();
                if (index != null && index >= 0) {
                    Animal an = mainModel.getAnimal(index);
                    String cmd = addCommandDialog.ShowAddCommandDialog();
                    if (cmd != null) {
                        if (!cmd.isEmpty()) {
                            try {
                                AnimalCommand ac = animalDAO.addAnimalCommand(an.getId(), cmd);
                                if (ac == null) {
                                    addCommandDialog.ShowResult("", "При добавлении команды возникла ошибка!", "");
                                } else {
                                    an.addCommand(cmd);
                                    addCommandDialog.ShowResult("Команда добавлена!", "", "");
                                    mainModel.fireTableDataChanged();
                                }
                            } catch (AnimalDBException e) {
                                e.printStackTrace();
                                addCommandDialog.ShowResult("", "Ошибка базы данных", "");
                                break;
                            } catch (AnimalDBDuplicateException e) {
                                // e.printStackTrace();
                                addCommandDialog.ShowResult("", "Команда уже существует!", "");
                                break;
                            }
                        }
                    }

                } else {
                    addCommandDialog.ShowResult("", "Не выбрано животное в таблице", "");
                }

        }

    }

}
