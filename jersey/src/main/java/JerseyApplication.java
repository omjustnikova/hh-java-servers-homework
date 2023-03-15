import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import resource.CounterResource;
import resource.StatusResource;

public class JerseyApplication {

    private static Server createServer(int port) {
        Server server = new Server(port);

        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        server.setHandler(handler);
        handler.setContextPath("/");

        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.register(StatusResource.class);
        resourceConfig.register(CounterResource.class);

        handler.addServlet(new ServletHolder(new ServletContainer(resourceConfig)), "/*");

        return server;
    }

    public static void main(String[] args) throws Exception {

        int port = 8081;
        Server server = createServer(port);
        server.start();
        server.join();
    }
}
