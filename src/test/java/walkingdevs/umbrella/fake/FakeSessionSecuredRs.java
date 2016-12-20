package walkingdevs.umbrella.fake;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/")
public class FakeSessionSecuredRs extends FakeRs {
    @GET
    @Path("/anonymous/image")
    public String images() {
        return "Image";
    }
}