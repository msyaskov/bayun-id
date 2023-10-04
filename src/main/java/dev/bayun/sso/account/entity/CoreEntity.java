package dev.bayun.sso.account.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public interface CoreEntity<ID extends Serializable> extends Serializable {

    ID getId();

    void setId(ID id);

    LocalDateTime getCreationDate();

    LocalDateTime getLastUpdateDate();

}
