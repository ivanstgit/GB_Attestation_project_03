package DB;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import model.*;

public class AnimalDao implements AutoCloseable {
    private String dbURL;
    private String dbUser;
    private String dbPassword;
    private String dbClassName;
    private Connection connection = null;
    private String dbType;

    public AnimalDao(String reqDBType) {
        if (reqDBType == "SQLite") {
            this.dbType = reqDBType;
            this.dbClassName = "org.sqlite.JDBC";
            this.dbURL = "jdbc:sqlite:Animals.sqlite";
            this.dbUser = "";
            this.dbPassword = "";
        } else if (reqDBType == "MySQL") {
            this.dbType = reqDBType;
            this.dbClassName = "com.mysql.cj.jdbc.Driver";
            this.dbURL = "jdbc:mysql://localhost:3306/HUMAN_FRIENDS";
            this.dbUser = "mysqluser";
            this.dbPassword = "MySQL_Password";
        } else {
            throw new AnimalUnsupportedDBException(reqDBType);
        }

    }

    public ArrayList<Animal> getAnimalList() throws AnimalDBException {
        ArrayList<Animal> animalList = new ArrayList<>();
        Map<Long, Integer> animalIndex = new HashMap<Long, Integer>();
        Integer index = 0;

        openConnection();

        String qry = "select * from animals";
        try (
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(qry)) {
            index = 0;
            while (rs.next()) {
                Animal animal = serializeToAnimal(rs);
                animalList.add(animal);
                animalIndex.put(animal.getId(), index);
                index++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AnimalDBException("Error selecting animal list");
        }

        qry = "select * from animals_commands";
        try (
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(qry)) {
            while (rs.next()) {
                AnimalCommand c = serializeToAnimalCommand(rs);
                index = animalIndex.get(c.getAnimalId());
                if (index != null) {
                    Animal animal = animalList.get(index);
                    animal.addCommand(c.getCommand());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AnimalDBException("Error selecting animal command list");
        }

        animalIndex.clear();
        closeConnection();
        return animalList;
    }

    public Animal addAnimal(String name, Date birthDate, String type)
            throws AnimalDBException, AnimalDBDuplicateException {
        /*
         * В БД генерируется ключ записи, плюс может быть своя логика
         * поэтому сначала проверяем наличие записи по семaнтике, добавляем,
         * считываем по семантаке
         */
        Animal animal = null;
        Boolean isExists = false;

        openConnection();

        // check if already exists
        String qry = "SELECT id FROM animals WHERE name = '" + name +
                "' AND birth_date = '" + birthDate + "' AND animal_type = '" + type + "'";
        try (
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(qry)) {
            if (rs.next()) {
                isExists = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AnimalDBException("Error while checking duplicates");
        }

        if (isExists) {
            throw new AnimalDBDuplicateException("Animal already exists");
        }

        // insert new
        try (final PreparedStatement preparedStatement = connection
                .prepareStatement("INSERT INTO animals (name, birth_date, animal_type) VALUES (?, ?, ?)")) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, birthDate.toString());
            preparedStatement.setString(3, type);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AnimalDBException("Error on insert animal");
        }

        // get added
        qry = "select * from animals WHERE name = '" + name +
                "' AND birth_date = '" + birthDate + "' AND animal_type = '" + type + "'";
        try (
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(qry)) {
            while (rs.next()) {
                animal = serializeToAnimal(rs);
                break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AnimalDBException("Error getting added");
        }

        closeConnection();
        return animal;
    }

    public AnimalCommand addAnimalCommand(Long animalId, String cmd)
            throws AnimalDBException, AnimalDBDuplicateException {
        /*
         * В БД генерируется ключ записи, плюс может быть своя логика
         * поэтому сначала проверяем наличие записи по семaнтике, добавляем,
         * считываем по семантаке
         */
        AnimalCommand animalCommand = null;
        Boolean isExists = false;

        try {
            openConnection();

            // check if already exists
            String qry = "SELECT id FROM animals_commands WHERE animal_id = '" + animalId +
                    "' AND command = '" + cmd + "'";
            try (
                    Statement statement = connection.createStatement();
                    ResultSet rs = statement.executeQuery(qry)) {
                if (rs.next()) {
                    isExists = true;
                }
            }

            if (isExists) {
                throw new AnimalDBDuplicateException("Animal command already exists");
            }

            // insert new
            try (
                    final PreparedStatement preparedStatement = connection
                            .prepareStatement("INSERT INTO animals_commands (animal_id, command) VALUES (?, ?)");) {
                preparedStatement.setLong(1, animalId);
                preparedStatement.setString(2, cmd);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new AnimalDBException("Error on insert animal");
            }

            // get added
            qry = "select * from animals_commands WHERE animal_id = '" + animalId +
                    "' AND command = '" + cmd + "'";
            try (
                    Statement statement = connection.createStatement();
                    ResultSet rs = statement.executeQuery(qry)) {
                while (rs.next()) {
                    animalCommand = serializeToAnimalCommand(rs);
                    break;
                }
            }

            closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return animalCommand;
    }

    private void openConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                try {
                    Class.forName(dbClassName);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    throw new AnimalDBException("Driver class not found");
                }

                connection = DriverManager.getConnection(
                        dbURL, dbUser, dbPassword);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AnimalDBException("Error establishing connection");
        }
    }

    private void closeConnection() throws AnimalDBException {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AnimalDBException("Error establishing connection");
        }
    }

    private Animal serializeToAnimal(ResultSet rs) throws SQLException {
        Animal animal = null;
        if (dbType == "SQLite") {
            animal = AnimalFactory.get(
                    (long) rs.getInt("id"),
                    rs.getString("name"),
                    java.sql.Date.valueOf(rs.getString("birth_date")),
                    rs.getString("animal_type"));
        } else {
            animal = AnimalFactory.get(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getDate("birth_date"),
                    rs.getString("animal_type"));
        }
        return animal;

    }

    private AnimalCommand serializeToAnimalCommand(ResultSet rs) {
        try {
            AnimalCommand animalCommand = new AnimalCommand(
                    rs.getLong("id"),
                    rs.getLong("animal_id"),
                    rs.getString("command"));
            return animalCommand;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

    }

    @Override
    public void close() throws Exception {
        if (connection == null || connection.isClosed()) {
            closeConnection();
        }
    }

}
