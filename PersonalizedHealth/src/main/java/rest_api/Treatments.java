/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest_api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import database.tables.EditBloodTestTable;
import static database.tables.EditBloodTestTable.getAllBloodTestsById;
import database.tables.EditRandevouzTable;
import database.tables.EditTreatmentTable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mainClasses.BloodTest;
import mainClasses.Treatment;
import org.json.simple.JSONArray;

/**
 * REST Web Service
 *
 * @author kokol
 */
@Path("Treatments")
public class Treatments {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of Treatment
     */
    public Treatments() {
    }

    @POST
    @Path("/addTreatment")
    @Produces({MediaType.APPLICATION_JSON})
    public Response addTreatment(String json) {
        Response.Status status = Response.Status.BAD_GATEWAY;
        String response = "{\"response\": \"Didn't add Treatment\" }";
        EditTreatmentTable treat_table_utils = new EditTreatmentTable();
        Treatment treatment = treat_table_utils.jsonToTreatment(json);
        try {
            ArrayList<BloodTest> blood_tests = getAllBloodTestsById(treatment.getUser_id());
            if (blood_tests.size() > 0) {

                int blood_test_id = blood_tests.get(blood_tests.size() - 1).getBloodtest_id();
                treatment.setBloodtest_id(blood_test_id);
                System.out.println(treatment);
                treat_table_utils.createNewTreatment(treatment);
                response = "{\"response\": \"Treatment added succesfully\" }";
                return Response.ok().type("application/json").entity(response).build();
            }

        } catch (SQLException ex) {
            Logger.getLogger(Treatments.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Treatments.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.status(status).type("application/json").entity(response).build();

    }

    /**
     * Retrieves representation of an instance of rest_api.Treatments
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Path("/showTreatments/{amka}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response showTreatments(@PathParam("amka") String amka)
            throws SQLException, ClassNotFoundException {
        Response.Status status = Response.Status.OK;

        JSONArray resJson = new JSONArray();
        ArrayList<BloodTest> res = EditBloodTestTable.getAllBloodTests(amka);
        if (res.isEmpty()) {
            return Response.status(Response.Status.FORBIDDEN).type("application/json").entity("{\"error\":\"Given amka doesn't exist\"}").build();
        }

        for (int i = 0; i < res.size(); i++) {
            String jsonElems = EditBloodTestTable.bloodTestToJSON(res.get(i));
            JsonParser jsonParser = new JsonParser();
            JsonObject jo = (JsonObject) jsonParser.parse(jsonElems);
            int id = jo.get("bloodtest_id").getAsInt();
            System.out.println(id);
            Treatment tr = EditTreatmentTable.databaseToTreatmentBT(id);
            if (tr == null) {
                continue;
            } else {
                resJson.add(tr);
            }
        }

        String json = new Gson().toJson(resJson);
        return Response.status(status).type("application/json").entity(json).build();
    }

    @GET
    @Path("/showTreatmentsDone/{doctor_id}/{user_id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response showTreatments(@PathParam("doctor_id") int doctor_id, @PathParam("user_id") int user_id)
            throws SQLException, ClassNotFoundException {
        Response.Status status = Response.Status.OK;
        JsonArray doc_done_rad = EditRandevouzTable.getDoctosDoneRandevouz(doctor_id);
        boolean found = false;
        for (JsonElement js : doc_done_rad) {
            JsonObject obj = js.getAsJsonObject();
            if (obj.get("user_id").getAsInt() == user_id) {
                found = true;
                break;
            }
        }
        if (!found) {
            return Response.status(Response.Status.FORBIDDEN).type("application/json").entity("{\"error\":\"given ID hasn't any done randevouz\"}").build();

        }
        JSONArray resJson = new JSONArray();
        ArrayList<BloodTest> res = EditBloodTestTable.getAllBloodTestsById(user_id);
        if (res.isEmpty()) {
            return Response.status(Response.Status.FORBIDDEN).type("application/json").entity("{\"error\":\"Given ID doesn't have treatments\"}").build();
        }

        for (int i = 0; i < res.size(); i++) {
            String jsonElems = EditBloodTestTable.bloodTestToJSON(res.get(i));
            JsonParser jsonParser = new JsonParser();
            JsonObject jo = (JsonObject) jsonParser.parse(jsonElems);
            int id = jo.get("bloodtest_id").getAsInt();
            System.out.println(id);
            Treatment tr = EditTreatmentTable.databaseToTreatmentBT(id);
            if (tr == null) {
                continue;
            } else {
                resJson.add(tr);
            }
        }

        String json = new Gson().toJson(resJson);
        return Response.status(status).type("application/json").entity(json).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of Treatments
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }
}
