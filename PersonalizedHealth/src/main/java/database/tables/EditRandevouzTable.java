/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.tables;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import mainClasses.Randevouz;
import database.init.DB_Connection;
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
public class EditRandevouzTable {

    public boolean addRandevouzFromJSON(String json) throws ClassNotFoundException {
        Randevouz r = jsonToRandevouz(json);
        return createNewRandevouz(r);
    }

    public static JsonArray getDoctorRandevouzByDate(int id, String _date) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        JsonArray js_arr = new JsonArray();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM randevouz WHERE doctor_id=" + id + " AND date_time=\'" + _date + "\'");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                JsonElement js = gson.fromJson(json, JsonElement.class);
                js_arr.add(js);
            }

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return js_arr;
    }

    public static JsonArray getDoctosDoneRandevouz(int id) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        JsonArray js_arr = new JsonArray();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM randevouz WHERE doctor_id=" + id + " AND status=\'done\'");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                JsonElement js = gson.fromJson(json, JsonElement.class);
                js_arr.add(js);
            }

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return js_arr;
    }

    public static JsonArray getDoctosrandevouz(int id) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        JsonArray js_arr = new JsonArray();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM randevouz WHERE doctor_id=" + id + "");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                JsonElement js = gson.fromJson(json, JsonElement.class);
                js_arr.add(js);
            }

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return js_arr;
    }

    public static ArrayList<Randevouz> getAllrandevouz() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<Randevouz> randevouz_list = new ArrayList<Randevouz>();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM randevouz");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Randevouz bt = gson.fromJson(json, Randevouz.class);
                randevouz_list.add(bt);
            }

            return randevouz_list;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public Randevouz databaseToRandevouz(int id) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM randevouz WHERE randevouz_id= '" + id + "'");
            rs.next();
            String json = DB_Connection.getResultsToJSON(rs);
            Gson gson = new Gson();
            Randevouz bt = gson.fromJson(json, Randevouz.class);
            return bt;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public Randevouz jsonToRandevouz(String json) {
        Gson gson = new Gson();
        Randevouz r = gson.fromJson(json, Randevouz.class);
        return r;
    }

    public static String randevouzToJSON(Randevouz r) {
        Gson gson = new Gson();

        String json = gson.toJson(r, Randevouz.class);
        return json;
    }

    public void updateRandevouz(int randevouzID, int userID, String info, String status) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String updateQuery = "UPDATE randevouz SET user_id='" + userID + "',status='" + status + "',user_info='" + info + "' WHERE randevouz_id = '" + randevouzID + "'";
        stmt.executeUpdate(updateQuery);
        System.out.println(updateQuery);
        stmt.close();
        con.close();
    }

    public static void reserveRandevouz(int randevouzID) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String updateQuery = "UPDATE randevouz SET status = \"selected\" WHERE randevouz_id = '" + randevouzID + "'";
        stmt.executeUpdate(updateQuery);
        System.out.println(updateQuery);
        stmt.close();
        con.close();
    }

    public boolean cancelRandevouz(int randevouzID) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String deleteQuery = "UPDATE  randevouz SET status=\"cancelled\" WHERE randevouz_id='" + randevouzID + "'";
        int deleted = stmt.executeUpdate(deleteQuery);
        stmt.close();
        con.close();
        return deleted != 0;
    }

    public static ArrayList<Randevouz> databaseToRandevouzArray() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<Randevouz> randevouz = new ArrayList<Randevouz>();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM randevouz");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Randevouz rand = gson.fromJson(json, Randevouz.class);
                randevouz.add(rand);
            }
            return randevouz;

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }
    
    public static ArrayList<Randevouz> showRandevouzOfID(int user_id) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<Randevouz> randevouz = new ArrayList<Randevouz>();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM randevouz WHERE user_id = '" + user_id + "'");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Randevouz rand = gson.fromJson(json, Randevouz.class);
                randevouz.add(rand);
            }
            return randevouz;

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public boolean deleteRandevouz(int randevouzID) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String deleteQuery = "DELETE FROM randevouz WHERE randevouz_id='" + randevouzID + "'";
        int deleted = stmt.executeUpdate(deleteQuery);
        stmt.close();
        con.close();
        return deleted != 0;
    }

    public void createRandevouzTable() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String sql = "CREATE TABLE randevouz "
                + "(randevouz_id INTEGER not NULL AUTO_INCREMENT, "
                + " doctor_id INTEGER not NULL, "
                + " user_id INTEGER not NULL, "
                + " date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP not NULL, "
                + " price INTEGER  not NULL, "
                + " doctor_info VARCHAR(500),"
                + " user_info VARCHAR(500),"
                + " status VARCHAR(15) not null,"
                + "FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id), "
                + " PRIMARY KEY ( randevouz_id  ))";
        stmt.execute(sql);
        stmt.close();
        con.close();

    }

    /**
     * Establish a database connection and add in the database.
     *
     * @throws ClassNotFoundException
     */
    public boolean createNewRandevouz(Randevouz rand) throws ClassNotFoundException {
        int result = 0;
        try {
            Connection con = DB_Connection.getConnection();

            Statement stmt = con.createStatement();

            String insertQuery = "INSERT INTO "
                    + " randevouz (doctor_id,user_id,date_time,price,doctor_info,user_info,status)"
                    + " VALUES ("
                    + "'" + rand.getDoctor_id() + "',"
                    + "'" + rand.getUser_id() + "',"
                    + "'" + rand.getDate_time() + "',"
                    + "'" + rand.getPrice() + "',"
                    + "'" + rand.getDoctor_info() + "',"
                    + "'" + rand.getUser_info() + "',"
                    + "'" + rand.getStatus() + "'"
                    + ")";
            //stmt.execute(table);

            result = stmt.executeUpdate(insertQuery);
            System.out.println("# The randevouz was successfully added in the database.");

            /* Get the member id from the database and set it to the member */
            stmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(EditRandevouzTable.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result != 0;
    }
}
