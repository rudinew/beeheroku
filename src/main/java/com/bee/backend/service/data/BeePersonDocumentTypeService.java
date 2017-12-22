package com.bee.backend.service.data;

import com.bee.backend.domain.data.BeeDocType;
import com.bee.backend.domain.data.BeePerson;
import com.bee.backend.domain.data.BeePersonDocumentType;

import java.util.List;

public interface BeePersonDocumentTypeService {

    BeePersonDocumentType getBeePersonDocumentTypeByOne(Long cId);
    List<BeePersonDocumentType> getBeePersonDocumentTypeAll();
    List<BeePersonDocumentType> getBeePersonDocumentTypeByBeePerson(BeePerson beePerson);
    List<BeePersonDocumentType> getBeePersonDocumentTypeByBeePersonIn(List<BeePerson> beePersons);
    List<BeePersonDocumentType> getBeePersonDocumentTypeFirst5ByBeePersonIn(List<BeePerson> beePersons);
    Long countByBeePersonIn(List<BeePerson> beePersons);
    List<BeePersonDocumentType> getBeePersonDocumentTypeByBeeDocType(BeeDocType beeDocType);
    List<BeePersonDocumentType> getBeePersonDocumentTypeByBeePersonAndBeeDocType(BeePerson beePerson, BeeDocType beeDocType);

    void saveBeePersonDocumentType(BeePersonDocumentType beePersonDocumentType);
    void saveAndFlushBeePersonDocumentType(BeePersonDocumentType beePersonDocumentType);

    void deleteBeePersonDocumentType(Long cId);

}
