package walkingdevs.umbrella;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
class SessionDestroyer implements HttpSessionListener {
    private UmbrellaImpl umbrella;

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        // Not interested
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        umbrella.sessionDestroyed(
            se.getSession().getId()
        );
    }
}