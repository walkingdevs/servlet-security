package walkingdevs.servlet.security;

import org.junit.Test;
import walkingdevs.http11.ReqBuilder;
import walkingdevs.http11.Resp;

import javax.inject.Inject;

public class BasicFilterTest extends AbstractTest {
    @Inject
    private SecurityContextImpl securityContext;

    @Test
    public void shouldAllow() {
        Resp resp = ReqBuilder.GET(url.toString() + "/basic/hello-my-anonymous-friend")
            .build()
            .send();
        assertEquals(
            200,
            resp.status()
        );
        assertEquals(
            "Hello, anonymous!",
            resp.body().text()
        );
    }

    @Test
    public void shouldRespWith401IfNotAllowedAndIfNotAuthenticated() {
        Resp resp = ReqBuilder.GET(url.toString() + "/basic/admin").build().send();
        assertEquals(
            401,
            resp.status()
        );
        assertNotEquals(
            "Welcome, admin!",
            resp.body().text()
        );
    }

    @Test
    public void shouldAllowAuthorised() {
        Resp resp = ReqBuilder.GET(url.toString() + "/admin")
            .header("Username", "admin")
            .header("Password", "fake")
            .build()
            .send();
        assertEquals(
            200,
            resp.status()
        );
        assertEquals(
            "Welcome, admin!",
            resp.body().text()
        );
    }

    @Test
    public void shouldRespWith403IfNotAllowedAndIfAuthenticated() {
        Resp resp = ReqBuilder.GET(url.toString() + "/admin")
            .header("Username", "admin")
            .header("Password", "fake")
            .build()
            .send();
        assertEquals(
            403,
            resp.status()
        );
        assertNotEquals(
            "Welcome, admin!",
            resp.body().text()
        );
    }
}