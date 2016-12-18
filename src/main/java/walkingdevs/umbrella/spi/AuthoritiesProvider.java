package walkingdevs.umbrella.spi;

import walkingdevs.umbrella.api.Authorities;

public interface AuthoritiesProvider {
    Authorities get(String username);
}