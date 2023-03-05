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
            writer.print(CounterDAO.getInstance().getCounter());
            writer.flush();
        }
        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            CounterDAO.getInstance().incrementCounter();
        }

        @Override
        protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            int subtractionValue = Integer.parseInt(request.getHeader("Subtraction-Value"));
            CounterDAO.getInstance().subtractCounter(subtractionValue);
        }
    }
