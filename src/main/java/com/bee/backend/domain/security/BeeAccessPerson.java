package com.bee.backend.domain.security;

import com.bee.backend.domain.BaseEntity;
import com.bee.backend.domain.data.BeePerson;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Вказано кому доступна Особова картка
 */
@Entity
@Table(name="bee_access_person")
public class BeeAccessPerson extends BaseEntity {
    private static final long serialVersionUID = 1L;
    //Особова картка
   /// @NotNull
    @ManyToOne
    @JoinColumn(name = "person_id")
    private BeePerson beePerson;
    //Пользователь
    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private BeeUsers beeUsers;

    @Enumerated(EnumType.STRING)
    private UserPrivileges privileges;
    //Пользователь который дал доступ
    @ManyToOne
    @JoinColumn(name = "parent_user_id")
    private BeeUsers beeParentUsers;

    public BeeAccessPerson() {
    }

    public BeeAccessPerson(BeePerson beePerson, BeeUsers beeUsers, UserPrivileges privileges, BeeUsers beeParentUsers) {
        this.beePerson = beePerson;
        this.beeUsers = beeUsers;
        this.privileges = privileges;
        this.beeParentUsers = beeParentUsers;
    }

    public BeePerson getBeePerson() {
        return beePerson;
    }

    public void setBeePerson(BeePerson beePerson) {
        this.beePerson = beePerson;
    }

    public BeeUsers getBeeUsers() {
        return beeUsers;
    }

    public void setBeeUsers(BeeUsers beeUsers) {
        this.beeUsers = beeUsers;
    }

    public UserPrivileges getPrivileges() {
        return privileges;
    }

    public void setPrivileges(UserPrivileges privileges) {
        this.privileges = privileges;
    }

    public BeeUsers getBeeParentUsers() {
        return beeParentUsers;
    }

    public void setBeeParentUsers(BeeUsers beeParentUsers) {
        this.beeParentUsers = beeParentUsers;
    }
}
