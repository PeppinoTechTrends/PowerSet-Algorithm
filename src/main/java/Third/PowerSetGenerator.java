package Third;


import Third.agent.FatherAgent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;

public class PowerSetGenerator {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        String dbUrl = "jdbc:mysql://127.0.0.1:3306/Test";
        String dbUser = "root";
        String dbPassword = "password";

        try(Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)){
            long start = System.currentTimeMillis();
            FatherAgent<String> fatherAgent = new FatherAgent<>(new HashSet<>(Arrays.asList(
                    "a", "b","c","d","e","g","h","i","l","m","n","o","p","q","r","s","t","u","v","z",
                    "1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20",
                    "21","22","22","23","24","25")), connection);
            fatherAgent.delegateWork();
            long end = System.currentTimeMillis();
            System.out.println("Time to execute: " + (end - start) + "ms");
            } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
