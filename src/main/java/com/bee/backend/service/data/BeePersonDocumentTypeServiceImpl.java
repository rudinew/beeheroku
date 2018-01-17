package com.bee.backend.service.data;

import com.bee.backend.domain.data.BeeDocType;
import com.bee.backend.domain.data.BeePerson;
import com.bee.backend.domain.data.BeePersonDocumentType;
import com.bee.backend.repositories.data.BeePersonDocumentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BeePersonDocumentTypeServiceImpl implements BeePersonDocumentTypeService {
    @Autowired
    BeePersonDocumentTypeRepository beePersonDocumentTypeRepository;

    @Override
    public BeePersonDocumentType getBeePersonDocumentTypeByOne(Long cId) {
        return beePersonDocumentTypeRepository.findOne(cId);
    }

    @Override
    public List<BeePersonDocumentType> getBeePersonDocumentTypeAll() {
        return beePersonDocumentTypeRepository.findAll();
    }

    @Override
    public List<BeePersonDocumentType> getBeePersonDocumentTypeByBeePerson(BeePerson beePerson) {
        return beePersonDocumentTypeRepository.findByBeePerson(beePerson);
    }
    @Override
    public List<BeePersonDocumentType> getBeePersonDocumentTypeByBeePersonIn(List<BeePerson> beePersons) {
        return beePersonDocumentTypeRepository.findByBeePersonIn(beePersons);
    }

    @Override
    public List<BeePersonDocumentType> getBeePersonDocumentTypeFirst5ByBeePersonIn(List<BeePerson> beePersons) {
        return beePersonDocumentTypeRepository.findFirst5ByBeePersonIn(beePersons);
    }

    @Override
    public Long countByBeePersonIn(List<BeePerson> beePersons) {
        return beePersonDocumentTypeRepository.countByBeePersonIn(beePersons);
    }

    @Override
    public List<BeePersonDocumentType> getBeePersonDocumentTypeByBeeDocType(BeeDocType beeDocType) {
        return beePersonDocumentTypeRepository.findByBeeDocType(beeDocType);
    }

    @Override
    public List<BeePersonDocumentType> getBeePersonDocumentTypeByBeePersonAndBeeDocType(BeePerson beePerson, BeeDocType beeDocType) {
        return beePersonDocumentTypeRepository.findByBeePersonAndBeeDocType(beePerson, beeDocType);
    }

    @Override
    public void saveBeePersonDocumentType(BeePersonDocumentType beePersonDocumentType) {
        beePersonDocumentTypeRepository.save(beePersonDocumentType);
    }

    @Override
    public void saveAndFlushBeePersonDocumentType(BeePersonDocumentType beePersonDocumentType) {
        beePersonDocumentTypeRepository.saveAndFlush(beePersonDocumentType);
    }

    @Override
    public void deleteBeePersonDocumentType(Long cId) {
        beePersonDocumentTypeRepository.delete(cId);
    }
}
