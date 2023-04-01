import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

public class JerseyApplication extends ResourceConfig {

    public JerseyApplication() {
        packages("resource");
    }

    private static Server createServer(int port) {
        Server server = new Server(port);

///////// 1 way
//        ServletContextHandler handler = new ServletContextHandler();
//        server.setHandler(handler);
//
//        ServletHolder servletHolder = handler.addServlet(ServletContainer.class, "/*");
//        servletHolder.setInitOrder(0);
//        servletHolder.setInitParameter("jersey.config.server.provider.packages", "resource");
////////// end of 1 way

////////// 2 way
//        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
//        server.setHandler(handler);
//        handler.setContextPath("/");
//
//        ResourceConfig resourceConfig = new ResourceConfig();
//        resourceConfig.register(StatusResource.class);
//        resourceConfig.register(CounterResource.class);
//
//        handler.addServlet(new ServletHolder(new ServletContainer(resourceConfig)), "/*");
///////// end of 2 way

        ServletContextHandler servletContextHandler = new ServletContextHandler();
        ServletHolder servletHolder = servletContextHandler.addServlet(ServletContainer.class, "/*");
        servletHolder.setInitParameter("jakarta.ws.rs.Application", JerseyApplication.class.getName());

        server.setHandler(servletContextHandler);

        return server;
    }

    public static void main(String[] args) throws Exception {

        int port = 8081;
        Server server = createServer(port);
        server.start();
        server.join();
    }
}
