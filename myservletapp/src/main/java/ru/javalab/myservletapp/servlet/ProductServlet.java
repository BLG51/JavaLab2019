package ru.javalab.myservletapp.servlet;

import ru.javalab.myservletapp.context.ApplicationContext;
import ru.javalab.myservletapp.model.Product;
import ru.javalab.myservletapp.service.ProductService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ProductServlet  extends HttpServlet {
    private ApplicationContext context;

    @Override
    public void init(ServletConfig config) {
        ServletContext servletContext = config.getServletContext();
        this.context = (ApplicationContext) servletContext.getAttribute("context");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductService productService = context.getComponent(ProductService.class, "productService");
        List<Product> list = productService.getAll();
        req.setAttribute("products", list);
        req.getServletContext().getRequestDispatcher("/products.jsp").forward(req, resp);
    }

}
