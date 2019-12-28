package ru.javalab.myservletapp.servlet;

import ru.javalab.myservletapp.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TableServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        Object attr = req.getSession().getAttribute("sortColumn")
        Cookie[] cookies = req.getCookies();
        String cookieName = "sortColumn";
        Cookie cookie = null;
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (cookieName.equals(c.getName())) {
                    cookie = c;
                    break;
                }
            }
            List<User> userList = getAllUsers();
            if (cookie != null) { //if (atttr != null)
                if (cookie.getValue().equals("sort_email_value")) { //if (attr.toString().equals("sort_email_value"))
                    userList.sort((o1, o2) -> {
                        int res = String.CASE_INSENSITIVE_ORDER.compare(o1.getEmail(), o2.getEmail());
                        if (res == 0) {
                            res = o1.getEmail().compareTo(o2.getEmail());
                        }
                        return res;
                    });
                    req.setAttribute("sort", 1);
                } else if (cookie.getValue().equals("sort_password_value")) { // if (attr.toString().equals("sort_password_value"))
                    userList.sort((o1, o2) -> {
                        int res = String.CASE_INSENSITIVE_ORDER.compare(o1.getPassword(), o2.getPassword());
                        if (res == 0) {
                            res = o1.getPassword().compareTo(o2.getPassword());
                        }
                        return res;
                    });
                }
            }
            req.setAttribute("list", userList);
        }
        req.getServletContext().getRequestDispatcher("/table.jsp").forward(req, resp);
    }

    private List<User> getAllUsers() throws FileNotFoundException {
        List<User> list = new ArrayList<>();
        Scanner sc = new Scanner(new File("regdata.csv"));
        int k = 0;
        while (sc.hasNextLine()) {
            String s = sc.nextLine();
            switch (k) {
                case 0:
                    list.add(new User());
                    k++;
                    break;
                case 1:
                    list.get(list.size() - 1).setEmail(s);
                    k++;
                    break;
                case 2:
                    list.get(list.size() - 1).setPassword(s);
                    k++;
                    break;
                case 3:
                    list.get(list.size() - 1).setCountry(s);
                    k++;
                    break;
                case 4:
                    list.get(list.size() - 1).setAbout(s);
                    k = 0;
                    break;
                default:
                    break;
            }
        }
        return list;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sort = req.getParameter("sort");
//        req.getSession().setAttribute("sortColumn", sort);
        Cookie cookie = new Cookie("sortColumn", sort);
        if (sort == null) {
            cookie.setMaxAge(0);
        } else {
            resp.addCookie(cookie);
        }
        resp.sendRedirect(getServletContext().getContextPath() + "/table");
    }
}
