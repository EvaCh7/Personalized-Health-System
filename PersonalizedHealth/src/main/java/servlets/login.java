package servlets;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import database.tables.EditDoctorTable;
import database.tables.EditSimpleUserTable;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import mainClasses.Doctor;
import mainClasses.SimpleUser;
import mainClasses.Utils;

/**
 *
 * @author kokol
 */
public class login extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("loggedIn") != null) {
            response.setStatus(200);

            String json = "{ \"username\":\"" + session.getAttribute("loggedIn").toString() + "\", \"type\":\"" + session.getAttribute("type").toString() + "\"  ,\"id\":\"" + session.getAttribute("id").toString() + "\"  }";

            response.getWriter().write(json);
        } else {
            response.setStatus(500);
        }

    }

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
        EditSimpleUserTable simple_user_utils = new EditSimpleUserTable();
        String json_str = utils.getJSONFromAjax(request.getReader());
        JsonObject user_json = JsonParser.parseString(json_str).getAsJsonObject();
        String username = user_json.get("username").getAsString();
        String password = user_json.get("password").getAsString();
        HttpSession session = request.getSession(true);
        Doctor dc;
        SimpleUser user;
        EditDoctorTable doctor_table_utils = new EditDoctorTable();
        try {
            if ((user = simple_user_utils.databaseToSimpleUser(username, password)) != null) {

                session.setAttribute("loggedIn", username);
                session.setAttribute("password", password);
                session.setAttribute("id", user.getUser_id());

                if (username.equals("admin")) {
                    session.setAttribute("type", "admin");

                } else {
                    session.setAttribute("type", "user");

                }

            } else if ((dc = EditDoctorTable.databaseToDoctor(username, password)) != null) {
                if (dc.getCertified() == 1) {
                    session.setAttribute("type", "doctor");

                } else {
                    session.setAttribute("type", "uncertified_doctor");

                }
                session.setAttribute("loggedIn", username);
                session.setAttribute("password", password);
                session.setAttribute("id", dc.getDoctor_id());

            } else {
                session.setAttribute("type", "doesn't exist");
                return;
            }
        } catch (SQLException ex) {
            Logger.getLogger(login.class
                    .getName()).log(Level.SEVERE, null, ex);
            response.setStatus(403);
            return;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(login.class
                    .getName()).log(Level.SEVERE, null, ex);
            response.setStatus(403);
            return;
        }
        String json = "{\"error\":\"no type\"}";
        if (session.getAttribute("type") != null) {
            json = "{\"type\":\"" + session.getAttribute("type").toString() + "\"}";
        }

        System.out.println(json);
        response.setStatus(200);
        response.getWriter().write(json);

    }

}
