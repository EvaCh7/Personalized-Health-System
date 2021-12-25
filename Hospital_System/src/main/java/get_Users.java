
import com.google.gson.Gson;
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
            JSONArray resJson = new JSONArray();
            ArrayList<Doctor> res_doc = EditDoctorTable.databaseToDoctors();
            ArrayList<SimpleUser> res_user = EditSimpleUserTable.databaseToUser();

            Gson gson = new Gson();

            for (int i = 0; i < res_doc.size(); i++) {
                String json_doc = EditDoctorTable.doctorToJSON(res_doc.get(i));
                resJson.add(json_doc);
            }
            for (int i = 0; i < res_user.size(); i++) {
                String json_user = EditSimpleUserTable.simpleUserToJSON(res_user.get(i));
                resJson.add(json_user);
            }
            
            String array = resJson.toString();
//            array = array.replace("\\", "");
            response.getWriter().write(array);
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
