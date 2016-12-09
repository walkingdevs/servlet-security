package walkingdevs.servlet.security;

import javax.inject.Inject;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class SessionDestroyer implements HttpSessionListener {
    @Inject
    private SecurityContextImpl securityContext;

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        // Not interested
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        securityContext.sessionDestroyed(
            se.getSession().getId()
        );
    }
}