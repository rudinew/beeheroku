package com.bee.backend.domain.data;

import com.bee.backend.domain.BaseEntity;
import com.bee.backend.domain.security.BeeUsers;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="bee_doc_notification")
public class BeeDocNotification extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private Boolean is_required;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private BeeDocType beeDocType;

    //Пользователь
    @ManyToOne
    @JoinColumn(name = "user_id")
    private BeeUsers beeUsers;

    public BeeDocNotification() {
    }

    public BeeDocNotification(Boolean is_required, BeeDocType beeDocType, BeeUsers beeUsers) {
        this.is_required = is_required;
        this.beeDocType = beeDocType;
        this.beeUsers = beeUsers;
    }

    public Boolean getIs_required() {
        return is_required;
    }

    public void setIs_required(Boolean is_required) {
        this.is_required = is_required;
    }

    public BeeDocType getBeeDocType() {
        return beeDocType;
    }

    public void setBeeDocType(BeeDocType beeDocType) {
        this.beeDocType = beeDocType;
    }

    public BeeUsers getBeeUsers() {
        return beeUsers;
    }

    public void setBeeUsers(BeeUsers beeUsers) {
        this.beeUsers = beeUsers;
    }
}
