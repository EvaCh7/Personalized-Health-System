/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

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
import mainClasses.Doctor;
import mainClasses.SimpleUser;
import mainClasses.Utils;

/**
 *
 * @author kokol
 */
public class doctor extends HttpServlet {

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

        if (param.equals("doctor_info")) {
            HttpSession session = request.getSession();
            String username = session.getAttribute("loggedIn").toString();
            String password = session.getAttribute("password").toString();
            EditDoctorTable doctor_utils = new EditDoctorTable();
            String doctor_info = "";
            try {
                System.out.println("username: " + username);
                System.out.println("password: " + password);

                doctor_info = doctor_utils.databaseToJSON(username, password);
                System.out.println(doctor_info);

            } catch (SQLException ex) {
                Logger.getLogger(user.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(user.class.getName()).log(Level.SEVERE, null, ex);
            }
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(doctor_info);
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
        EditDoctorTable doc_obj = new EditDoctorTable();
        Doctor doc = doc_obj.jsonToDoctor(json_str);
        try {
            doc_obj.updateDoctorInfo(doc);
            HttpSession session = request.getSession();
            session.setAttribute("password", doc.getPassword());

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
