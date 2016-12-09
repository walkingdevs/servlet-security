package walkingdevs.servlet.security;

import walkingdevs.data.Path;
import walkingdevs.http11.Method;
import walkingdevs.servlet.security.api.Permission;
import walkingdevs.servlet.security.api.Type;
import walkingdevs.servlet.security.spi.SecuredPath;
import walkingdevs.servlet.security.spi.SecurityProvider;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebFilter(urlPatterns = "/*")
class SessionFilter implements Filter {
    private final Logger LOG = Logger.getLogger(getClass().getName());

    @Inject
    private SecurityContextImpl securityContext;

    @Inject
    private SecurityProvider securityProvider;

    @Inject
    private Instance<SecuredPath> securedPaths;

    private Map<Path<String>, SecuredPath> sessionSecuredPaths = new HashMap<>();

    @PostConstruct
    private void init() {
        for (SecuredPath securedPath : securedPaths) {
            if (securedPath.type() == Type.Session) {
                sessionSecuredPaths.put(securedPath.path(), securedPath);
                LOG.log(Level.FINE, "+Session SecuredPath: {0}", securedPath.path());
            }
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {
        LOG.fine("SessionFilter created");
    }

    @Override
    public void destroy() {
        LOG.fine("SessionFilter destroyed");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) req;
        HttpServletResponse httpResp = (HttpServletResponse) resp;
        LOG.log(Level.FINE,
            "Filtering: {0}", httpReq.getRequestURL()
        );
        Path<String> reqPath = Path.mkHttp(
            httpReq.getRequestURI()
        );
        if (reqPath.root().equals(Path.mk(httpReq.getContextPath()))) {
            reqPath = reqPath.tail();
        }
        SecuredPath securedPath = sessionSecuredPaths.get(
            Path.mkHttp(
                httpReq.getRequestURI()
            ).root()
        );
        System.out.println(securedPath);
        if (securedPath == null && sessionSecuredPaths.containsKey(Path.mk("/"))) {
            securedPath = sessionSecuredPaths.get(Path.mk("/"));
            securityContext.sessionChanged(
                ((HttpServletRequest) req).getSession().getId()
            );
            LOG.fine("Secure root");
        } else {
            chain.doFilter(req, resp);
            LOG.fine("Dismiss");
            return;
        }
        Path<String> path;
        if (securedPath.path().equals(Path.mk("/"))) {
            path = Path.mkHttp(httpReq.getRequestURI());
            LOG.fine("Path is /");
        } else {
            path = Path.mkHttp(httpReq.getRequestURI()).tail();
            LOG.fine("Path is " + Path.mkHttp(httpReq.getRequestURI()).root());
        }
        // Actually a Hack... because of Fucking servlet api
        if (path.root().equals(Path.mk(httpReq.getContextPath()))) {
            path = path.tail();
        }
        if (path.string().equals("/login")) {
            if (securityContext.isIn()) {
                httpResp.sendRedirect(httpReq.getContextPath());
                LOG.fine("Already logged in. Redirecting to " + httpReq.getContextPath());
            } else {
                chain.doFilter(req, resp);
                LOG.fine("Stay at /login");
            }
            return;
        }
        if (path.string().equals("/denied")) {
            chain.doFilter(req, resp);
            LOG.fine("Stay at /denied");
            return;
        }
        // Everything is ok
        if (isAllowed(path, Method.valueOf(httpReq.getMethod()), securedPath)) {
            chain.doFilter(req, resp);
            LOG.log(Level.FINE, "ALLOWED {0} to {1}",
                new Object[]{
                    httpReq.getMethod(),
                    httpReq.getRequestURL()
                }
            );
            return;
        }
        LOG.log(Level.FINE, "DENIED {0} to {1}",
            new Object[]{
                httpReq.getMethod(),
                httpReq.getRequestURL()
            }
        );
        if (!securityContext.isIn()) {
            httpResp.sendRedirect(httpReq.getContextPath() + "/login");
            LOG.fine("Redirecting to " + httpReq.getContextPath() + "/login");
        } else {
            httpResp.sendRedirect(httpReq.getContextPath() + "/denied");
            LOG.fine("Redirecting to " + httpReq.getContextPath() + "/denied");
        }
    }

    private boolean isAllowed(Path<String> path, Method method, SecuredPath securedPath) {
        Set<String> authorities = new HashSet<>();
        authorities.add("anonymous");
        if (securityContext.isIn()) {
            for (String authority : securityProvider.authorities(securityContext.user())) {
                authorities.add(authority);
            }
        }
        for (Permission permission : securedPath.permissions()) {
            if (path.root().equals(permission.path().root()) && permission.method() == method && authorities.contains(permission.authority())) {
                return true;
            }
        }
        return false;
    }
}