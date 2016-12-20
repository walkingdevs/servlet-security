package walkingdevs.umbrella;

import walkingdevs.data.Path;
import walkingdevs.http11.Method;
import walkingdevs.str.Str;
import walkingdevs.umbrella.api.*;
import walkingdevs.umbrella.spi.PermsProvider;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebFilter(urlPatterns = "/*")
class UmbrellaFilter implements Filter {
    private final Logger LOG = Logger.getLogger(getClass().getName());

    @Inject
    private UmbrellaImpl umbrella;

    @Inject
    private PermsProvider permsProvider;

    private Perms perms;

    @PostConstruct
    private void init() {
        perms = PermsBuilder.mk()
            .add(permsProvider.get())
            .add(PermsComposer.session("/")
                .authority("anonymous")
                .path("/login")
                .path("/denied")
                .anyMethod()
                .compose()
            )
            .build();
    }

    @Override
    public void init(FilterConfig filterConfig) {
        LOG.fine("UmbrellaFilter created");
    }

    @Override
    public void destroy() {
        LOG.fine("UmbrellaFilter destroyed");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) req;
        HttpServletResponse httpResp = (HttpServletResponse) resp;
        LOG.log(Level.FINE,
            "Filtering {0}", httpReq.getRequestURL()
        );
        umbrella.sessionChanged(
            httpReq.getSession().getId()
        );
        Path<String> path = Path.mkHttp(
            httpReq.getRequestURI()
        );
        if (!httpReq.getContextPath().equals("/")) {
            path = path.tail();
        }
        Method method = Method.valueOf(httpReq.getMethod());
        AuthMethod authMethod = figureOutAuthMethod(path);
        if (authMethod == AuthMethod.Session) {
            ProcessResult processResult = processSessionReq(path, authMethod, method);
            if (processResult == ProcessResult.Allowed) {
                chain.doFilter(req, resp);
                LOG.log(Level.FINE, "ALLOWED {0} to {1}",
                    new Object[] {
                        method,
                        path
                    }
                );
            } else if (processResult == ProcessResult.Unauthorised) {
                httpResp.sendRedirect(httpReq.getContextPath() + "/login");
                LOG.fine("Redirecting to login page");
            } else if (processResult == ProcessResult.Denied) {
                httpResp.sendRedirect(httpReq.getContextPath() + "/denied");
                LOG.log(Level.FINE, "DENIED {0} to {1}",
                    new Object[] {
                        method,
                        path
                    }
                );
            }  else if (processResult == ProcessResult.RedirectToIndex) {
                httpResp.sendRedirect(httpReq.getContextPath());
                LOG.fine("Already logged in");
            }
        } else if(authMethod == AuthMethod.Basic) {
            ProcessResult processResult = processBasicReq(
                path,
                authMethod,
                method,
                httpReq.getHeader("Username"),
                httpReq.getHeader("Password")
            );
            if (processResult == ProcessResult.Allowed) {
                chain.doFilter(req, resp);
            } else if (processResult == ProcessResult.Unauthorised) {
                httpResp.setStatus(401);
                LOG.fine("Ended up with 401");
            } else {
                httpResp.setStatus(403);
                LOG.fine("Ended up with 403");
            }
        } else {
            // Simply ignore not secured content
            httpResp.setStatus(403);
        }
    }

    // TODO: tree...
    private AuthMethod figureOutAuthMethod(Path<String> path) {
        for (Perm perm : perms) {
            if (perm.path().equals(path)) {
                return perm.authenticationMethod();
            }
        }
        for (Perm p : perms) {
            if (path.string().startsWith(p.path().string())) {
                return p.authenticationMethod();
            }
        }
        return AuthMethod.None;
    }

    private ProcessResult processSessionReq(Path<String> path, AuthMethod authMethod, Method method) {
        if (path.string().equals("/login") && umbrella.isIn()) {
            return ProcessResult.RedirectToIndex;
        }
        // Everything is isOk
        if (isAllowed(path, authMethod, method)) {
            return ProcessResult.Allowed;
        }
        if (!umbrella.isIn()) {
            return ProcessResult.Unauthorised;
        } else {
            return ProcessResult.Denied;
        }
    }

    private ProcessResult processBasicReq(Path<String> path, AuthMethod authMethod, Method method, String username, String password) {
        if (!Str.mk(username).isBlank() && !Str.mk(password).isBlank()) {
            LogInResult logInResult = umbrella.logIn(
                username,
                password
            );
            if (!logInResult.isOk()) {
                LOG.log(Level.FINE,
                    "{0} failed to login", username
                );
            }
        }
        // Everything is Ok
        if (isAllowed(path, authMethod, method)) {
            return ProcessResult.Allowed;
        }
        if (!umbrella.isIn()) {
            return ProcessResult.Unauthorised;
        } else {
            return ProcessResult.Denied;
        }
    }

    private boolean isAllowed(Path<String> path, AuthMethod authMethod, Method method) {
        boolean anonymousIsAllowed = check(Perm.mk(
            path.string(),
            authMethod,
            "anonymous",
            method
        ));
        if (anonymousIsAllowed) {
            return true;
        }
        for (String authority : umbrella.authorities(umbrella.user())) {
            boolean allowed = check(Perm.mk(
                path.string(),
                authMethod,
                authority,
                method
            ));
            if (allowed) {
                return true;
            }
        }
        return false;
    }

    // TODO: tree...
    private boolean check(Perm perm) {
        if (perms.has(perm)) {
            return true;
        } else {
            for (Perm p : perms) {
                if (perm.path().string().startsWith(p.path().string())) {
                    return p.authenticationMethod().equals(perm.authenticationMethod())
                        && p.authority().equals(perm.authority())
                        && p.method().equals(perm.method());
                }
            }
        }
        return false;
    }

    enum ProcessResult {
        Unauthorised,
        Denied,
        Allowed,
        BadRequest,
        RedirectToIndex
    }
}