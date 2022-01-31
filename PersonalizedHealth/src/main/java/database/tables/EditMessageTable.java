/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.tables;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import database.tables.EditBloodTestTable;
import database.init.DB_Connection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import mainClasses.BloodTest;
import mainClasses.Message;

/**
 *
 * @author micha
 */
public class EditMessageTable {

    public void addMessageFromJSON(String json) throws ClassNotFoundException {
        Message msg = jsonToMessage(json);
        createNewMessage(msg);
    }

    public Message jsonToMessage(String json) {
        Gson gson = new Gson();
        Message msg = gson.fromJson(json, Message.class);
        return msg;
    }

    public String messageToJSON(Message msg) {
        Gson gson = new Gson();

        String json = gson.toJson(msg, Message.class);
        return json;
    }

    public static JsonArray GetBloodDonationMessages(int user_id) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        JsonArray array = new JsonArray();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM message WHERE user_id=" + user_id + " AND blood_donation=1");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                JsonElement bt = gson.fromJson(json, JsonElement.class);
                array.add(bt);
            }

        } catch (Exception e) {
            stmt.close();
            con.close();
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        stmt.close();
        con.close();
        return array;
    }

    public static JsonArray SelectMessagesOfDoctorWithPatient(int user_id, int doctor_id) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        JsonArray array = new JsonArray();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM message WHERE user_id=" + user_id + " AND blood_donation = 0 AND doctor_id=" + doctor_id + "");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                JsonElement bt = gson.fromJson(json, JsonElement.class);
                array.add(bt);
            }

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        stmt.close();
        con.close();
        return array;
    }

    public static boolean UpdateMessage(int message_id, int doctor_id, int user_id, String sender, int blood_donation, String bloodtype, String date_time, String message) {
        try {
            Connection con = DB_Connection.getConnection();
            Statement stmt;
            try {
                stmt = con.createStatement();
            } catch (SQLException ex) {
                Logger.getLogger(EditMessageTable.class.getName()).log(Level.SEVERE, null, ex);
                return false;

            }
            String updateQuery = "UPDATE message SET user_id='" + user_id + "',sender='" + sender + "',doctor_id='" + doctor_id + "',message='" + message + "',date_time='" + date_time + "',bloodtype='" + bloodtype + "' WHERE message_id = '" + message_id + "'";
            try {
                stmt.executeUpdate(updateQuery);
            } catch (SQLException ex) {
                Logger.getLogger(EditMessageTable.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
            System.out.println(updateQuery);
            stmt.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(EditMessageTable.class.getName()).log(Level.SEVERE, null, ex);

            return false;

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EditMessageTable.class.getName()).log(Level.SEVERE, null, ex);
            return false;

        }

        return true;

    }

    public Message databaseToMessage(int id) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM message WHERE message_id= '" + id + "'");
            rs.next();
            String json = DB_Connection.getResultsToJSON(rs);
            Gson gson = new Gson();
            Message bt = gson.fromJson(json, Message.class);
            return bt;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        stmt.close();
        con.close();
        return null;
    }

    public void createMessageTable() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String sql = "CREATE TABLE message "
                + "(message_id INTEGER not NULL AUTO_INCREMENT, "
                + "doctor_id INTEGER not null,"
                + "user_id INTEGER not null,"
                + "date_time TIMESTAMP  DEFAULT CURRENT_TIMESTAMP not NULL, "
                + "message VARCHAR(1000) not NULL, "
                + "sender VARCHAR(15),"
                + "blood_donation BOOLEAN,"
                + "bloodtype VARCHAR(15),"
                + "FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id), "
                + "FOREIGN KEY (user_id) REFERENCES users(user_id), "
                + "PRIMARY KEY ( message_id ))";
        stmt.execute(sql);
        stmt.close();
        con.close();

    }

    /**
     * Establish a database connection and add in the database.
     *
     * @throws ClassNotFoundException
     */
    public void createNewMessage(Message msg) throws ClassNotFoundException {
        try {
            Connection con = DB_Connection.getConnection();

            Statement stmt = con.createStatement();

            String insertQuery = "INSERT INTO "
                    + " message (doctor_id,user_id,date_time,message,sender,blood_donation,bloodtype) "
                    + " VALUES ("
                    + "'" + msg.getDoctor_id() + "',"
                    + "'" + msg.getUser_id() + "',"
                    + "'" + msg.getDate_time() + "',"
                    + "'" + msg.getMessage() + "',"
                    + "'" + msg.getSender() + "',"
                    + "'" + msg.getBlood_donation() + "',"
                    + "'" + msg.getBloodtype() + "'"
                    + ")";
            //stmt.execute(table);
            System.out.println(insertQuery);
            stmt.executeUpdate(insertQuery);
            System.out.println("# The Message was successfully added in the database.");

            /* Get the member id from the database and set it to the member */
            stmt.close();
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(EditBloodTestTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
