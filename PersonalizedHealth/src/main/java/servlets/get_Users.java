package servlets;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import database.tables.EditDoctorTable;
import database.tables.EditSimpleUserTable;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import mainClasses.Doctor;
import mainClasses.SimpleUser;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author eva
 */
public class get_Users extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ShowCertDocs</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ShowCertDocs at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            JsonArray json_array = new JsonArray();

            ArrayList<Doctor> res_doc = EditDoctorTable.databaseToDoctorArray();
            for (Doctor doc : res_doc) {
                String json_str = EditDoctorTable.doctorToJSON(doc);
                JsonObject user_json = JsonParser.parseString(json_str).getAsJsonObject();
                json_array.add(user_json);
            }
            ArrayList<SimpleUser> res_user = EditSimpleUserTable.databaseToUser();

            for (SimpleUser us : res_user) {
                String json_str = EditSimpleUserTable.simpleUserToJSON(us);
                JsonObject user_json = JsonParser.parseString(json_str).getAsJsonObject();
                json_array.add(user_json);
            }
            response.setStatus(HttpServletResponse.SC_OK);

            response.getWriter().write(json_array.toString());
        } catch (SQLException ex) {
            Logger.getLogger(get_Users.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(get_Users.class.getName()).log(Level.SEVERE, null, ex);
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
        processRequest(request, response);
    }
}
