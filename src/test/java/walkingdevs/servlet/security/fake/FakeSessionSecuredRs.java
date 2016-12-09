package walkingdevs.servlet.security.fake;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/")
public class FakeSessionSecuredRs {
    @GET @Path("/hello-my-anonymous-friend")
    public String hello() {
        return "Hello, anonymous!";
    }

    @GET @Path("/admin")
    public String admin() {
        return "Welcome, admin!";
    }
}