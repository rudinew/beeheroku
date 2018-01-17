package com.bee.backend.domain.data;

import com.bee.backend.domain.BaseEntity;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

@Entity
@Table(name="bee_document_file")
public class BeeDocumentFile extends BaseEntity {
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
   // @ManyToOne
   // @JoinColumn(name = "user_id")
    //private BeeUsers beeUsers;
    //дата загрузки
    @Column(name = "dt_from")
    // @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate dtFrom;

    @ManyToOne
    @JoinColumn(name = "document_id")
    private BeeDocument beeDocument;

    public BeeDocumentFile() {
        this.dtFrom = new LocalDate();
    }

    public BeeDocumentFile(byte[] file, String name, String size, String type, BeeDocument beeDocument) {
        this.file = file;
        this.name = name;
        this.size = size;
        this.type = type;

        this.dtFrom = new LocalDate();
        this.beeDocument = beeDocument;
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

    public BeeDocument getBeeDocument() {
        return beeDocument;
    }

    public void setBeeDocument(BeeDocument beeDocument) {
        this.beeDocument = beeDocument;
    }
}
