import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

@WebServlet(urlPatterns = "/counter/clear")
public class CounterClearServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //TODO: simplify the code if it is possible
        boolean clearCounterIsSuccessful = false;

        if (request.getCookies() != null) {

            Optional<Cookie> cookie =
                    Stream.of(request.getCookies())
                            .filter(cookieVal -> cookieVal.getName().equals("hh-auth"))
                            .findFirst();

            if (cookie.isPresent()) {

                Optional<String> cookieValue =
                        Optional.ofNullable(cookie.get().getValue());

                if (cookieValue.isPresent() && cookieValue.get().length() > 10) {
                    CounterDAO.getInstance().clearCounter();
                    clearCounterIsSuccessful = true;
                }
            }
        }

        if (!clearCounterIsSuccessful) {
            setPreconditionFailedResponseStatus(response);
        }
    }

    private void setPreconditionFailedResponseStatus(HttpServletResponse response) {
        response.setStatus(412);
    }
}
