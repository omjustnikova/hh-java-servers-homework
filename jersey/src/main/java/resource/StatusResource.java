package resource;

import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Singleton
@Path("/status")
public class StatusResource {

    @GET
    public Response get() {
        return Response.ok().build();
    }

}
