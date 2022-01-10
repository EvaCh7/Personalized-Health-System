/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest_api;

import Utils.UtilsDate;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import database.tables.EditBloodTestTable;
import database.tables.EditDoctorTable;
import database.tables.EditMessageTable;
import database.tables.EditRandevouzTable;
import database.tables.EditSimpleUserTable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mainClasses.Randevouz;
import java.util.*;
import java.util.stream.Stream;
import mainClasses.Doctor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import static rest_api.DoctorsLoc.distance;

/**
 * REST Web Service
 *
 * @author kokol
 */
@Path("randevouz")
public class Randevouzs {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of Randevouz
     */
    public Randevouzs() {
    }

    private boolean isRandevouzMoreThan30Minutes(String time) {
        LocalTime _time = LocalTime.parse(time);
        return false;
    }

    @Path("/reserveRandevouz/{randevouz_id}/{user_id}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response reserveRandevouz(@PathParam("randevouz_id") int randevouz_id, @PathParam("user_id") int user_id) throws SQLException, ClassNotFoundException {
        Response.Status status = Response.Status.BAD_REQUEST;
        String response = "{\"response\": \"Error: Randevouz wasn't reserved.\" }";

        try {
            EditRandevouzTable.reserveRandevouz(randevouz_id, user_id);
            status = Response.Status.OK;
            response = "{\"response\": \"Randevouz was reserved succesfully.\" }";

        } catch (SQLException ex) {
            Logger.getLogger(Randevouzs.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Randevouzs.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Response.status(status).type("application/json").entity(response).build();
    }

    @Path("/showAllRandevouz/{username}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response showRandevouz(@PathParam("username") String username) throws SQLException, ClassNotFoundException {
        JSONArray resJson = new JSONArray();
        Response.Status status = Response.Status.OK;

        int id = EditSimpleUserTable.getIDfromUsername(username);

        if (id == 0) {
            return Response.status(Response.Status.BAD_GATEWAY).type("application/json").entity("{\"error\":\"Given username doesn't exist\"}").build();
        }

        ArrayList<Randevouz> res = EditRandevouzTable.showRandevouzOfID(id);

        if (res.isEmpty()) {
            return Response.status(Response.Status.BAD_GATEWAY).type("application/json").entity("{\"error\":\"Given amka doesn't exist\"}").build();
        }

        for (int i = 0; i < res.size(); i++) {
            String jsonElems = EditRandevouzTable.randevouzToJSON(res.get(i));
            JsonParser jsonParser = new JsonParser();
            JsonObject jo = (JsonObject) jsonParser.parse(jsonElems);
            resJson.add(jo);
        }

        String json = new Gson().toJson(resJson);
        return Response.status(status).type("application/json").entity(json).build();
    }

    @Path("/updateRandevouz")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateRandevouz(String json) {

        String response = "{\"response\": \"error didn't add new randevouz\" }";
        Response.Status status;
        status = Response.Status.BAD_REQUEST;

        Gson gson = new Gson();
        JsonArray js_array = gson.fromJson(json, JsonArray.class);
        EditRandevouzTable rand_obj = new EditRandevouzTable();

        for (JsonElement jso : js_array) {
            JsonObject js = jso.getAsJsonObject();
            try {
                System.out.println("randevouz: " + js.get("randevouz_id").getAsInt() + " user id: " + js.get("user_id").getAsInt() + js.get("user_info").getAsString());
                System.out.println(js.get("status").getAsString());
                rand_obj.updateRandevouz(js.get("randevouz_id").getAsInt(), js.get("user_id").getAsInt(), js.get("user_info").getAsString(), js.get("status").getAsString());
                response = "{\"response\": \"randevouzs updated succesfully\" }";

                status = Response.Status.OK;

            } catch (SQLException ex) {
                Logger.getLogger(Randevouzs.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Randevouzs.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return Response.status(status).type("application/json").entity(response).build();

    }

    @Path("/addRandevouz/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addRandevouz(String json
    ) {

        String response = "{\"response\": \"error didn't add new randevouz\" }";
        Response.Status status;
        status = Response.Status.BAD_REQUEST;
        Gson gson = new Gson();
        JsonObject js = gson.fromJson(json, JsonObject.class);
        String date_time = js.get("date_time").getAsString();
        String date = UtilsDate.getDate(date_time);
        String _time = UtilsDate.getTime(date_time);

        if (!UtilsDate.isFutureDate(date)) {
            response = "{\"response\": \"error invalid date, not from future\" }";
            return Response.status(status).type("application/json").entity(response).build();

        }
        if (!UtilsDate.isTimeBetween(_time, "08:00:00", "20:30:01")) {
            response = "{\"response\": \"error invalid time, time must be between 08:00:00 && 20:30:00  \" }";
            return Response.status(status).type("application/json").entity(response).build();

        }
        try {
            LocalTime time1 = LocalTime.parse(_time);

            // Calculating the difference in milliseconds
            ArrayList<Randevouz> randev_list = EditRandevouzTable.getAllrandevouz();

            for (Randevouz randev : randev_list) {
                LocalTime time2 = LocalTime.parse(UtilsDate.getTime(randev.getDate_time()));
                long hours = ChronoUnit.HOURS.between(time1, time2);
                long minutes = ChronoUnit.MINUTES.between(time1, time2) % 60;
                if (hours == 0 && minutes < 30) {
                    response = "{\"response\": \"error invalid time, each randevouz must have 30 minutes difference  \" }";
                    return Response.status(status).type("application/json").entity(response).build();
                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(Randevouz.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Randevouz.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (js.get("price").getAsInt() < 10 || js.get("price").getAsInt() > 80) {
            response = "{\"response\": \"error price must be between 10 and 80 \" }";
            return Response.status(status).type("application/json").entity(response).build();
        }
        try {
            EditRandevouzTable rand_utils = new EditRandevouzTable();
            if (rand_utils.addRandevouzFromJSON(json)) {

                status = Response.Status.OK;
                response = "{\"response\": \"The randevouz was successfully added in the database\" }";
            }

            return Response.status(status).type("application/json").entity(response).build();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Randevouz.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.status(status).type("application/json").entity(response).build();

    }

    public static boolean hasUserAndDocADoneRandevouz(int doctor_id, int user_id) {
        try {
            JsonArray array = EditRandevouzTable.getDoctosDoneRandevouz(doctor_id);
            for (JsonElement js : array) {
                JsonObject json = js.getAsJsonObject();
                if (json.get("user_id").getAsInt() == user_id) {
                    return true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Randevouzs.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Randevouzs.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    @Path("/getRandevouz/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRandevouz(
            @PathParam("id") int id,
            @QueryParam("date") String selected_date
    ) {

        String response = "{\"response\": \"error invalid ID or Server error\" }";
        Response.Status status;
        if (selected_date == null) {

            try {
                status = Response.Status.OK;
                JsonArray array = EditRandevouzTable.getDoctosrandevouz(id);

                return Response.status(status).type("application/json").entity(array.toString()).build();
            } catch (SQLException ex) {
                Logger.getLogger(Randevouzs.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Randevouzs.class.getName()).log(Level.SEVERE, null, ex);
            }
            status = Response.Status.BAD_REQUEST;

            return Response.status(status).type("application/json").entity(response).build();
        } else { //date given as input

            try {
                status = Response.Status.OK;
                JsonArray array = EditRandevouzTable.getDoctosrandevouz(id);
                JsonArray array_return = new JsonArray();
                for (JsonElement js : array) {
                    JsonObject _js = js.getAsJsonObject();
                    if (UtilsDate.getDate(_js.get("date_time").getAsString()).equals(selected_date)) {
                        array_return.add(_js);
                    }
                }

                return Response.status(status).type("application/json").entity(array_return.toString()).build();
            } catch (SQLException ex) {
                Logger.getLogger(Randevouzs.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Randevouzs.class.getName()).log(Level.SEVERE, null, ex);
            }
            status = Response.Status.BAD_REQUEST;

            return Response.status(status).type("application/json").entity(response).build();
        }

    }

    @Path("/getBloodDonationRandevouz/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBloodDonationRandevouz(@PathParam("id") int id)
            throws ParseException {
        String response = "{\"response\": \"error invalid ID or Server error\" }";
        Response.Status status;
        try {
            JsonArray blood_donation_messages = EditMessageTable.GetBloodDonationMessages(id);
            return Response.ok().type("application/json").entity(blood_donation_messages.toString()).build();

        } catch (SQLException ex) {
            Logger.getLogger(Randevouzs.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Randevouzs.class.getName()).log(Level.SEVERE, null, ex);
        }
        status = Response.Status.BAD_GATEWAY;

        return Response.status(status).type("application/json").entity(response).build();
    }

    @Path("/getRandevouzNotification/{user_id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRandevouzNotification(@PathParam("user_id") int user_id)
            throws ParseException, SQLException, ClassNotFoundException {
        ArrayList<Randevouz> res = EditRandevouzTable.getDateOfRandevouz(user_id);

        String response = "{\"response\": \"error invalid ID or Server error\" }";
        Response.Status status;

        for (int i = 0; i < res.size(); i++) {
            String jsonElems = EditRandevouzTable.randevouzToJSON(res.get(i));
            JsonParser jsonParser = new JsonParser();
            JsonObject jo = (JsonObject) jsonParser.parse(jsonElems);
            JSONObject item = new JSONObject();

            String date = jo.get("date_time").getAsString();
            System.out.println(date);

            if (UtilsDate.is4Hours(date)) {
                System.out.println("edw eimaste");

                String json = new Gson().toJson(res);
                System.out.println("res: "+json);
                return Response.ok().type("application/json").entity(json).build();
            } else {
                continue;
            }
        }
        status = Response.Status.BAD_GATEWAY;

        return Response.status(status).type("application/json").entity(response).build();
    }

    private void addTableData(PdfPTable table, JsonArray array) {
        String str = "";
        for (int i = 0; i < array.size(); ++i) {

            Iterator<String> iterator = array.get(i).getAsJsonObject().keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                str = array.get(i).getAsJsonObject().get(key).getAsString();
                PdfPCell header = new PdfPCell();
                header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                header.setBorderWidth(2);
                header.setPhrase(new Phrase(str));
                table.addCell(header);

            }
        }
    }

    private void addTableHeader(PdfPTable table, JsonArray array) {
        String str = "";
        Iterator<String> iterator = array.get(0).getAsJsonObject().keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            str = key;
            PdfPCell header = new PdfPCell();
            header.setBackgroundColor(BaseColor.LIGHT_GRAY);
            header.setBorderWidth(2);
            header.setPhrase(new Phrase(str));
            table.addCell(header);

        }

    }

    private Document JsonArrayToPDF(JsonArray array, String fileName) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Randevouzs.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException ex) {
            Logger.getLogger(Randevouzs.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
        PdfPTable table = new PdfPTable(array.get(0).getAsJsonObject().size());
        addTableHeader(table, array);
        addTableData(table, array);

        try {
            document.add(table);
        } catch (DocumentException ex) {
            Logger.getLogger(Randevouzs.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        document.close();
        return document;

    }

    @GET
    @Path("/rankedRandevouz")
    @Produces(MediaType.APPLICATION_JSON)
    public Response rankedRandevouz()
            throws SQLException, ParseException, ClassNotFoundException {
        Response.Status status = Response.Status.OK;

        JSONArray resJson = new JSONArray();
        ArrayList<Randevouz> res = EditRandevouzTable.databaseToRandevouzArray();

        if (res.isEmpty()) {
            return Response.status(Response.Status.FORBIDDEN).type("application/json").entity("{\"error\":\"Given amka doesn't exist\"}").build();
        }

        for (int i = 0; i < res.size(); i++) {
            String jsonElems = EditRandevouzTable.randevouzToJSON(res.get(i));
            JsonParser jsonParser = new JsonParser();
            JsonObject jo = (JsonObject) jsonParser.parse(jsonElems);
            JSONObject item = new JSONObject();
            Doctor doctorInfo = EditDoctorTable.getDoctorInfo(jo.get("doctor_id").getAsInt());
            String name = EditDoctorTable.doctorToJSON(doctorInfo);
            JsonObject jo_name = (JsonObject) jsonParser.parse(name);

            System.out.println(name);
            item.put("First name", jo_name.get("firstname").getAsString());
            item.put("Last name", jo_name.get("lastname").getAsString());
            item.put("Price", jo.get("price").getAsInt());
            item.put("Doctor Info", jo.get("doctor_info").getAsString());
            item.put("Doctor ID", jo.get("doctor_id").getAsInt());

            resJson.add(item);
        }

        String json = new Gson().toJson(resJson);
        return Response.status(status).type("application/json").entity(json).build();
    }

    @Path("/getRandevouzPDF/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRandevouzPDF(
            @PathParam("id") int id,
            @QueryParam("date") String selected_date
    ) {

        String response = "{\"response\": \"error invalid ID or Server error\" }";
        Response.Status status;
        if (selected_date == null) {

            try {
                status = Response.Status.OK;
                JsonArray array = EditRandevouzTable.getDoctosrandevouz(id);
                Document document = JsonArrayToPDF(array, "C:\\Users\\kokol\\Documents\\randevouz.pdf");
                File file = new File("C:\\Users\\kokol\\Documents\\randevouz.pdf");
                return Response.ok(file, MediaType.APPLICATION_OCTET_STREAM)
                        .header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"") //optional
                        .build();

            } catch (SQLException ex) {
                Logger.getLogger(Randevouzs.class
                        .getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Randevouzs.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
            status = Response.Status.BAD_REQUEST;

            return Response.status(status).type("application/json").entity(response).build();
        } else { //date given as input

            try {
                status = Response.Status.OK;
                JsonArray array = EditRandevouzTable.getDoctosrandevouz(id);
                JsonArray array_return = new JsonArray();
                for (JsonElement js : array) {
                    JsonObject _js = js.getAsJsonObject();
                    if (UtilsDate.getDate(_js.get("date_time").getAsString()).equals(selected_date)) {
                        array_return.add(_js);
                    }
                }
                Document document = JsonArrayToPDF(array_return, "C:\\Users\\kokol\\Documents\\randevouz.pdf");
                File file = new File("C:\\Users\\kokol\\Documents\\randevouz.pdf");
                return Response.ok(file, MediaType.APPLICATION_OCTET_STREAM)
                        .header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"") //optional
                        .build();
            } catch (SQLException ex) {
                Logger.getLogger(Randevouzs.class
                        .getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Randevouzs.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
            status = Response.Status.BAD_REQUEST;

            return Response.status(status).type("application/json").entity(response).build();
        }

    }

    @Path("/getDoneRandevouz/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDoneRandevouz(
            @PathParam("id") int id
    ) {
        String response = "{\"response\": \"error couldn't get done randevouz\" }";
        Response.Status status;
        status = Response.Status.BAD_REQUEST;

        try {
            status = Response.Status.OK;

            JsonArray array = EditRandevouzTable.getDoctosDoneRandevouz(id);
            return Response.status(status).type("application/json").entity(array.toString()).build();

        } catch (SQLException ex) {
            Logger.getLogger(Randevouzs.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Randevouzs.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return Response.status(status).type("application/json").entity(response).build();

    }

    @Path("/cancelRandevouz/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)

    public Response CancelRandevouz(
            @PathParam("id") int id
    ) {
        String response = "{\"response\": \"error cancel wasn't succesful\" }";
        Response.Status status;
        EditRandevouzTable rand_utils = new EditRandevouzTable();
        try {

            if (rand_utils.cancelRandevouz(id)) {
                response = "{\"response\": \"Randevouz cancelled successfully\" }";
                status = Response.Status.OK;
                return Response.status(status).type("application/json").entity(response).build();

            }

        } catch (SQLException ex) {
            Logger.getLogger(Randevouz.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Randevouz.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        status = Response.Status.BAD_REQUEST;

        return Response.status(status).type("application/json").entity(response).build();

    }

    /**
     * PUT method for updating or creating an instance of Randevouz
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content
    ) {
    }
}
