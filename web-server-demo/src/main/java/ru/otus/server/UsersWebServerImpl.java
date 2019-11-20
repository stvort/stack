package ru.otus.server;

import com.google.gson.Gson;
import org.eclipse.jetty.security.*;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.security.Constraint;
import ru.otus.dao.UserDao;
import ru.otus.services.InMemoryLoginServiceImpl;
import ru.otus.services.TemplateProcessor;
import ru.otus.services.UserAuthService;
import ru.otus.servlet.AuthorizationFilter;
import ru.otus.servlet.LoginServlet;
import ru.otus.servlet.UsersApiServlet;
import ru.otus.servlet.UsersServlet;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class UsersWebServerImpl implements UsersWebServer {
    private final int port;
    private final UserAuthService userAuthService;
    private final UserDao userDao;
    private final Gson gson;
    private final TemplateProcessor templateProcessor;
    private final SecurityType securityType;
    private final Server server;

    public UsersWebServerImpl(int port, SecurityType securityType,
                              UserAuthService userAuthService,
                              UserDao userDao,
                              Gson gson,
                              TemplateProcessor templateProcessor) {
        this.port = port;
        this.securityType = securityType;
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

        context.addServlet(new ServletHolder(new UsersServlet(templateProcessor, userDao)), "/users");
        context.addServlet(new ServletHolder(new UsersApiServlet(userDao, gson)), "/api/user/*");

        if (securityType == SecurityType.FILTER_BASED) {
            //context.addServlet(new ServletHolder(new LoginServlet(templateProcessor, userAuthService)), "/login");
            context.addServlet(new ServletHolder(new LoginServlet(templateProcessor, userAuthService)), "/login");
            context.addFilter(new FilterHolder(new AuthorizationFilter()), "/users", null);
            context.addFilter(new FilterHolder(new AuthorizationFilter()), "/api/user", null);
        }

        Server server = new Server(port);

        HandlerList handlers = new HandlerList();
        handlers.addHandler(createResourceHandler());

        if (securityType == SecurityType.BASIC || securityType == SecurityType.BASIC_CUSTOM) {
            handlers.addHandler(createSecurityHandler(context, "/users", "/api/user/*"));
        }
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

    private SecurityHandler createSecurityHandler(ServletContextHandler context, String ...paths) {
        Constraint constraint = new Constraint();
        constraint.setName("auth");
        constraint.setAuthenticate(true);
        constraint.setRoles(new String[]{"user", "admin"});

        List<ConstraintMapping> constraintMappings = new ArrayList<>();
        IntStream.range(0, paths.length).forEachOrdered(i -> {
            ConstraintMapping mapping = new ConstraintMapping();
            mapping.setPathSpec(paths[i]);
            mapping.setConstraint(constraint);
            constraintMappings.add(mapping);
        });

        ConstraintSecurityHandler security = new ConstraintSecurityHandler();
        //как декодировать стороку с юзером:паролем https://www.base64decode.org/
        security.setAuthenticator(new BasicAuthenticator());

        LoginService loginService = securityType == SecurityType.BASIC? createHashLoginService() : createInMemoryLoginService();
        security.setLoginService(loginService);
        security.setHandler(new HandlerList(context));
        security.setConstraintMappings(constraintMappings);

        return security;
    }

    private LoginService createHashLoginService() {
        String configLocation = null;
        File realmFile = new File("./realm.properties");
        if (realmFile.exists()) {
            configLocation = realmFile.toURI().getPath();
        }

        if (configLocation == null) {
            System.out.println("local realm config not found, looking into Resources");
            configLocation = Optional.ofNullable(UsersWebServerImpl.class.getClassLoader().getResource("realm.properties"))
                    .orElseThrow(() -> new RuntimeException("Realm property file not found")).getPath();

        }
        return new HashLoginService("AnyRealm", URLDecoder.decode(configLocation, StandardCharsets.UTF_8));
    }

    private LoginService createInMemoryLoginService() {
        return new InMemoryLoginServiceImpl(userDao);
    }
}
