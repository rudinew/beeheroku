package com.bee.backend.domain.data;

import com.bee.backend.domain.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="bee_citizenship")
public class BeeCitizenship extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String name;

/*    @OneToMany(mappedBy="beeCitizenship")
    private List<BeePerson> persons = new ArrayList<BeePerson>();
*/
    public BeeCitizenship(String name) {
        this.name = name;
    }

    public BeeCitizenship() {
    }

 /*   public List<BeePerson> getPersons() {
        return persons;
    }

    public void setPersons(List<BeePerson> persons) {
        this.persons = persons;
    }
*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
