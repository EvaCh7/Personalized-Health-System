/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest_api;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;

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
     * @return an instance of java.lang.String
     */
    @Path("/json")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
       return "lalalala";
    }

    /**
     * PUT method for updating or creating an instance of Randevouz
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }
}
