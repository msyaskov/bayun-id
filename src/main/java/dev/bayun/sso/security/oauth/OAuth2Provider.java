package dev.bayun.sso.security.oauth;

/**
 * @author Максим Яськов
 */

public enum OAuth2Provider {
    GOOGLE,
    YANDEX;

    public static OAuth2Provider getByName(String name) {
        if (name == null) {
            throw new NullPointerException("A name is null");
        }

        for (OAuth2Provider provider : OAuth2Provider.values()) {
            if (provider.name().equalsIgnoreCase(name)) {
                return provider;
            }
        }

        throw new IllegalArgumentException(String.format("OAuth2Provider with name=%s not found", name));
    }
}
