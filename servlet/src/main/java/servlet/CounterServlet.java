package servlet;

import dao.ICounterDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = "/counter")
public class CounterServlet extends HttpServlet {

    private final ICounterDAO counterDAO;

    public CounterServlet(ICounterDAO counterDAO) {
        this.counterDAO = counterDAO;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();
        writer.print(counterDAO.getCounter());
        writer.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        counterDAO.incrementCounter();
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int subtractionValue = Integer.parseInt(request.getHeader("Subtraction-Value"));
            counterDAO.subtractCounter(subtractionValue);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
        }
    }
}
