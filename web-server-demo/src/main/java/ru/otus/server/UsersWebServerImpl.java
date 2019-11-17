package ru.otus.server;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.otus.dao.UserDao;
import ru.otus.services.TemplateProcessor;
import ru.otus.services.UserAuthService;
import ru.otus.servlet.AuthorizationFilter;
import ru.otus.servlet.LoginServlet;
import ru.otus.servlet.UsersApiServlet;
import ru.otus.servlet.UsersPageServlet;

import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class UsersWebServerImpl implements UsersWebServer {
    private final int port;
    private final UserAuthService userAuthService;
    private final UserDao userDao;
    private final Gson gson;
    private final TemplateProcessor templateProcessor;
    private final Server server;

    public UsersWebServerImpl(int port, UserAuthService userAuthService, UserDao userDao, Gson gson, TemplateProcessor templateProcessor) {
        this.port = port;
        this.userAuthService = userAuthService;
        this.userDao = userDao;
        this.gson = gson;
        this.templateProcessor = templateProcessor;
        server = initContext();
    }

    @Override
    public void start() throws Exception {
        server.start();
    }

    @Override
    public void join() throws Exception {
        server.join();
    }

    @Override
    public void stop() throws Exception {
        server.stop();
    }

    private Server initContext() {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        context.addServlet(new ServletHolder(new UsersPageServlet(templateProcessor, userDao)), "/users");
        context.addServlet(new ServletHolder(new UsersApiServlet(userDao, gson)), "/api/user");
        context.addServlet(new ServletHolder(new LoginServlet(userAuthService)), "/api/login");

        context.addFilter(new FilterHolder(new AuthorizationFilter()), "/users", null);
        context.addFilter(new FilterHolder(new AuthorizationFilter()), "/api/user", null);

        Server server = new Server(port);

        HandlerList handlers = new HandlerList();
        handlers.addHandler(createResourceHandler());
        handlers.addHandler(context);
        server.setHandler(handlers);

        return server;
    }

    private ResourceHandler createResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(new String[]{"index.html"});

        URL fileDir = UsersWebServerImpl.class.getClassLoader().getResource("static");
        if (fileDir == null) {
            throw new RuntimeException("File Directory not found");
        }
        resourceHandler.setResourceBase(URLDecoder.decode(fileDir.getPath(), StandardCharsets.UTF_8));
        return resourceHandler;
    }
}
