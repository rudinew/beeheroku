package com.bee.backend.domain.data;

import com.bee.backend.domain.BaseEntity;
import com.bee.backend.domain.security.BeeUsers;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

/**
 * Клієнти - аватар
 */
@Entity
@Table(name="bee_person_file")
public class BeePersonFile extends BaseEntity {
    private static final long serialVersionUID = 1L;

    //a MEDIUMBLOB for 16777215 bytes (16 MB)
    //a LONGBLOB for 4294967295 bytes (4 GB).
    //@Lob
    @Column(length=4194304, nullable = false) //
    private byte[] file;

    @Column()
    private String name;

    @Column()
    private String size;

    @Column()
    private String type;
    //Пользователь
     @ManyToOne
     @JoinColumn(name = "user_id")
     private BeeUsers beeUsers;
    @Column(name = "dt_from")
    // @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    //дата загрузки
    private LocalDate dtFrom;
    @ManyToOne
    @JoinColumn(name = "person_id")
    private BeePerson beePerson;

    public BeePersonFile() {
        this.dtFrom = new LocalDate();
    }

    public BeePersonFile(byte[] file, String name, String size, String type, BeePerson beePerson, BeeUsers beeUsers) {
        this.file = file;
        this.name = name;
        this.size = size;
        this.type = type;

        this.dtFrom = new LocalDate();
        this.beePerson = beePerson;

        this.beeUsers = beeUsers;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getDtFrom() {
        return dtFrom;
    }

    public void setDtFrom(LocalDate dtFrom) {
        this.dtFrom = dtFrom;
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
}
