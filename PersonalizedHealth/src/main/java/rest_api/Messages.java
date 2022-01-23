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
import database.tables.EditMessageTable;
import database.tables.EditRandevouzTable;
import database.tables.EditSimpleUserTable;
import java.sql.SQLException;
import java.util.ArrayList;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mainClasses.Message;
import mainClasses.Randevouz;
import mainClasses.SimpleUser;

/**
 * REST Web Service
 *
 * @author kokol
 */
@Path("Messages")
public class Messages {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of Messages
     */
    public Messages() {
    }

    /**
     * Retrieves representation of an instance of rest_api.Messages
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    @Path("/sendMessageToBloodDonors")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendMessageToBloodDonors(String json
    ) {
        String response = "{\"response\": \"error couldn't send the messages\" }";
        Response.Status status;
        status = Response.Status.BAD_GATEWAY;

        JsonObject js = new Gson().fromJson(json, JsonObject.class);
        String bloodType = js.get("bloodtype").getAsString();
        String message = js.get("message").getAsString();
        try {
            ArrayList<SimpleUser> users = new EditSimpleUserTable().databaseToUser();

            for (SimpleUser user : users) {
                System.out.println("use blood type: " + user.getBloodtype());
                if (user.getBloodtype() != null && user.getBloodtype().equals(bloodType) && user.getBlooddonor() == 1) {
                    Message msg = new Message();

                    msg.setBlood_donation(1);
                    msg.setBloodtype(bloodType);
                    msg.setDoctor_id(js.get("doctor_id").getAsInt());
                    msg.setMessage(message);
                    msg.setSender("doctor");
                    msg.setUser_id(user.getUser_id());
                    msg.setDate_time(js.get("date_time").getAsString());

                    new EditMessageTable().createNewMessage(msg);
                }
            }
            response = "{\"response\": \" Messages sent succesfully\" }";
            return Response.ok().type("application/json").entity(response).build();

        } catch (SQLException ex) {
            Logger.getLogger(Messages.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Messages.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.status(status).type("application/json").entity(response).build();

    }

    @Path("/AddUserAnswers")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response AddUserAnswers(String json
    ) {
        String response = "{\"response\": \"error There's not a succesful randevouz between this patient and doctor\" }";
        Response.Status status;
        status = Response.Status.BAD_GATEWAY;
        System.out.println(json);
        if (json == null || json.equals("null")) {
            response = "{\"response\": \"error: no new messages added\" }";
            return Response.status(status).type("application/json").entity(response).build();

        }
        JsonArray json_arr = new JsonArray();

        EditRandevouzTable rand_obj = new EditRandevouzTable();
        json_arr = new Gson().fromJson(json, JsonArray.class);
        JsonObject js_obj = json_arr.get(0).getAsJsonObject();
        if (!Randevouzs.hasUserAndDocADoneRandevouz(js_obj.get("doctor_id").getAsInt(), js_obj.get("user_id").getAsInt())) {
            status = Response.Status.BAD_GATEWAY;
            return Response.status(status).type("application/json").entity(response).build();

        }

        int i = 0;
        for (JsonElement js : json_arr) {
            try {
                if (!js.isJsonNull()) {
                    JsonObject json_obj = new Gson().fromJson(js, JsonObject.class);
                    String _js = json_obj.toString();
                    new EditMessageTable().addMessageFromJSON(_js);
                }

            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Messages.class.getName()).log(Level.SEVERE, null, ex);
            }
            ++i;
        }
        response = "{\"response\": \"Answers Added Succesfully\" }";
        return Response.ok().type("application/json").entity(response).build();

    }

    @Path("/AddDoctorAnswers")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response AddDoctorAnswers(String json
    ) {
        String response = "{\"response\": \"error There's not a succesful randevouz between this patient and doctor\" }";
        Response.Status status;
        status = Response.Status.BAD_GATEWAY;
        System.out.println(json);
        if (json == null || json.equals("null")) {
            response = "{\"response\": \"error: no new messages added\" }";
            return Response.status(status).type("application/json").entity(response).build();

        }
        JsonArray json_arr = new JsonArray();

        EditRandevouzTable rand_obj = new EditRandevouzTable();
        json_arr = new Gson().fromJson(json, JsonArray.class);
        JsonObject js_obj = json_arr.get(0).getAsJsonObject();
        if (!Randevouzs.hasUserAndDocADoneRandevouz(js_obj.get("doctor_id").getAsInt(), js_obj.get("user_id").getAsInt())) {
            status = Response.Status.BAD_GATEWAY;
            return Response.status(status).type("application/json").entity(response).build();

        }

        int i = 0;
        for (JsonElement js : json_arr) {
            try {
                if (!js.isJsonNull()) {
                    JsonObject json_obj = new Gson().fromJson(js, JsonObject.class);
                    String _js = json_obj.toString();
                    new EditMessageTable().addMessageFromJSON(_js);
                }

            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Messages.class.getName()).log(Level.SEVERE, null, ex);
            }
            ++i;
        }
        response = "{\"response\": \"Answers Added Succesfully\" }";
        return Response.ok().type("application/json").entity(response).build();

    }

    @Path("/UpdateDoctorAnswers")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response UpdateDoctorAnswers(String json
    ) {
        String response = "{\"response\": \"error There's not a succesful randevouz between this patient and doctor\" }";
        Response.Status status;
        status = Response.Status.BAD_GATEWAY;
        JsonArray json_arr = new JsonArray();

        EditRandevouzTable rand_obj = new EditRandevouzTable();
        json_arr = new Gson().fromJson(json, JsonArray.class);
        JsonObject js_obj = new Gson().fromJson(json_arr.get(0), JsonObject.class);

        if (!Randevouzs.hasUserAndDocADoneRandevouz(js_obj.get("doctor_id").getAsInt(), js_obj.get("user_id").getAsInt())) {
            status = Response.Status.BAD_GATEWAY;
            return Response.status(status).type("application/json").entity(response).build();

        }
        int i = 0;
        for (JsonElement js : json_arr) {
            JsonObject _js = new Gson().fromJson(json_arr.get(i), JsonObject.class);
            EditMessageTable.UpdateMessage(_js.get("message_id").getAsInt(), _js.get("doctor_id").getAsInt(), _js.get("user_id").getAsInt(), _js.get("sender").getAsString(), _js.get("blood_donation").getAsInt(), _js.get("bloodtype").getAsString(), _js.get("date_time").getAsString(), _js.get("message").getAsString());
            ++i;
        }
        response = "{\"response\": \"Answers Updated Succesfully\" }";
        return Response.status(status).type("application/json").entity(response).build();

    }

    /*
    
        EditRandevouzTable rand_obj = new EditRandevouzTable();
        JsonArray json_arr = new JsonArray();
        json_arr = new Gson().fromJson(json, JsonArray.class);
        JsonObject js_obj = new Gson().fromJson(json_arr.get(0), JsonObject.class);

        if (Randevouzs.hasUserAndDocADoneRandevouz(js_obj.get("doctor_id").getAsInt(), js_obj.get("user_id").getAsInt())) {
            status = Response.Status.BAD_GATEWAY;
            return Response.status(status).type("application/json").entity(response).build();

        }
        try {
            json_arr = EditMessageTable.SelectMessagesOfDoctorWithPatient(js_obj.get("doctor_id").getAsInt(), js_obj.get("user_id").getAsInt());
            return Response.ok().type("application/json").entity(json_arr.toString()).build();

     */
    @Path("/getDoctorMessages/{doctor_id}/{user_id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDoctorMessages(@PathParam("doctor_id") int doctor_id, @PathParam("user_id") int user_id
    ) {
        String response = "{\"response\": \"error There's not a succesful randevouz between this patient and doctor\" }";
        Response.Status status;
        status = Response.Status.BAD_GATEWAY;
        JsonArray json_arr = new JsonArray();
        if (!Randevouzs.hasUserAndDocADoneRandevouz(doctor_id, user_id)) {
            status = Response.Status.BAD_GATEWAY;
            return Response.status(status).type("application/json").entity(response).build();

        }
        try {
            json_arr = EditMessageTable.SelectMessagesOfDoctorWithPatient(user_id, doctor_id);
            return Response.ok().type("application/json").entity(json_arr.toString()).build();

        } catch (SQLException ex) {
            Logger.getLogger(Messages.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Messages.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.status(status).type("application/json").entity(response).build();

    }

    @Path("/getUserMessages/{user_id}/{doctor_id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserMessages(@PathParam("user_id") int user_id, @PathParam("doctor_id") int doctor_id
    ) {
        String response = "{\"response\": \"error There's not a succesful randevouz between this patient and doctor\" }";
        Response.Status status;
        status = Response.Status.BAD_GATEWAY;
        JsonArray json_arr = new JsonArray();
        if (!Randevouzs.hasUserAndDocADoneRandevouz(doctor_id, user_id)) {
            status = Response.Status.BAD_GATEWAY;
            return Response.status(status).type("application/json").entity(response).build();

        }
        try {
            json_arr = EditMessageTable.SelectMessagesOfDoctorWithPatient(user_id, doctor_id);

            return Response.ok().type("application/json").entity(json_arr.toString()).build();

        } catch (SQLException ex) {
            Logger.getLogger(Messages.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Messages.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.status(status).type("application/json").entity(response).build();

    }

    /**
     * PUT method for updating or creating an instance of Messages
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content
    ) {
    }
}
