package uk.gov.homeoffice.toolkit.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.eclipse.jetty.servlet.ServletContextHandler.NO_SESSIONS;


public class StampServer {

    private static final Logger logger = LoggerFactory.getLogger(StampServer.class);

    public static void main(String[] args) throws Exception {
        Server server = new Server(8081);
        ServletContextHandler servletContextHandler = new ServletContextHandler(NO_SESSIONS);
        servletContextHandler.setContextPath("/");
        server.setHandler(servletContextHandler);
        ServletHolder servletHolder = servletContextHandler.addServlet(ServletContainer.class, "/*");
        servletHolder.setInitOrder(0);
        servletHolder.setInitParameter(
                "jersey.config.server.provider.packages",
                "uk.gov.homeoffice.toolkit.server.endpoint"
        );

        try {
            server.start();
            server.join();
        } catch (Exception e) {
            logger.error("StampServer error", e);
            throw e;
        } finally {
            server.destroy();
        }
    }
}
