package dev.bayun.sso.oauth.repository;

import dev.bayun.sso.core.convert.EntityConverter;
import dev.bayun.sso.oauth.entity.RegisteredClientEntity;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

/**
 * @author Максим Яськов
 */

@Repository
public class JpaRegisteredClientRepository implements RegisteredClientRepository {

    private final JpaRegisteredClientEntityRepository jpaRegisteredClientEntityRepository;

    private final EntityConverter<RegisteredClientEntity, RegisteredClient> registeredClientEntityConverter;

    public JpaRegisteredClientRepository(JpaRegisteredClientEntityRepository jpaRegisteredClientEntityRepository,
                                         EntityConverter<RegisteredClientEntity, RegisteredClient> registeredClientEntityConverter) {
        Assert.notNull(jpaRegisteredClientEntityRepository, "a registeredClientEntityRepository must not be null");
        Assert.notNull(registeredClientEntityConverter, "a registeredClientEntityMapper must not be null");

        this.jpaRegisteredClientEntityRepository = jpaRegisteredClientEntityRepository;
        this.registeredClientEntityConverter = registeredClientEntityConverter;
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        Assert.hasText(clientId, "a clientId must not be empty");
        return this.jpaRegisteredClientEntityRepository.findByClientId(clientId)
                .map(registeredClientEntityConverter::convertToObject)
                .orElse(null);
    }

    @Override
    public RegisteredClient findById(String id) {
        Assert.hasText(id, "an id must not be empty");
        return this.jpaRegisteredClientEntityRepository.findById(id)
                .map(registeredClientEntityConverter::convertToObject)
                .orElse(null);
    }

    @Override
    public void save(RegisteredClient registeredClient) {
        Assert.notNull(registeredClient, "a registeredClient must not be null");
        this.jpaRegisteredClientEntityRepository.save(registeredClientEntityConverter.convertToEntity(registeredClient));
    }
}
