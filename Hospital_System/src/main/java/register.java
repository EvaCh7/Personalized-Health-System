/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import database.tables.EditDoctorTable;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import database.tables.EditSimpleUserTable;
import java.io.BufferedReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import mainClasses.Utils;

/**
 *
 * @author kokol
 */
public class register extends HttpServlet {

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Utils utils = new Utils();
        String json_str = utils.getJSONFromAjax(request.getReader());
        System.out.println(json_str);
        JsonObject user_json = JsonParser.parseString(json_str).getAsJsonObject();
        JsonElement specialty = user_json.get("specialty");
        //json_str = eliminate_special_characters(json_str);
        System.out.println(json_str);
        String msg = "";
        if (specialty.isJsonNull()) {

            EditSimpleUserTable simple_user_obj = new EditSimpleUserTable();
            msg = "register completed succesfully";
            try {
                System.out.println(json_str);

                simple_user_obj.addSimpleUserFromJSON(json_str);

            } catch (ClassNotFoundException ex) {
                Logger.getLogger(register.class.getName()).log(Level.SEVERE, null, ex);
                response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);

            }
        } else {
            EditDoctorTable doctor_edit_obj = new EditDoctorTable();
            msg = "register completed but the admin need to certify you";

            try {
                doctor_edit_obj.addDoctorFromJSON(json_str);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(register.class.getName()).log(Level.SEVERE, null, ex);
                response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);

            }
        }
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(msg+"\n"+json_str);

    }

    private static boolean hasSpecialChars(String input) {
        boolean flag = false;
        if ((input != null) && (input.length() > 0)) {
            char c;
            for (int i = 0; i < input.length(); i++) {
                c = input.charAt(i);
                switch (c) {
                case '<':
                    flag = true;
                    break;
                case '>':
                    flag = true;
                    break;
                case '&':
                    flag = true;
                    break;
                }
            }
        }
        return (flag);
    }

    private static String eliminate_special_characters(String input) {
        if (!hasSpecialChars(input)) {
            return (input);
        }
        StringBuffer filtered = new StringBuffer(input.length());
        char c;
        for (int i = 0; i < input.length(); i++) {
            c = input.charAt(i);
            switch (c) {
            case '<':
                filtered.append("&lt;");
                break;
            case '>':
                filtered.append("&gt;");
                break;
            case '&':
                filtered.append("&amp;");
                break;
            default:
                filtered.append(c);
            }
        }
        return (filtered.toString());
    }

}
