package com.bee.backend.domain.data;

import com.bee.backend.domain.BaseEntity;
import com.bee.backend.domain.security.BeeUsers;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

//журнал дій
@Entity
@Table(name="BEE_ACTIONS")
public class BeeActions extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String name;
    private String action;
    //Пользователь
    @ManyToOne
    @JoinColumn(name = "user_id")
    private BeeUsers beeUsers;
    //дата
    @Column(name = "dt_from")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate dtFrom;

    public BeeActions() {
    }

    public BeeActions(BeeUsers beeUsers, String action, String name) {
        this.beeUsers = beeUsers;
        this.action = action;
        this.name = name;
        this.dtFrom = new LocalDate();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public BeeUsers getUsers() {
        return beeUsers;
    }

    public void setUsers(BeeUsers beeUsers) {
        this.beeUsers = beeUsers;
    }

    public LocalDate getDtFrom() {
        return dtFrom;
    }

    public void setDtFrom(LocalDate dtFrom) {
        this.dtFrom = dtFrom;
    }
}
