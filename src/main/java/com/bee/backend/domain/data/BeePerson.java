package com.bee.backend.domain.data;

import com.bee.backend.domain.BaseEntity;
import com.bee.backend.domain.security.BeeAccessPerson;
import com.bee.backend.domain.security.BeeUsers;
import com.bee.web.validators.ValidEmail;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name="bee_person")
public class BeePerson extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @NotNull
    @Column(name = "l_name")
    private String lastname;
    @NotNull
    @Column(name = "f_name")
    private String firstname;
    @NotNull
    @Column(name = "m_name")
    private String middlename;

    //from petclinic
    @NotNull
    @Column(name = "dt_birth")
    // @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate datebirth;  //DateTime -  Immutable replacement for JDK Calendar
    // LocalDate is a good class to use to represent a date of birth
    //http://www.joda.org/joda-time/quickstart.html

    private short gender;
    @Column(name = "place_birth")
    private String placebirth;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "citizenship_id")
    private BeeCitizenship beeCitizenship;
    //  @Enumerated(EnumType.STRING)
    //  private PersonGender gender;
    @Column(name = "family_status")
    private short familystatus;
    private String phone;
    @ValidEmail
    @Column(name = "e_mail")
    private String email;
    @Column(name = "social_networks")
    private String socialnetworks;
    @Column(name = "address_registration")
    private String addressregistration;
    @Column(name = "address_live")
    private String addresslive;
    //Пользователь
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private BeeUsers beeUsers;
    //дата создания/модификации
    @Column(name = "dt_from")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate dtFrom;

    //PHOTO
    @OneToMany(mappedBy="beePerson", fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    //when private then in indexOLD.html - Property or field 'beeTaxes' cannot be found on object of type 'com.bee.domain.BeePerson' - maybe not public?
    public List<BeePersonFile> beePersonFiles = new ArrayList<BeePersonFile>();
    //from petclinic
    //protected
    public  List<BeePersonFile> getBeePersonFiles() {
        if (this.beePersonFiles == null) {
            this.beePersonFiles = new ArrayList<BeePersonFile>();
        }
        return this.beePersonFiles;
    }

    public void setBeePersonFile(BeePersonFile beePersonFile) {
        getBeePersonFiles().add(beePersonFile);
        beePersonFile.setBeePerson(this);
    }

    public int getNrOfBeePersonFiles() {
        return getBeePersonFiles().size();
    }

    //BEEACCESSPERSON
    @OneToMany(mappedBy="beePerson", fetch = FetchType.EAGER)
    //https://stackoverflow.com/questions/13539050/entitynotfoundexception-in-hibernate-many-to-one-mapping-however-data-exist
    //when private then in indexOLD.html - Property or field 'beeTaxes' cannot be found on object of type 'com.bee.domain.BeePerson' - maybe not public?
    public List<BeeAccessPerson> beeAccessPersons = new ArrayList<BeeAccessPerson>();
    //from petclinic
    //protected
    public  List<BeeAccessPerson> getBeeAccessPersons() {
        if (this.beeAccessPersons == null) {
            this.beeAccessPersons = new ArrayList<BeeAccessPerson>();
        }
        return this.beeAccessPersons;
    }

    public int getNrOfBeeAccessPersons() {
        return getBeeAccessPersons().size();
    }
  /*  public void setbeeAccessPersons(BeeAccessPerson beeAccessPerson) {
        getBeeAccessPersons().add(beeAccessPerson);
        beeAccessPerson.setBeePerson(this);
    }*/



    public BeePerson() {
        this.dtFrom = new LocalDate();
    }

    public BeePerson(String lastname, String firstname, String middlename,
                     LocalDate datebirth, String placebirth, short gender,
                     short familystatus, String phone, String email, String socialnetworks ,
                     String addressregistration, String addresslive,
                     BeeCitizenship beeCitizenship
    ) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.middlename = middlename;
        this.datebirth = datebirth;
        this.placebirth = placebirth;
        this.gender = gender;
        this.familystatus = familystatus;
        this.phone = phone;
        this.email = email;
        this.socialnetworks = socialnetworks;
        this.beeCitizenship = beeCitizenship;
        this.dtFrom = new LocalDate();
        this.addressregistration = addressregistration;
        this.addresslive = addresslive;
        // this.beePhoto = beePhoto;
    }


    public BeePerson(String lastname, String firstname, String middlename, LocalDate datebirth, short gender, String phone) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.middlename = middlename;
        this.datebirth = datebirth;
        this.gender = gender;
        this.phone = phone;
    }


    public String getName() {
        return lastname + ' ' + firstname + ' ' + middlename;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public LocalDate getDatebirth() {
        return datebirth;
    }

    public void setDatebirth(LocalDate datebirth) {
        this.datebirth = datebirth;
    }

    public short getGender() {
        return gender;
    }

    public void setGender(short gender) {
        this.gender = gender;
    }

    public String getPlacebirth() {
        return placebirth;
    }

    public void setPlacebirth(String placebirth) {
        this.placebirth = placebirth;
    }

    public BeeCitizenship getBeeCitizenship() {
        return beeCitizenship;
    }

    public void setBeeCitizenship(BeeCitizenship beeCitizenship) {
        this.beeCitizenship = beeCitizenship;
    }

    public short getFamilystatus() {
        return familystatus;
    }

    public void setFamilystatus(short familystatus) {
        this.familystatus = familystatus;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSocialnetworks() {
        return socialnetworks;
    }

    public void setSocialnetworks(String socialnetworks) {
        this.socialnetworks = socialnetworks;
    }

    public String getAddressregistration() {
        return addressregistration;
    }

    public void setAddressregistration(String addressregistration) {
        this.addressregistration = addressregistration;
    }

    public String getAddresslive() {
        return addresslive;
    }

    public void setAddresslive(String addresslive) {
        this.addresslive = addresslive;
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
