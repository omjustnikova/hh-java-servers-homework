import dao.CounterDAO;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlet.CounterClearServlet;
import servlet.CounterServlet;
import servlet.StatusServlet;

public class ServletApplication {

    private static Server createServer(int port) {
        Server server = new Server(port);
        CounterDAO counterDAO = CounterDAO.getInstance();
        ServletContextHandler handler = new ServletContextHandler(server, "/");

        handler.addServlet(StatusServlet.class, "/status");

        CounterServlet counterServlet = new CounterServlet(counterDAO);
        handler.addServlet(new ServletHolder(counterServlet), "/counter");

        CounterClearServlet counterClearServlet = new CounterClearServlet(counterDAO);
        handler.addServlet(new ServletHolder(counterClearServlet), "/counter/clear");

        server.setHandler(handler);
        return server;
    }

    public static void main(String[] args) throws Exception {
        int port = 8081;
        Server server = createServer(port);
        server.start();
        server.join();
    }
}

/**  import org.eclipse.jetty.server.Server;
  import org.eclipse.jetty.servlet.ServletContextHandler;

  public class ServletApplication {

    private static Server createServer(int port) {
      Server server = new Server(port);
      ServletContextHandler handler = new ServletContextHandler(server, "/");
      handler.addServlet(StatusServlet.class, "/status");
      handler.addServlet(CounterServlet.class, "/counter");
      handler.addServlet(CounterClearServlet.class, "/counter/clear");
      server.setHandler(handler);
      return server;
    }

    public static void main(String[] args) throws Exception {
      int port = 8081;
      Server server = createServer(port);
      server.start();
      server.join();
    }
  }*/

