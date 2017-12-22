package com.bee.backend.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
from PetClinic
 */
@MappedSuperclass
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //strategy = GenerationType.IDENTITY FOR MYSQL
    //GenerationType.AUTO)
    //@GeneratedValue(strategy = GenerationType.SEQUENCE) // FOR POSTGRESQL https://www.thoughts-on-java.org/hibernate-postgresql-5-things-need-know/
    //org.hibernate.HibernateException: Missing sequence or table: hibernate_sequenc
    private Long id;


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public boolean isNew() {

        return (this.id == null);
    }


}
