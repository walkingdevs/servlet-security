package walkingdevs.umbrella;

import org.junit.Test;
import walkingdevs.http11.ReqBuilder;
import walkingdevs.http11.Resp;
import walkingdevs.str.Str;

import javax.inject.Inject;

public class SessionTest extends AbstractTest {
    @Inject
    private UmbrellaImpl umbrella;

    @Test
    public void shouldAllowAnonymous() {
        Resp resp = ReqBuilder.GET(url.toString() + "/anonymous")
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
    public void shouldNotAllowNotAllowed() {
        Resp resp = ReqBuilder.GET(url.toString() + "bank-account")
            .build()
            .send();
        assertEquals(
            404,
            resp.status()
        );
        assertNotEquals(
            "666",
            resp.body().text()
        );
    }

    @Test
    public void shouldRedirectToLoginIfNotAllowedAndIfNotAuthenticated() {
        Resp resp = ReqBuilder.GET(url.toString() + "superheroes-only")
            .build()
            .send();
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
        Resp resp = ReqBuilder.GET(url.toString() + "/login?username=batman&password=top-secret")
            .build()
            .send();
        resp = ReqBuilder.GET(url.toString() + "/superheroes-only")
            .header("Cookie", resp.headers().get("Set-Cookie").value())
            .build()
            .send();
        assertEquals(
            200,
            resp.status()
        );
        assertEquals(
            "Welcome, Super Hero!",
            resp.body().text()
        );
    }

    @Test
    public void shouldSetSession() {
        Resp resp = ReqBuilder.GET(url.toString() + "anonymous")
            .build()
            .send();
        assertEquals(
            200,
            resp.status()
        );
        assertFalse(
            Str.mk(
                umbrella.sessionId()
            ).isBlank()
        );
        assertTrue(
            resp.headers().has("Set-Cookie")
        );
        assertTrue(
            resp.headers().get("Set-Cookie").value().contains(
                umbrella.sessionId()
            )
        );
    }

    @Test
    public void shouldLogin() {
        Resp resp = ReqBuilder.GET(url.toString() + "/login?username=admin&password=top-secret")
            .build()
            .send();
        assertEquals(
            200,
            resp.status()
        );
        assertTrue(
            umbrella.isIn()
        );
        assertEquals(
            "admin",
            umbrella.user()
        );
    }

    @Test
    public void shouldRedirectToRootIfAlreadyLoggedIn() {
        Resp resp = ReqBuilder.GET(url.toString() + "/login?username=user&password=top-secret")
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
        Resp resp = ReqBuilder.GET(url.toString() + "/login?username=batman&password=top-secret")
            .build()
            .send();
        resp = ReqBuilder.GET(url.toString() + "superman")
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
            "Welcome, Superman!",
            resp.body().text()
        );
    }
}