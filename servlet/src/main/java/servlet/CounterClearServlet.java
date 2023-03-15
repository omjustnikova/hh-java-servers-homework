package servlet;

import dao.ICounterDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.*;

@WebServlet(urlPatterns = "/counter/clear")
public class CounterClearServlet extends HttpServlet {

    private final ICounterDAO counterDAO;

    public CounterClearServlet(ICounterDAO counterDAO) {
        this.counterDAO = counterDAO;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Optional<String> hhAuthCookieValue = Optional.ofNullable(request) // NPE in case request is null was fixed
                .flatMap(requestObj -> Optional.ofNullable(requestObj.getCookies())) // NPE in case getCookies() is null was fixed
                .stream() // Stream<Cookie[]>
                .flatMap(Arrays::stream) // Stream<Cookie>
                .filter(cookie -> Objects.equals("hh-auth", cookie.getName())) // cookie.getName().equals("hh-auth") can throw NPE in case getName is null
                .findFirst() // in case findFirst is the first there is no NPE, otherwise(map is the first) it is 
                .map(Cookie::getValue);


        if (hhAuthCookieValue.isEmpty() || hhAuthCookieValue.get().length() <= 10) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        counterDAO.clearCounter();
    }
}
