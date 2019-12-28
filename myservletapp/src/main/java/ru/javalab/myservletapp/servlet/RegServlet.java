package ru.javalab.myservletapp.servlet;

import ru.javalab.myservletapp.context.ApplicationContext;
import ru.javalab.myservletapp.dto.UserDto;
import ru.javalab.myservletapp.model.Role;
import ru.javalab.myservletapp.model.User;
import ru.javalab.myservletapp.service.UserService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegServlet extends HttpServlet {
    private String filename = "regdata.csv";
    private boolean isOk = true;
    private ApplicationContext context;

    @Override
    public void init(ServletConfig config) {
        ServletContext servletContext = config.getServletContext();
        this.context = (ApplicationContext) servletContext.getAttribute("context");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        List<Role> list = (new UserService()).getRoles();
        writer.print("<html>" +
                "<head>" +
                "</head>" +
                "<body>" +
                "<div>" +
                "<form method=\"post\" action=\"register\">" +
                "<p>email</p>" +
                "<input name=\"email\" type=\"email\"><br>" +
                "<p>password</p>" +
                "<input name=\"password\" type=\"password\"><br>" +
                "<p>repeat password</p>" +
                "<input name=\"password-repeat\" type=\"password\" placeholder=\"repeat password\"><br>" +
                "<p>country</p>" +
                "<select name=\"country\" type=\"text\">" +
                "<option value=\"russia\">Russia</option>" +
                "<option value=\"ukraine\">Ukraine</option>" +
                "<option value=\"usa\">USA</option>" +
                "<option value=\"germany\">Germany</option>" +
                "</select><br>" +
                "<p>about</p>" +
                "<input name=\"about\" type=\"text\"><br>" +
                "<p>agreement</p>" +
                "<input name=\"agreement\" type=\"checkbox\"><br>" +

                "<p>role</p>" +
                "<select name=\"role\" type=\"text\">");

        for (Role r : list) {
            writer.print("<option value=\"" + r.getRole() + "\">" + r.getRole() + "</option>");
        }
        writer.println("</select><br>" +

                "<p>new role</p>" +
                "<input name=\"nrole\" type=\"text\"><br>" +

                "<input type=\"submit\">" +
                "</form>" +
                "</div>");
        if (!isOk) {
            writer.println("<p>Wrong data</p>");
        }
        writer.println("<body>" +
                "</html>");

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserService serv = context.getComponent(UserService.class, "userService");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String passwordRepeat = req.getParameter("password-repeat");
        String country = req.getParameter("country");
        String about = req.getParameter("about");
        String role = req.getParameter("role");
        String newRole = req.getParameter("nrole");
        String agreement = req.getParameter("agreement");

        User user = new User(email, password, country, about, role);
        UserDto us = new UserDto(email, password);

        Matcher matcher = Pattern.compile(".+@.+\\..+").matcher(email);

        try {
            if (!serv.isRegistered(us) && matcher.find() && !password.equals("") && !country.equals("") && !about.equals("") && password.equals(passwordRepeat) && !role.equals("") && agreement.equals("on")) {

                Role r = new Role(newRole);
                if (!newRole.equals("") && !serv.roleExists(r)) {
                    serv.createRole(r);
                    user.setRole(newRole);
                }

                serv.create(user);
                isOk = true;
            } else {
                isOk = false;
            }
        } catch (Exception e) {
        //    isOk = false;
            resp.sendRedirect(req.getContextPath() + "/errorpage");
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/register");
    }
}
