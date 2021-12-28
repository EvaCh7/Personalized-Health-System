/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest_api;

import database.tables.EditRandevouzTable;
import java.sql.SQLException;
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

/**
 * REST Web Service
 *
 * @author kokol
 */
@Path("randevouz")
public class Randevouz {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of Randevouz
     */
    public Randevouz() {
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

    @Path("/addRandevouz/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addRandevouz(String json) {

        String response = "{\"response\": \"error didn't add new randevouz\" }";
        Response.Status status;
        status = Response.Status.BAD_REQUEST;

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

    @Path("/cancelRandevouz/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response CancelRandevouz(
            @PathParam("id") int id) {
        String response = "{\"response\": \"error cancel wasn't succesful\" }";
        Response.Status status;
        EditRandevouzTable rand_utils = new EditRandevouzTable();
        try {

            if (rand_utils.deleteRandevouz(id)) {
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
