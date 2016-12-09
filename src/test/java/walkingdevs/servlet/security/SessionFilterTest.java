package walkingdevs.servlet.security;

import org.junit.Test;
import walkingdevs.http11.ReqBuilder;
import walkingdevs.http11.Resp;
import walkingdevs.str.Str;

import javax.inject.Inject;

public class SessionFilterTest extends AbstractTest {
    @Inject
    private SecurityContextImpl securityContext;

    @Test
    public void shouldAllow() {
        Resp resp = ReqBuilder.GET(url.toString() + "/hello-my-anonymous-friend")
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
    public void shouldRedirectToLoginIfNotAllowedAndIfNotAuthenticated() {
        Resp resp = ReqBuilder.GET(url.toString() + "/admin").build().send();
        assertEquals(
            302,
            resp.status()
        );
        assertEquals(
            url.getPath() + "login",
            resp.headers().get("Location").value()
        );
        assertNotEquals(
            "Welcome, admin!",
            resp.body().text()
        );
    }

    @Test
    public void shouldAllowAuthorised() {
        Resp resp = ReqBuilder.GET(url.toString() + "/login?username=tor&password=fake")
            .build()
            .send();
        resp = ReqBuilder.GET(url.toString() + "/admin")
            .header("Cookie", resp.headers().get("Set-Cookie").value())
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
    public void shouldSetSession() {
        Resp resp = ReqBuilder.GET(url.toString())
            .build()
            .send();
        assertFalse(
            Str.mk(
                securityContext.sessionId()
            ).isBlank()
        );
        assertTrue(
            resp.headers().has("Set-Cookie")
        );
        assertTrue(
            resp.headers().get("Set-Cookie").value().contains(
                securityContext.sessionId()
            )
        );
    }

    @Test
    public void shouldLogin() {
        Resp resp = ReqBuilder.GET(url.toString() + "/login?username=admin&password=fake")
            .build()
            .send();
        assertEquals(
            200,
            resp.status()
        );
        assertTrue(
            securityContext.isIn()
        );
        assertEquals(
            "admin",
            securityContext.user()
        );
    }

    @Test
    public void shouldRedirectToRootIfAlreadyLoggedIn() {
        Resp resp = ReqBuilder.GET(url.toString() + "/login?username=user&password=fake")
            .build()
            .send();
        resp = ReqBuilder.GET(url.toString() + "/login")
            .header("Cookie", resp.headers().get("Set-Cookie").value())
            .build()
            .send();
        assertEquals(
            302,
            resp.status()
        );
        assertEquals(
            url.getPath(),
            resp.headers().get("Location").value() + "/"
        );
    }

    @Test
    public void shouldRedirectToDeniedIfNotAllowedAndIfAuthenticated() {
        Resp resp = ReqBuilder.GET(url.toString() + "/login?username=batman&password=fake")
            .build()
            .send();
        resp = ReqBuilder.GET(url.toString() + "/admin")
            .header("Cookie", resp.headers().get("Set-Cookie").value())
            .build()
            .send();
        assertEquals(
            302,
            resp.status()
        );
        assertEquals(
            url.getPath() + "denied",
            resp.headers().get("Location").value()
        );
        assertNotEquals(
            "Welcome, admin!",
            resp.body().text()
        );
    }
}