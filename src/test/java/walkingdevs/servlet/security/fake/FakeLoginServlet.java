package walkingdevs.servlet.security.fake;

import walkingdevs.servlet.security.api.SecurityContext;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/login")
public class FakeLoginServlet extends HttpServlet {
    @Inject
    private SecurityContext securityContext;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        securityContext.logIn(
            req.getParameter("username"),
            req.getParameter("password")
        );
    }
}