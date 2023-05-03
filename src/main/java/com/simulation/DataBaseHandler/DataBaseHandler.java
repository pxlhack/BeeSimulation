package com.simulation.DataBaseHandler;

import com.simulation.Bee.Bee;
import com.simulation.Bee.MaleBee;
import com.simulation.Bee.WorkerBee;
import com.simulation.Model.Habitat;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.sql.*;
import java.util.UUID;

public class DataBaseHandler {
    private static final Habitat h = Habitat.getHabitat();
    private static Connection connection;

    private static final String DB_URL = "jdbc:h2:C:/programming/Java/labs/BeeSimulation/db/bee-db";

    private static void createTable() {
        String SQL_CREATE = "CREATE TABLE IF NOT EXISTS BEES(" + "ID VARCHAR(100) PRIMARY KEY," + "TYPE VARCHAR(10)," + "LIFE_TIME INT," + "BIRTH_TIME INT" + ")";
        startConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();
    }

    private static void dropTable() {
        String SQL_DROP = "DROP TABLE IF EXISTS BEES";
        startConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_DROP);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();
    }


    private static void startConnection() {
        try {
            if (connection == null || connection.isClosed()) connection = DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void insertMaleToTable() {
        startConnection();
        try {
            for (Bee bee : h.getBeeArray()) {
                if (bee instanceof MaleBee) {

                    String sql = "INSERT INTO BEES VALUES(?, ?, ?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, bee.getId().toString());
                    preparedStatement.setString(2, "MALE");
                    preparedStatement.setInt(3, MaleBee.getLifeTime());
                    preparedStatement.setInt(4, h.getTreeMap().get(bee.getId()));

                    preparedStatement.executeUpdate();
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();
    }

    public static void insertWorkerToTable() {
        startConnection();
        try {
            for (Bee bee : h.getBeeArray()) {
                if (bee instanceof WorkerBee) {

                    String sql = "INSERT INTO BEES VALUES(?, ?, ?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, bee.getId().toString());
                    preparedStatement.setString(2, "WORKER");
                    preparedStatement.setInt(3, MaleBee.getLifeTime());
                    preparedStatement.setInt(4, h.getTreeMap().get(bee.getId()));

                    preparedStatement.executeUpdate();

                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();
    }

    public static void selectMaleFromTable(int dailyTime) {
        startConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM BEES WHERE TYPE = 'MALE'");

            while (resultSet.next()) {
                String idString = resultSet.getString("ID");
                UUID id = UUID.fromString(idString);
                int birthTime = resultSet.getInt("BIRTH_TIME");
                int[] c = h.getRandomCoordinates();
                ImageView imageView = new ImageView(new Image("C:\\image\\m_bee.png"));
                h.increaseMaleCount();
                MaleBee bee = new MaleBee(id);
                bee.setImageView(imageView);
                if (birthTime > dailyTime) birthTime = dailyTime;
                h.addToCollections(birthTime, bee, c);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();
    }

    public static void selectWorkerFromTable(int dailyTime) {
        startConnection();
        try {

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM BEES WHERE TYPE = 'WORKER'");

            while (resultSet.next()) {
                String idString = resultSet.getString("ID");
                UUID id = UUID.fromString(idString);
                int birthTime = resultSet.getInt("BIRTH_TIME");
                int[] c = h.getRandomCoordinates();
                WorkerBee bee = new WorkerBee(id, c);
                ImageView imageView = new ImageView(new Image("C:\\image\\w_bee.png"));
                bee.setImageView(imageView);
                h.increaseWorkerCount();
                if (birthTime > dailyTime) birthTime = dailyTime;
                h.addToCollections(birthTime, bee, c);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();
    }


    public static void main(String[] args) {
        dropTable();
        createTable();
    }
}

