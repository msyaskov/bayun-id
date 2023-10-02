package dev.bayun.sso.entity;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

@ToString
@EqualsAndHashCode
public abstract class DefaultCoreEntity<ID extends Serializable> implements CoreEntity<ID> {

    private ID id;
    private LocalDateTime creationDate;
    private LocalDateTime lastUpdateDate;

    @Override
    public ID getId() {
        return id;
    }

    @Override
    public void setId(ID id) {
        this.id = id;
    }

    @Override
    public LocalDateTime getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(LocalDateTime date) {
        this.creationDate = date;
    }

    @Override
    public LocalDateTime getLastUpdateDate() {
        return this.lastUpdateDate;
    }

    public void setLastUpdateDate(LocalDateTime date) {
        this.lastUpdateDate = date;
    }
}
