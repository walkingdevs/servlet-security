package walkingdevs.umbrella.fake;

import walkingdevs.umbrella.Umbrella;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/login")
class FakeLoginServlet extends HttpServlet {
    @Inject
    private Umbrella umbrella;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        umbrella.logIn(
            req.getParameter("username"),
            req.getParameter("password")
        );
    }
}