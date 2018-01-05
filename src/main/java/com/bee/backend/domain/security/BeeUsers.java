package com.bee.backend.domain.security;

import com.bee.backend.domain.BaseEntity;
import com.bee.web.validators.ValidEmail;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name="bee_users")
public class BeeUsers extends BaseEntity {
    private static final long serialVersionUID = 1L;
    @Size(min=1)
    @Column(unique=true)
    private String login;
    @NotNull
    @Size(min=1)
    @Column(name = "l_name")
    private String lastname;
    @NotNull
    @Size(min=1)
    @Column(name = "f_name")
    private String firstname;
    @NotNull
    @Size(min=1)
    @Column(name = "m_name")
    private String middlename;
    @NotNull
    @Column(name = "dt_birth")
    // @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate datebirth;
    private short gender;
    private String phone;
    @ValidEmail
    @Column(name = "e_mail")
    private String email;
    private String password;
    private String confirmPassword;
    //https://stackoverflow.com/questions/8038939/map-a-tinyint-as-boolean-hibernate
    @Column(name = "is_active", columnDefinition = "INTEGER")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean is_active;
    @Enumerated(EnumType.STRING)
    private UserRole role;


    //дата создания/модификации
    @Column(name = "dt_from")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate dtFrom;

    //Relation
 /*   @OneToMany(mappedBy="beeParentUsers", fetch = FetchType.LAZY)
    public List<BeeUsersRelation> beeUsersRelationsfromParent = new ArrayList<BeeUsersRelation>();

    public  List<BeeUsersRelation> getBeeUsersRelationsfromParent() {
        if (this.beeUsersRelationsfromParent == null) {
            this.beeUsersRelationsfromParent = new ArrayList<BeeUsersRelation>();
        }
        return this.beeUsersRelationsfromParent;
    }*/

    ///
    //BEEDOCNOTIFICATION
   /* @OneToMany(mappedBy="beeUsers", fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    public List<BeeDocNotification> beeDocNotifications = new ArrayList<BeeDocNotification>();
    public  List<BeeDocNotification> getBeeDocNotifications() {
        if (this.beeDocNotifications == null) {
            this.beeDocNotifications = new ArrayList<BeeDocNotification>();
        }
        return this.beeDocNotifications;
    }

    public void setBeeDocNotifications(BeeDocNotification beeDocNotification) {
        getBeeDocNotifications().add(beeDocNotification);
        beeDocNotification.setBeeUsers(this);
    }

    public int getNrOfBeeDocNotifications() {
        return getBeeDocNotifications().size();
    }
    */
    ///

    public BeeUsers() {
        this.dtFrom = new LocalDate();
    }

    public BeeUsers(String login, String lastname, String firstname, String middlename, LocalDate datebirth, short gender, String phone, String email, String password, String confirmPassword, Boolean is_active, UserRole role) {
        this.login = login;
        this.lastname = lastname;
        this.firstname = firstname;
        this.middlename = middlename;
        this.datebirth = datebirth;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.is_active = is_active;
        this.role = role;
        this.dtFrom = new LocalDate();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public Boolean getIs_active() {
        return is_active;
    }

    public void setIs_active(Boolean is_active) {
        this.is_active = is_active;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public LocalDate getDtFrom() {
        return dtFrom;
    }

    public void setDtFrom(LocalDate dtFrom) {
        this.dtFrom = dtFrom;
    }
//http://websystique.com/springmvc/springmvc-hibernate-many-to-many-example-annotation-using-join-table/
//https://www.mkyong.com/java/java-how-to-overrides-equals-and-hashcode/
/*@Override
public boolean equals(Object o) {

    if (o == this) return true;
    if (!(o instanceof BeeUsers)) {
        return false;
    }
    BeeUsers user = (BeeUsers) o;
    return Objects.equals(id, user.id) &&
            Objects.equals(login, user.login);
}

    @Override
    public int hashCode() {
        return Objects.hash(id, login);
    }*/
}
