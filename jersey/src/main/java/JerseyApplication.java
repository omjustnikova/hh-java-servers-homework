import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import resource.CounterDTO;


public class JerseyApplication {

    private static Server createServer(int port) {
        Server server = new Server(port);

        ServletContextHandler handler = new ServletContextHandler();
        server.setHandler(handler);

        ServletHolder servletHolder = handler.addServlet(ServletContainer.class, "/*");
        servletHolder.setInitOrder(0);
        servletHolder.setInitParameter("jersey.config.server.provider.packages", "resource");

        return server;
    }

    public static void main(String[] args) throws Exception {

        int port = 8081;
        Server server = createServer(port);
        server.start();
        server.join();
    }
}
