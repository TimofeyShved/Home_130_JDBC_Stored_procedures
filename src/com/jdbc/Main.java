package com.jdbc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Main {

    public static void main( String args[] ) {
        Connection c = null; // соединение
        Statement stmt = null; // поток работы с БД

        try {
            Class.forName("org.sqlite.JDBC");  // формат работы бд
            c = DriverManager.getConnection("jdbc:sqlite:test.db"); // сама бд, подключение к файлу
            c.setAutoCommit(false);  // отключение авто сохронения
            System.out.println("Открытие бд, успех!");

            // ----------------------------------------------------- Создание самой таблицы
            try {
                stmt = c.createStatement(); // бд в поток
                String sql = "CREATE TABLE COMPANY " +
                        "(ID INT PRIMARY KEY     NOT NULL," +
                        " NAME           TEXT    NOT NULL, " +
                        " AGE            INT     NOT NULL, " +
                        " ADDRESS        CHAR(50), " +
                        " SALARY         REAL)"; // создание таблицы в sql
                stmt.executeUpdate(sql); // обновить бд
                //c.commit();
            }catch (Exception e){};

            // ------------------------------------------------------- Добавление данных
            stmt = c.createStatement(); // бд в поток
            String sql;
            /*
            sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) " +
                    "VALUES (1, 'Paul', 32, 'California', 20000.00 );";
            stmt.executeUpdate(sql); // обновление действий по запросу sql, втавка полей в бд / INSERT(вставка)
             */


            // ------------------------------------------------------- Хранимые процедуры
            sql = "CREATE PROCEDURE CompanyCount (OUT cnt INT) " +
                    "BEGIN " +
                        "SELECT count(*) into cnt FROM COMPANY; " +
                    "END";
            stmt.executeUpdate(sql); // обновление действий по запросу sql, Хранимые процедуры в бд / create procedure

            // ------------------------------------------------------- Вызов хранимой процедуры
            CallableStatement callableStatement = c.prepareCall("{call CompanyCount(?)}");
            callableStatement.registerOutParameter(1, java.sql.Types.INTEGER);
            callableStatement.execute();

            System.out.println(callableStatement.getInt(1));
            System.out.println("--------------------");


            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() ); // ошибка
            System.exit(0);
        }
        System.out.println("Хранимые процедуры, успех!");
    }
}