package uk.gov.homeoffice.toolkit.server.endpoint;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/version")
public class Version {

    @GET
    @Path("/")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "v1";
    }
}
