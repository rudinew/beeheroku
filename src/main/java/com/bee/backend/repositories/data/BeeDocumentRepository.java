package com.bee.backend.repositories.data;

import com.bee.backend.domain.data.BeeDocType;
import com.bee.backend.domain.data.BeeDocument;
import com.bee.backend.domain.data.BeePerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BeeDocumentRepository extends JpaRepository<BeeDocument, Long>, JpaSpecificationExecutor<BeeDocument> {
    List<BeeDocument> findAll();

    List<BeeDocument> findByBeePerson(BeePerson beePerson);

    List<BeeDocument> findByBeeDocType(BeeDocType beeDocType);

    List<BeeDocument> findByBeePersonAndBeeDocType(BeePerson beePerson, BeeDocType beeDocType);

}
