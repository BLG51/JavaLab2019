package ru.javalab.myservletapp.servlet;

import ru.javalab.myservletapp.context.ApplicationContext;
import ru.javalab.myservletapp.dao.RandomStringDaoImpl;
import ru.javalab.myservletapp.dto.UserDto;
import ru.javalab.myservletapp.model.RandomString;
import ru.javalab.myservletapp.model.User;
import ru.javalab.myservletapp.service.RandomStringService;
import ru.javalab.myservletapp.service.UserService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

@WebServlet("/login")
public class AuthServlet extends HttpServlet {
    RandomStringService rs = new RandomStringService();
    private static final String WORD = "secret";
    private ApplicationContext context;

    @Override
    public void init(ServletConfig config) {
        ServletContext servletContext = config.getServletContext();
        this.context = (ApplicationContext) servletContext.getAttribute("context");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Cookie[] cookies = req.getCookies();
        String cookieName = "auth";
        Cookie cookie = null;
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (cookieName.equals(c.getName())) {
                    cookie = c;
                       break;
                }
            }
        }
        if (cookie != null) {
            if (!cookie.getValue().equals("false")) {
                req.getSession().setAttribute("authorized", decyp(cookie.getValue()));
                resp.sendRedirect(req.getServletContext().getContextPath() + "/table");
                return;
            }
        }


        if (req.getSession().getAttribute("authorized") != null && (req.getSession().getAttribute("authorized").equals("false"))) {
            req.setAttribute("warning", "wrong data");
        } else if (req.getSession().getAttribute("authorized") != null && !(req.getSession().getAttribute("authorized").equals("false"))) {
            resp.sendRedirect(req.getServletContext().getContextPath() + "/table");
            return;
        }

        req.getSession().setAttribute("authorized", null);
        req.getServletContext().getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    //private List<User> getAllUsers() throws FileNotFoundException {
//        List<User> list = new ArrayList<>();
//        Scanner sc = new Scanner(new File("regdata.csv"));
//        int k = 0;
//        while (sc.hasNextLine()) {
//            String s = sc.nextLine();
//            switch (k) {
//                case 0:
//                    list.add(new User());
//                    k++;
//                    break;
//                case 1:
//                    list.get(list.size() - 1).setEmail(s);
//                    k++;
//                    break;
//                case 2:
//                    list.get(list.size() - 1).setPassword(s);
//                    k++;
//                    break;
//                case 3:
//                    list.get(list.size() - 1).setCountry(s);
//                    k++;
//                    break;
//                case 4:
//                    list.get(list.size() - 1).setAbout(s);
//                    k = 0;
//                    break;
//                default:
//                    break;
//            }
//        }
//        return list;
//    }

    private String cyp(String str1) {
        Random random = new Random();
        int s = random.nextInt();
        rs.create(new RandomString(s,str1));
        return s+"";
    }

    private String decyp(String  n) {
        RandomString r = rs.get(Integer.parseInt(n));
        return r.getId();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserService serv = context.getComponent(UserService.class, "userService");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String remember = req.getParameter("remember_me");
        req.getSession().setAttribute("authorized", false);
        //List<User> userList = getAllUsers();

        UserDto us = new UserDto(email, password);



//        for (User u : userList) {
//            if (email.equals(u.getEmail()) && password.equals(u.getPassword())) {
                if(serv.isRegistered(us)){

                req.getSession().setAttribute("authorized", email);

                if (remember.equals("on")) {
                    resp.addCookie(new Cookie("auth", cyp(email)));
                }
                }
//                break;
//            }
//        }

        resp.sendRedirect(req.getServletContext().getContextPath() + "/table");
    }
}