package com.bee.backend.domain.security;

import com.bee.backend.domain.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="bee_users_relation")
public class BeeUsersRelation extends BaseEntity  {
    private static final long serialVersionUID = 1L;
    //Пользователь кто включил в семью
    @ManyToOne
    @JoinColumn(name = "parent_user_id")
    private BeeUsers beeParentUsers;
    //Пользователь который состоит в семье
    @ManyToOne
    @JoinColumn(name = "child_user_id")
    private BeeUsers beeChildUsers;

    public BeeUsersRelation() {
    }

    public BeeUsersRelation(BeeUsers beeParentUsers, BeeUsers beeChildUsers) {
        this.beeParentUsers = beeParentUsers;
        this.beeChildUsers = beeChildUsers;
    }

    public BeeUsers getBeeParentUsers() {
        return beeParentUsers;
    }

    public void setBeeParentUsers(BeeUsers beeParentUsers) {
        this.beeParentUsers = beeParentUsers;
    }

    public BeeUsers getBeeChildUsers() {
        return beeChildUsers;
    }

    public void setBeeChildUsers(BeeUsers beeChildUsers) {
        this.beeChildUsers = beeChildUsers;
    }

    //https://www.mkyong.com/java/java-how-to-overrides-equals-and-hashcode/
  /*  @Override
    public boolean equals(Object o) {

        if (o == this) return true;
        if (!(o instanceof BeeUsersRelation)) {
            return false;
        }
        BeeUsersRelation user = (BeeUsersRelation) o;
        return Objects.equals(beeParentUsers, user.beeParentUsers) &&
                Objects.equals(beeChildUsers, user.beeChildUsers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(beeParentUsers.getId(), beeChildUsers.getId());
    }*/
}
