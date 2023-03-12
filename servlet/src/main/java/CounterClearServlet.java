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
        Optional<String> hhAuthCookieValue = Optional.ofNullable(request.getCookies())
                 .stream()
                 .flatMap(Arrays::stream)
                 .filter(cookie -> Objects.equals("hh-auth", cookie.getName()))
                 .map(Cookie::getValue)
                 .findFirst();

            if (hhAuthCookieValue.isEmpty() || hhAuthCookieValue.get().length() <= 10) {
                setPreconditionFailedResponseStatus(response);
                return;
            }

            CounterDAO.getInstance().clearCounter();
    }

    private void setPreconditionFailedResponseStatus(HttpServletResponse response) {
        response.setStatus(412);
    }
}
