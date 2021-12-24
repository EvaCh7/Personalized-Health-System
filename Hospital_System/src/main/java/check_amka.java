/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import database.init.DB_Connection;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mainClasses.Utils;

/**
 *
 * @author kokol
 */
public class check_amka extends HttpServlet {

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Utils utils = new Utils();
        String json_str = utils.getJSONFromAjax(request.getReader());
        JsonObject amka_json = JsonParser.parseString(json_str).getAsJsonObject();
        String username = amka_json.get("amka").getAsString();
        try {
            if (amka_exists(username)) {
                response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
                System.out.println("amka  exists");
            } else {
                System.out.println("amka  doesn't exists");

            }
        } catch (SQLException ex) {
            Logger.getLogger(check_username.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(check_username.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    public boolean amka_exists(String amka) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM users WHERE amka = '" + amka + "'");
            if (!rs.isBeforeFirst()) {
                while (rs != null && rs.next()) {

                }
            } else {
                return true;
            }

            rs = stmt.executeQuery("SELECT * FROM doctors WHERE amka = '" + amka + "'");
            if (!rs.isBeforeFirst()) {
                return false;
            }

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        stmt.close();

        return true;

    }
}
