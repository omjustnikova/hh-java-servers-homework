import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

@WebServlet(urlPatterns = "/counter/clear")
public class CounterClearServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean clearCounterIsSuccessful = false;
        Optional<Cookie> hhAuthCookie = Optional.ofNullable(request.getCookies())
                 .stream()
                 .flatMap(Arrays::stream)
                 .filter(cookie -> Objects.equals("hh-auth", cookie.getName()))
                 .findFirst();

        if (hhAuthCookie.isPresent()) {
            Optional<String> cookieValue =
                    Optional.ofNullable(hhAuthCookie.get().getValue());

            if (cookieValue.isPresent() && cookieValue.get().length() > 10) {
                CounterDAO.getInstance().clearCounter();
                clearCounterIsSuccessful = true;
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
