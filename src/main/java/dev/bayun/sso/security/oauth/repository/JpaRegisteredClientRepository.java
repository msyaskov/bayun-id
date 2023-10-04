package dev.bayun.sso.security.oauth.repository;

import dev.bayun.sso.core.mapper.EntityMapper;
import dev.bayun.sso.security.oauth.entity.RegisteredClientEntity;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

/**
 * @author Максим Яськов
 */

@Repository
public class JpaRegisteredClientRepository implements RegisteredClientRepository {

    private final RegisteredClientEntityRepository registeredClientEntityRepository;

    private final EntityMapper<RegisteredClientEntity, RegisteredClient> registeredClientEntityMapper;

    public JpaRegisteredClientRepository(RegisteredClientEntityRepository registeredClientEntityRepository,
                                         EntityMapper<RegisteredClientEntity, RegisteredClient> registeredClientEntityMapper) {
        Assert.notNull(registeredClientEntityRepository, "a registeredClientEntityRepository must not be null");
        Assert.notNull(registeredClientEntityMapper, "a registeredClientEntityMapper must not be null");

        this.registeredClientEntityRepository = registeredClientEntityRepository;
        this.registeredClientEntityMapper = registeredClientEntityMapper;
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        Assert.hasText(clientId, "a clientId must not be empty");
        return this.registeredClientEntityRepository.findByClientId(clientId)
                .map(registeredClientEntityMapper::toObject)
                .orElse(null);
    }

    @Override
    public RegisteredClient findById(String id) {
        Assert.hasText(id, "an id must not be empty");
        return this.registeredClientEntityRepository.findById(id)
                .map(registeredClientEntityMapper::toObject)
                .orElse(null);
    }

    @Override
    public void save(RegisteredClient registeredClient) {
        Assert.notNull(registeredClient, "a registeredClient must not be null");
        this.registeredClientEntityRepository.save(registeredClientEntityMapper.toEntity(registeredClient));
    }
}
