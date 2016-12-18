package walkingdevs.umbrella.api;

import java.util.Iterator;
import java.util.Set;

class AuthoritiesImpl implements Authorities {
    public Iterator<String> iterator() {
        return authorities.iterator();
    }

    AuthoritiesImpl(Set<String> authorities) {
        this.authorities = authorities;
    }

    private final Set<String> authorities;
}
