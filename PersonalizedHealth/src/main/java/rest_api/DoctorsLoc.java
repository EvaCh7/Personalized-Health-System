/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest_api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import database.tables.EditDoctorTable;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mainClasses.Doctor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * REST Web Service
 *
 * @author ORamaVR Test PC
 */
@Path("docs")
public class DoctorsLoc {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of DoctorsLoc
     */
    public DoctorsLoc() {
    }

    public static double distance(double lat1,
            double lat2, double lon1,
            double lon2) {

        // The math module contains a function
        // named toRadians which converts from
        // degrees to radians.
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2), 2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers. Use 3956
        // for miles
        double r = 6371;

        // calculate the result
        return (c * r);
    }

    @GET
    @Path("/findDocLoc/{lat}/{lon}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response FindDocs(@PathParam("lat") double lat, @PathParam("lon") double lon)
            throws SQLException, ParseException, ClassNotFoundException {
        Response.Status status = Response.Status.OK;
        JSONArray resJson = new JSONArray();
        ArrayList<Doctor> res = EditDoctorTable.databaseToDoctorArray();

        if (res.isEmpty()) {
            return Response.status(Response.Status.FORBIDDEN).type("application/json").entity("{\"error\":\"Given amka doesn't exist\"}").build();
        }

        for (int i = 0; i < res.size(); i++) {
            String jsonElems = EditDoctorTable.doctorToJSON(res.get(i));
            JsonParser jsonParser = new JsonParser();
            JsonObject jo = (JsonObject) jsonParser.parse(jsonElems);
            double lat_doc = jo.get("lat").getAsDouble();
            double lon_doc = jo.get("lon").getAsDouble();
            double distance_calc = distance(lat, lat_doc, lon, lon_doc);

            JSONObject item = new JSONObject();
            item.put("Distance(in kilometers)", distance_calc);
            item.put("First Name", jo.get("firstname").getAsString());
            item.put("Last Name", jo.get("lastname").getAsString());
            item.put("Doctor ID", jo.get("doctor_id").getAsInt());

            resJson.add(item);
        }

        String json = new Gson().toJson(resJson);
        return Response.status(status).type("application/json").entity(json).build();
    }

    @GET
    @Path("/doctorsLatLon")
    @Produces(MediaType.APPLICATION_JSON)
    public Response doctorLatLon() throws SQLException, ParseException, ClassNotFoundException {
        Response.Status status = Response.Status.OK;
        JSONArray resJson = new JSONArray();
        ArrayList<Doctor> res = EditDoctorTable.databaseToDoctorArray();

        if (res.isEmpty()) {
            return Response.status(Response.Status.FORBIDDEN).type("application/json").entity("{\"error\":\"Given amka doesn't exist\"}").build();
        }

        for (int i = 0; i < res.size(); i++) {
            String jsonElems = EditDoctorTable.doctorToJSON(res.get(i));
            JsonParser jsonParser = new JsonParser();
            JsonObject jo = (JsonObject) jsonParser.parse(jsonElems);
            JSONObject item = new JSONObject();

            item.put("lat", jo.get("lat").getAsDouble());
            item.put("lon", jo.get("lon").getAsDouble());
            item.put("First Name", jo.get("firstname").getAsString());
            item.put("Last Name", jo.get("lastname").getAsString());
            item.put("Doctor ID", jo.get("doctor_id").getAsInt());

            resJson.add(item);
        }

        String json = new Gson().toJson(resJson);
        return Response.status(status).type("application/json").entity(json).build();
    }

    @PUT
    @Path("/Certify/{doctor_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCertifiedDoc(@PathParam("doctor_id") int doctor_id)
            throws SQLException, ClassNotFoundException {
        try {
            EditDoctorTable.certifyDoctor(doctor_id);
            return Response.status(Response.Status.OK).type("application/json").entity("{\"success\":\"Quantity Updated\"}").build();
        } catch (SQLException ex) {
            Logger.getLogger(Examinations.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Examinations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.status(Response.Status.NOT_FOUND).type("application/json").entity("{\"error\":\"Doctor Does not Exist\"}").build();

    }
}
