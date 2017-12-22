package com.bee.backend.service.data;


import com.bee.backend.domain.data.BeeCitizenship;
import com.bee.backend.domain.data.BeeDocType;
import com.bee.backend.repositories.data.BeeCitizenshipRepository;
import com.bee.backend.repositories.data.BeeDocTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class BeeTypeServiceImpl implements BeeTypeService {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private BeeCitizenshipRepository beeCitizenshipRepository;
    @Autowired
    private BeeDocTypeRepository beeDocTypeRepository;
   /* @Autowired
    private BeePassportTypeRepository beePassportTypeRepository;

    @Autowired
    private BeeCertificateTypeRepository beeCertificateTypeRepository;*/

    //beeCitizenshipRepository
 /*   @Override
    public Page<BeeCitizenship> getBeeCitizenshipPage(Integer pageNumber) {
        PageRequest request = new PageRequest(pageNumber - 1, PAGE_SIZE, Sort.Direction.DESC, "name");
        return beeCitizenshipRepository.findAll(request);
    }*/

    @Override
    public BeeCitizenship getBeeCitizenshipByName(String cName) {
        return beeCitizenshipRepository.findByName(cName);
    }


    @Override
    public BeeCitizenship getBeeCitizenshipByOne(Long cId) {
        return beeCitizenshipRepository.findOne(cId);
    }

    @Override
    public List<BeeCitizenship> getBeeCitizenshipAll() {
        return beeCitizenshipRepository.findAll();
    }

    @Override
    @Transactional
    public void saveBeeCitizenship(BeeCitizenship beeCitizenship) {
        beeCitizenshipRepository.save(beeCitizenship);

    }

    @Override
    @Transactional
    public void saveAndFlushBeeCitizenship(BeeCitizenship beeCitizenship) {
        beeCitizenshipRepository.saveAndFlush(beeCitizenship);
    }

    @Override
    @Transactional
    public void deleteBeeCitizenship(Long cId) {
        beeCitizenshipRepository.delete(cId);
    }

    //beeDocTypeRepository
    @Override
    public BeeDocType getBeeDocTypeByName(String cName) {
        return beeDocTypeRepository.findByName(cName);
    }

    @Override
    public BeeDocType getBeeDocTypeByAlias(String cAlias) {
        return beeDocTypeRepository.findByAlias(cAlias);
    }

    @Override
    public BeeDocType getBeeDocTypeByOne(Long cId) {
        return beeDocTypeRepository.findOne(cId);
    }

    @Override
    public List<BeeDocType> getBeeDocTypeAll() {
        return beeDocTypeRepository.findAll();
    }

    @Override
    @Transactional
    public void saveBeeDocType(BeeDocType beeDocType) {
        beeDocTypeRepository.save(beeDocType);
    }

    @Override
    @Transactional
    public void saveAndFlushBeeDocType(BeeDocType beeDocType) {
        beeDocTypeRepository.saveAndFlush(beeDocType);
    }

    @Override
    @Transactional
    public void deleteBeeDocType(Long cId) {
        beeDocTypeRepository.delete(cId);
    }
}
