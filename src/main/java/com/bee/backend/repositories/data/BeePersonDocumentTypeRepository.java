package com.bee.backend.repositories.data;

import com.bee.backend.domain.data.BeeDocType;
import com.bee.backend.domain.data.BeePerson;
import com.bee.backend.domain.data.BeePersonDocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BeePersonDocumentTypeRepository extends JpaRepository<BeePersonDocumentType, Long> {
    List<BeePersonDocumentType> findAll();

    List<BeePersonDocumentType> findByBeePerson(BeePerson beePerson);
    List<BeePersonDocumentType> findByBeePersonIn(List<BeePerson> beePersons);
    List<BeePersonDocumentType> findFirst5ByBeePersonIn(List<BeePerson> beePersons);
    Long countByBeePersonIn(List<BeePerson> beePersons);
    List<BeePersonDocumentType> findByBeeDocType(BeeDocType beeDocType);

    List<BeePersonDocumentType> findByBeePersonAndBeeDocType(BeePerson beePerson, BeeDocType beeDocType);

}
