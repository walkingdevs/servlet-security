package walkingdevs.umbrella.api;

import java.util.HashSet;
import java.util.Set;

public interface Authorities extends Iterable<String> {
    static Authorities mk(String... authorities) {
        Set<String> set = new HashSet<>();
        for (String authority : authorities) {
            set.add(authority);
        }
        return new AuthoritiesImpl(set);
    }
}