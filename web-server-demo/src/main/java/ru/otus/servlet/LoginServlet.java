package ru.otus.servlet;

import ru.otus.services.UserAuthService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.*;

public class LoginServlet extends HttpServlet {

    private static final String PARAM_LOGIN = "login";
    private static final String PARAM_PASSWORD = "password";
    private static final int MAX_INACTIVE_INTERVAL = 30;
    private static final String PAGE_USERS = "/users";


    private final UserAuthService userAuthService;

    public LoginServlet(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String name = request.getParameter(PARAM_LOGIN);
        String password = request.getParameter(PARAM_PASSWORD);

        if (userAuthService.authenticate(name, password)) {
            HttpSession session = request.getSession();
            session.setMaxInactiveInterval(MAX_INACTIVE_INTERVAL);
            response.sendRedirect(PAGE_USERS);
        } else {
            response.setStatus(SC_UNAUTHORIZED);
        }

    }

}
