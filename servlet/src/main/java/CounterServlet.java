import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = "/counter")
public class CounterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();
        writer.print("<!DOCTYPE html><html lang='en'><head><meta charset='UTF-8'><title>Increment Counter</title></head><body>Counter: " + CounterDAO.getInstance().getCounter() + "<form method='POST'><input type='submit' value='Increment Counter'/></form></body></html>");
        writer.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CounterDAO.getInstance().incrementCounter();
        response.sendRedirect("/counter");
    }

}
