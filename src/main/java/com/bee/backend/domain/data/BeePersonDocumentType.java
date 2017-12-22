package com.bee.backend.domain.data;

import com.bee.backend.domain.BaseEntity;

import javax.persistence.*;

///Таблиця для оповіщень
//якщо в ній є запис особова картка + тип документу, то даний тип документу не внесено
@Entity
@Table(name="bee_person_document_type")
public class BeePersonDocumentType extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "person_id")
    private BeePerson beePerson;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_id")
    private BeeDocType beeDocType;

    public BeePersonDocumentType() {
    }

    public BeePersonDocumentType(BeePerson beePerson, BeeDocType beeDocType) {
        this.beePerson = beePerson;
        this.beeDocType = beeDocType;

    }

    public BeePerson getBeePerson() {
        return beePerson;
    }

    public void setBeePerson(BeePerson beePerson) {
        this.beePerson = beePerson;
    }

    public BeeDocType getBeeDocType() {
        return beeDocType;
    }

    public void setBeeDocType(BeeDocType beeDocType) {
        this.beeDocType = beeDocType;
    }


}
