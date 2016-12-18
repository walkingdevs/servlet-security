package walkingdevs.umbrella.fake;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

abstract class FakeRs {
    @GET @Path("/anonymous")
    public String anonymous() {
        return "Hello, anonymous!";
    }

    @GET @Path("/superheroes-only")
    public String superhero() {
        return "Welcome, Super Hero!";
    }

    @GET @Path("/bank-account")
    public String forgot() {
        return "666";
    }

    @GET @Path("/superman")
    public String superman() {
        return "Welcome, Superman!";
    }
}