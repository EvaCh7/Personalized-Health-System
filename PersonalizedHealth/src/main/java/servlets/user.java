package servlets;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import database.tables.EditDoctorTable;
import database.tables.EditSimpleUserTable;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import mainClasses.SimpleUser;
import mainClasses.Utils;

/**
 *
 * @author kokol
 */
public class user extends HttpServlet {

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
        String param = request.getParameter("type");
        if (param.equals("user_info")) {
            HttpSession session = request.getSession();

            String username = session.getAttribute("loggedIn").toString();
            String password = session.getAttribute("password").toString();
            EditSimpleUserTable user_utils = new EditSimpleUserTable();
            String user_info = "";
            try {
                user_info = user_utils.databaseUserToJSON(username, password);
                System.out.println(user_info);

            } catch (SQLException ex) {
                Logger.getLogger(user.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(user.class.getName()).log(Level.SEVERE, null, ex);
            }
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(user_info);
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
        String json_str = utils.getJSONFromAjax(request.getReader());
        EditSimpleUserTable simple_user_obj = new EditSimpleUserTable();
        SimpleUser user = simple_user_obj.jsonToSimpleUser(json_str);
        try {
            simple_user_obj.updateUser(user);

        } catch (SQLException ex) {
            Logger.getLogger(register.class.getName()).log(Level.SEVERE, null, ex);
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(user.class.getName()).log(Level.SEVERE, null, ex);
        }

        response.setStatus(HttpServletResponse.SC_OK);

        response.getWriter().write(json_str);
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

}
