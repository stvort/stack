package ru.otus.servlet;

import com.google.gson.Gson;
import ru.otus.model.User;
import ru.otus.dao.UserDao;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class UsersApiServlet extends HttpServlet {

    private static final String CONTENT_TYPE_APPLICATION_JSON = "application/json;charset=UTF-8";
    private static final String PARAM_ID = "id";
    private final UserDao userDao;
    private final Gson gson;

    public UsersApiServlet(UserDao userDao, Gson gson) {
        this.userDao = userDao;
        this.gson = gson;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter(PARAM_ID);
        User user = userDao.findById(Long.parseLong(id)).orElse(null);

        resp.setContentType(CONTENT_TYPE_APPLICATION_JSON);
        ServletOutputStream out = resp.getOutputStream();
        out.print(gson.toJson(user));
    }

}
