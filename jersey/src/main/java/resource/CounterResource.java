package resource;

import dao.CounterDAO;
import dao.ICounterDAO;
import jakarta.inject.Singleton;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Singleton
@Path("/counter")
public class CounterResource {

    private final ICounterDAO counterDAO = CounterDAO.getInstance();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get() {
        int value = counterDAO.getCounter();
        String date = DateTimeFormatter.ISO_INSTANT.format(Instant.now());
        CounterDTO counterDTO = new CounterDTO(value, date);
        return Response.ok().entity(counterDTO).build();
    }

    @POST
    public Response post() {
        counterDAO.incrementCounter();
        return Response.ok().build();
    }

    @DELETE
    public Response delete(@HeaderParam("Subtraction-Value") String subtractionValue) {
        try {
            counterDAO.subtractCounter(Integer.parseInt(subtractionValue));
        } catch (NumberFormatException e) {
            Response.status(HttpServletResponse.SC_PRECONDITION_FAILED);
        }
        return Response.ok().build();
    }

    @POST
    @Path("/clear")
    public Response post(@CookieParam("hh-auth") String hhAuthCookie) {
        Optional<String> hhAuthCookieValue = Optional.ofNullable(hhAuthCookie);

        if (hhAuthCookieValue.isEmpty() || hhAuthCookieValue.get().length() <= 10) {
            return Response.status(HttpServletResponse.SC_UNAUTHORIZED).build();
        } else {
            counterDAO.clearCounter();
            return Response.ok().build();
        }
    }

}
