    import jakarta.servlet.ServletException;
    import jakarta.servlet.annotation.WebServlet;
    import jakarta.servlet.http.Cookie;
    import jakarta.servlet.http.HttpServlet;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import java.io.IOException;
    import java.util.Optional;
    import java.util.stream.Stream;

    @WebServlet(urlPatterns = "/counter/clear")
    public class CounterClearServlet extends HttpServlet {

        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            // TODO: need refactoring, but I don't know how to do it
            if (request.getCookies() == null) {
                response.setStatus(412);
            } else {
                Optional<Cookie> authCookie = Stream.of(request.getCookies())
                        .filter(cookie -> cookie.getName().equals("hh-auth"))
                        .findFirst();
                if (authCookie.isPresent() && authCookie.get().getValue() != null && authCookie.get().getValue().length() > 10) {
                    CounterDAO.getInstance().clearCounter();
                } else {
                    // Precondition Failed
                    response.setStatus(412);
                }
            }
        }
    }
