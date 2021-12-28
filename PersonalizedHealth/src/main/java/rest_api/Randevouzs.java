/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest_api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import database.tables.EditRandevouzTable;
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

    /**
     * Retrieves representation of an instance of rest_api.Randevouz
     *
     * @return an instance of java.lang.String
     */
    @Path("/json")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        return "lalalala";
    }

    public static boolean isFutureDate(String pDateString) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(pDateString);
            return new Date().before(date);
        } catch (ParseException ex) {
            Logger.getLogger(Randevouz.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    private String getDate(String date_time) {
        String[] str = date_time.split(" ");
        return str[0];

    }

    private String getTime(String date_time) {
        String[] str = date_time.split(" ");
        return str[1];

    }

    public boolean isTimeBetween(String time, String from_time, String to_time) {
        LocalTime from = LocalTime.parse(from_time);
        LocalTime to = LocalTime.parse(to_time);
        LocalTime _time = LocalTime.parse(time);
        if (_time.isAfter(from) && _time.isBefore(to)) {
            return true;

        }
        return false;

    }

    private boolean isRandevouzMoreThan30Minutes(String time) {
        LocalTime _time = LocalTime.parse(time);
        return false;
    }

    @Path("/addRandevouz/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addRandevouz(String json) {

        String response = "{\"response\": \"error didn't add new randevouz\" }";
        Response.Status status;
        status = Response.Status.BAD_REQUEST;
        Gson gson = new Gson();
        JsonObject js = gson.fromJson(json, JsonObject.class);
        String date_time = js.get("date_time").getAsString();
        String date = getDate(date_time);
        String _time = getTime(date_time);

        if (!isFutureDate(date)) {
            response = "{\"response\": \"error invalid date, not from future\" }";
            return Response.status(status).type("application/json").entity(response).build();

        }
        if (!isTimeBetween(_time, "08:00:00", "20:30:01")) {
            response = "{\"response\": \"error invalid time, time must be between 08:00:00 && 20:30:00  \" }";
            return Response.status(status).type("application/json").entity(response).build();

        }
        try {
            LocalTime time1 = LocalTime.parse(_time);

            // Calculating the difference in milliseconds
            ArrayList<Randevouz> randev_list = EditRandevouzTable.getAllrandevouz();

            for (Randevouz randev : randev_list) {
                LocalTime time2 = LocalTime.parse(getTime(randev.getDate_time()));
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

    @Path("/getRandevouz/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRandevouz(
            @PathParam("id") int id) {

        String response = "{\"response\": \"error invalid ID or Server error\" }";
        Response.Status status;

        try {
            status = Response.Status.ACCEPTED;
            JsonArray array = EditRandevouzTable.getDoctosrandevouz(id);
            return Response.status(status).type("application/json").entity(array.toString()).build();
        } catch (SQLException ex) {
            Logger.getLogger(Randevouzs.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Randevouzs.class.getName()).log(Level.SEVERE, null, ex);
        }
        status = Response.Status.BAD_REQUEST;

        return Response.status(status).type("application/json").entity(response).build();
    }

    @Path("/cancelRandevouz/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)

    public Response CancelRandevouz(
            @PathParam("id") int id) {
        String response = "{\"response\": \"error cancel wasn't succesful\" }";
        Response.Status status;
        EditRandevouzTable rand_utils = new EditRandevouzTable();
        try {

            if (rand_utils.cancelRandevouz(id)) {
                response = "{\"response\": \"Randevouz cancelled succesfuly\" }";
                status = Response.Status.OK;
                return Response.status(status).type("application/json").entity(response).build();

            }

        } catch (SQLException ex) {
            Logger.getLogger(Randevouz.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Randevouz.class.getName()).log(Level.SEVERE, null, ex);
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
    public void putJson(String content) {
    }
}
