package GUI;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;

import controller.AnimalRegisterController;
import model.AnimalRegister;

public class AnimalRegisterMain extends JFrame {
    private AnimalRegister animalTable;
    private AnimalRegisterController mainController;
    private JTable table;

    public AnimalRegisterMain(AnimalRegister reg, AnimalRegisterController cntrl) {
        this.animalTable = reg;
        this.mainController = cntrl;

        // Меню
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Животное");
        menuBar.add(menu);

        JMenuItem itm = new JMenuItem("Добавить животное");
        itm.setActionCommand("add_animal");
        menu.add(itm);
        itm.addActionListener(this.mainController);

        itm = new JMenuItem("Обучить новой комманде");
        itm.setActionCommand("add_command");
        itm.addActionListener(this.mainController);

        menu.add(itm);

        setJMenuBar(menuBar);

        // Table
        table = new JTable(this.animalTable);
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        TableColumn column = null;
        for (int i = 0; i < 5; i++) {
            column = table.getColumnModel().getColumn(i);
            if (i < 2) {
                column.setPreferredWidth(10);
            } else if (i == 4) {
                column.setPreferredWidth(500);
            } else {
                column.setPreferredWidth(50);
            }
        }

        JScrollPane scrollPane = new JScrollPane(table);

        setContentPane(scrollPane);

        // Настройка окна
        setTitle("Реестр животных");
        setPreferredSize(new Dimension(1280, 480));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    public Integer getSelectedIndex() {
        return table.getSelectedRow();
    }

}
