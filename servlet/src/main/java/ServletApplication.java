  import org.eclipse.jetty.server.Server;
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
  }
