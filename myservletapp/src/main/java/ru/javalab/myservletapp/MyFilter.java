import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class MyFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) servletRequest;
        String methpd = httpReq.getMethod();
        File file = new File("myfile.txt");
        PrintWriter pw = new PrintWriter(file);
        pw.println(methpd);
        pw.flush();
        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void destroy() {

    }
}
