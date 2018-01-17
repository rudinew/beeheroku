package com.bee.backend.domain.data;

import com.bee.backend.domain.BaseEntity;
import com.bee.backend.domain.security.BeeUsers;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="bee_document")
public class BeeDocument extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String series;
    private String num;
    @Column(name = "dt_start")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate dtStart;  //DateTime -  Immutable replacement for JDK Calendar
    // LocalDate is a good class to use to represent a date of birth
    //http://www.joda.org/joda-time/quickstart.html
    @Column(name = "dt_end")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate dtEnd;
    private String vydano;
    private String description;
    @ManyToOne
    @JoinColumn(name = "person_id")
    private BeePerson beePerson;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private BeeDocType beeDocType;

    //Пользователь
    @ManyToOne
    @JoinColumn(name = "user_id")
    private BeeUsers beeUsers;
    //дата загрузки
    @Column(name = "dt_from")
    // @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate dtFrom;

    //PHOTO
    @OneToMany(mappedBy="beeDocument", cascade=CascadeType.REMOVE)
    @Fetch(value = FetchMode.SUBSELECT)
    //when private then in indexOLD.html - Property or field 'beeTaxes' cannot be found on object of type 'com.bee.domain.BeePerson' - maybe not public?
    public List<BeeDocumentFile> beeDocumentFiles = new ArrayList<BeeDocumentFile>();
    //from petclinic
    //protected
    public  List<BeeDocumentFile> getBeeDocumentFiles() {
        if (this.beeDocumentFiles == null) {
            this.beeDocumentFiles = new ArrayList<BeeDocumentFile>();
        }
        return this.beeDocumentFiles;
    }

    public void setBeeDocumentFile(BeeDocumentFile beeDocumentFile) {
        getBeeDocumentFiles().add(beeDocumentFile);
        beeDocumentFile.setBeeDocument(this);
    }

    public int getNrOfBeeDocumentFiles() {
        return getBeeDocumentFiles().size();
    }
    ////

    public BeeDocument() {
        this.dtFrom = new LocalDate();
    }

    public BeeDocument(String series, String num, LocalDate dtStart, LocalDate dtEnd, String description, String vydano, BeePerson beePerson, BeeDocType beeDocType) {
        this.num = num;
        this.series = series;
        this.dtStart = dtStart;
        this.dtEnd = dtEnd;
        this.description = description;
        this.vydano = vydano;
        this.beePerson = beePerson;
        this.beeDocType = beeDocType;
       // this.beeUsers = beeUsers;
        this.dtFrom = new LocalDate();
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public LocalDate getDtStart() {
        return dtStart;
    }

    public void setDtStart(LocalDate dtStart) {
        this.dtStart = dtStart;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public LocalDate getDtEnd() {
        return dtEnd;
    }

    public void setDtEnd(LocalDate dtEnd) {
        this.dtEnd = dtEnd;
    }

    public String getVydano() {
        return vydano;
    }

    public void setVydano(String vydano) {
        this.vydano = vydano;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public BeeUsers getBeeUsers() {
        return beeUsers;
    }

    public void setBeeUsers(BeeUsers beeUsers) {
        this.beeUsers = beeUsers;
    }

    public LocalDate getDtFrom() {
        return dtFrom;
    }

    public void setDtFrom(LocalDate dtFrom) {
        this.dtFrom = dtFrom;
    }
}
