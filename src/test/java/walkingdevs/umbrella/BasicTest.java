package walkingdevs.umbrella;

import org.junit.Test;
import walkingdevs.http11.ReqBuilder;
import walkingdevs.http11.Resp;

public class BasicTest extends AbstractTest {
    @Test
    public void shouldAllow() {
        Resp resp = ReqBuilder.GET(url.toString() + "basic/anonymous")
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
        Resp resp = ReqBuilder.GET(url.toString() + "/basic/superheroes-only")
            .build()
            .send();
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
    public void shouldRespWith401IfCredentialsIsIncorrect() {
        Resp resp = ReqBuilder.GET(url.toString() + "basic/superheroes-only")
            .header("Username", "batman")
            .header("Password", "secret")
            .build()
            .send();
        assertEquals(
            401,
            resp.status()
        );
        assertNotEquals(
            "Welcome, Super Hero!",
            resp.body().text()
        );
    }

    @Test
    public void shouldAllowAuthorised() {
        Resp resp = ReqBuilder.GET(url.toString() + "basic/superheroes-only")
            .header("Username", "batman")
            .header("Password", "top-secret")
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
    public void shouldRespWith403IfNotAllowedAndIfAuthenticated() {
        Resp resp = ReqBuilder.GET(url.toString() + "basic/superman")
            .header("Username", "batman")
            .header("Password", "top-secret")
            .build()
            .send();
        assertEquals(
            403,
            resp.status()
        );
        assertNotEquals(
            "Welcome, Superman!",
            resp.body().text()
        );
    }
}