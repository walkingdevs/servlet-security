package walkingdevs.umbrella.spi;

import walkingdevs.umbrella.api.Perms;

public interface PermsProvider {
    Perms get();
}