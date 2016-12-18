package walkingdevs.umbrella.fake;

import walkingdevs.umbrella.api.Authorities;
import walkingdevs.umbrella.spi.AuthoritiesProvider;

class FakeAuthoritiesProvider implements AuthoritiesProvider {
    public Authorities get(String username) {
        if (username.equals("batman")) {
            return Authorities.mk(
                "superhero",
                "bat",
                "man"
            );
        }
        if (username.equals("superman")) {
            return Authorities.mk(
                "superhero",
                "super",
                "man"
            );
        }
        if (username.equals("rocky")) {
            return Authorities.mk(
                "crazy",
                "army-hater",
                "man"
            );
        }
        return Authorities.mk();
    }
}