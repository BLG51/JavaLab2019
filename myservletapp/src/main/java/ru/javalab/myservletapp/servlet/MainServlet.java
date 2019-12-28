package ru.javalab.myservletapp.servlet;

import ru.javalab.myservletapp.model.RandomString;
import ru.javalab.myservletapp.model.User;
import ru.javalab.myservletapp.service.RandomStringService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

@WebServlet ("/index")
public class MainServlet extends HttpServlet{
        RandomStringService rs = new RandomStringService();
        private static final String WORD = "secret";

        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            req.getServletContext().getRequestDispatcher("/index.jsp").forward(req, resp);
        }

        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
