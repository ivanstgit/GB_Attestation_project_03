
/*
 * main program
 */
import java.sql.SQLException;

import controller.AnimalRegisterController;

public class Task_13_15 {
    public static void main(String[] args) throws SQLException {
        System.out.println("test");

        AnimalRegisterController controller = new AnimalRegisterController();
        String result = controller.process("SQLite"); // "MySQL", "SQLite" supported
        if (result != null) {
            System.out.println(result);
        }
    }

}