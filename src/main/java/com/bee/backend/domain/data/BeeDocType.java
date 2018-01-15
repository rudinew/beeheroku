package com.bee.backend.domain.data;

import com.bee.backend.domain.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="bee_document_type")
public class BeeDocType extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String name;

    private String alias;

    private Boolean is_required;

   /* @OneToMany(mappedBy="beeDocType", fetch = FetchType.EAGER)
    private List<BeeDocument> beeDocuments = new ArrayList<BeeDocument>();

    public List<BeeDocument> getBeeDocuments() {
        return beeDocuments;
    }

    public void setBeeDocuments(List<BeeDocument> beeDocuments) {
        this.beeDocuments = beeDocuments;
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Boolean getIs_required() {
        return is_required;
    }

    public void setIs_required(Boolean is_required) {
        this.is_required = is_required;
    }
}


