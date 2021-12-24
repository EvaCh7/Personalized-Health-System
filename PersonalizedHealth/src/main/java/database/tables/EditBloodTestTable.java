/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.tables;

import mainClasses.BloodTest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import database.init.DB_Connection;
import mainClasses.SimpleUser;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mike
 */
public class EditBloodTestTable {

    public void addBloodTestFromJSON(String json) throws ClassNotFoundException {
        BloodTest bt = jsonToBloodTest(json);
        bt.setValues();
        createNewBloodTest(bt);
    }

    public BloodTest jsonToBloodTest(String json) {
        Gson gson = new Gson();
        BloodTest btest = gson.fromJson(json, BloodTest.class);
        return btest;
    }

    public String bloodTestToJSON(BloodTest bt) {
        Gson gson = new Gson();

        String json = gson.toJson(bt, BloodTest.class);
        return json;
    }

    public JsonArray BloodTestExaminationToJsonArray(String amka, String exam) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        JsonArray json_array = new JsonArray();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT test_date, medical_center," + exam + ", " + exam + "_level " + "FROM bloodtest WHERE amka= '" + amka + "'");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                JsonParser parser = new JsonParser();
                JsonObject blood_test_json = parser.parse(json).getAsJsonObject();
                json_array.add(blood_test_json);
            }
            System.out.println(json_array.toString());
            return json_array;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public JsonArray BloodTestToJsonArray(String amka) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        JsonArray json_array = new JsonArray();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM bloodtest WHERE amka= '" + amka + "'");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                JsonParser parser = new JsonParser();
                JsonObject blood_test_json = parser.parse(json).getAsJsonObject();
                json_array.add(blood_test_json);
            }
            return json_array;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public static ArrayList<BloodTest> getAllBloodTests(String amka) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<BloodTest> btl = new ArrayList<BloodTest>();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM bloodtest WHERE amka= '" + amka + "'");
            while (rs.next()) {

                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                BloodTest bt = gson.fromJson(json, BloodTest.class);
                btl.add(bt);
            }
            return btl;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public BloodTest databaseToBloodTest(String amka, String date) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM bloodtest WHERE amka= '" + amka + "' AND test_date='" + date + "'");
            rs.next();
            String json = DB_Connection.getResultsToJSON(rs);
            Gson gson = new Gson();
            BloodTest bt = gson.fromJson(json, BloodTest.class);
            return bt;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        stmt.close();
        con.close();
        return null;
    }

    public boolean bloodTestIdExists(int id) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT bloodtest_id FROM bloodtest WHERE bloodtest_id= '" + id + "'");
            if (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(EditBloodTestTable.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public void updateBloodTest(int id, double value, String test) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        BloodTest bt = new BloodTest();
        String update = "";
        if (test.equals("cholesterol")) {
            bt.setCholesterol(value);
            bt.setCholesterol_level();
            update = "UPDATE bloodtest SET " + test + "='" + value + "', " + test + "_level='" + bt.getCholesterol_level() + "' WHERE bloodtest_id = '" + id + "'";

        } else if (test.equals("blood_sugar")) {
            bt.setBlood_sugar(value);
            bt.setBlood_sugar_level();
            update = "UPDATE bloodtest SET " + test + "='" + value + "'," + test + "_level='" + bt.getBlood_sugar_level() + "' WHERE bloodtest_id = '" + id + "'";

        } else if (test.equals("iron")) {
            bt.setIron(value);
            bt.setIron_level();
            update = "UPDATE bloodtest SET " + test + "='" + value + "'," + test + "_level='" + bt.getIron_level() + "' WHERE bloodtest_id = '" + id + "'";

        } else if (test.equals("vitamin_d3")) {
            bt.setVitamin_d3(value);
            bt.setVitamin_d3_level();
            update = "UPDATE bloodtest SET " + test + "='" + value + "'," + test + "_level='" + bt.getVitamin_d3_level() + "' WHERE bloodtest_id = '" + id + "'";

        } else if (test.equals("vitamin_b12")) {
            bt.setVitamin_b12(value);
            bt.setVitamin_b12_level();
            update = "UPDATE bloodtest SET " + test + "='" + value + "'," + test + "_level='" + bt.getVitamin_b12_level() + "' WHERE bloodtest_id = '" + id + "'";

        }
        System.out.println(update);
        stmt.executeUpdate(update);
    }

    public void deleteBloodTest(int bloodtestid) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String deleteQuery = "DELETE FROM bloodtest WHERE bloodtest_id='" + bloodtestid + "'";
        stmt.executeUpdate(deleteQuery);
        stmt.close();
        con.close();
    }

    public void createBloodTestTable() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String sql = "CREATE TABLE bloodtest "
                + "(bloodtest_id INTEGER not NULL AUTO_INCREMENT, "
                + "amka VARCHAR (11) not null,"
                + "test_date DATE not NULL, "
                + "medical_center VARCHAR(100) not NULL, "
                + "blood_sugar DOUBLE, "
                + "blood_sugar_level VARCHAR(10),"
                + "cholesterol DOUBLE, "
                + "cholesterol_level VARCHAR(10),"
                + "iron DOUBLE, "
                + "iron_level VARCHAR(10),"
                + "vitamin_d3 DOUBLE, "
                + "vitamin_d3_level VARCHAR(10),"
                + "vitamin_b12 DOUBLE, "
                + "vitamin_b12_level VARCHAR(10),"
                + "PRIMARY KEY ( bloodtest_id ))";
        stmt.execute(sql);
        stmt.close();
        con.close();

    }

    /**
     * Establish a database connection and add in the database.
     *
     * @throws ClassNotFoundException
     */
    public void createNewBloodTest(BloodTest bt) throws ClassNotFoundException {
        try {
            Connection con = DB_Connection.getConnection();

            Statement stmt = con.createStatement();

            String insertQuery = "INSERT INTO "
                    + " bloodtest (amka,test_date,medical_center,blood_sugar,blood_sugar_level,cholesterol,"
                    + "cholesterol_level,iron,iron_level,vitamin_d3,vitamin_d3_level,vitamin_b12,vitamin_b12_level) "
                    + " VALUES ("
                    + "'" + bt.getAmka() + "',"
                    + "'" + bt.getTest_date() + "',"
                    + "'" + bt.getMedical_center() + "',"
                    + "'" + bt.getBlood_sugar() + "',"
                    + "'" + bt.getBlood_sugar_level() + "',"
                    + "'" + bt.getCholesterol() + "',"
                    + "'" + bt.getCholesterol_level() + "',"
                    + "'" + bt.getIron() + "',"
                    + "'" + bt.getIron_level() + "',"
                    + "'" + bt.getVitamin_d3() + "',"
                    + "'" + bt.getVitamin_d3_level() + "',"
                    + "'" + bt.getVitamin_b12() + "',"
                    + "'" + bt.getVitamin_b12_level() + "'"
                    + ")";
            //stmt.execute(table);
            System.out.println(insertQuery);
            stmt.executeUpdate(insertQuery);
            System.out.println("# The bloodtest was successfully added in the database.");

            /* Get the member id from the database and set it to the member */
            stmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(EditBloodTestTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
