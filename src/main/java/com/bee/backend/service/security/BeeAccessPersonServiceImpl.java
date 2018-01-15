package com.bee.backend.service.security;


import com.bee.backend.domain.data.BeePerson;
import com.bee.backend.domain.security.BeeAccessPerson;
import com.bee.backend.domain.security.BeeUsers;
import com.bee.backend.repositories.security.BeeAccessPersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BeeAccessPersonServiceImpl implements BeeAccessPersonService {


    @Autowired
    private BeeAccessPersonRepository beeAccessPersonRepository;


    @Override
    public BeeAccessPerson getBeeAccessPersonByOne(Long cId) {
        return beeAccessPersonRepository.findOne(cId);
    }

    @Override
    public List<BeeAccessPerson> getBeeAccessPersonAll() {
        return beeAccessPersonRepository.findAll();
    }

    @Override
    public List<BeeAccessPerson> getBeeAccessPersonByBeePerson(BeePerson beePerson) {
        return beeAccessPersonRepository.findAllByBeePerson(beePerson);
    }

    @Override
    public List<BeeAccessPerson> getBeeAccessPersonByBeeUsers(BeeUsers beeUsers) {
        return beeAccessPersonRepository.findAllByBeeUsers(beeUsers);
    }

    @Override
    @Transactional
    public void saveBeeAccessPerson(BeeAccessPerson beeAccessPerson) {
        beeAccessPersonRepository.save(beeAccessPerson);
    }

    @Override
    @Transactional
    public void saveAndFlushBeeAccessPerson(BeeAccessPerson beeAccessPerson) {
        beeAccessPersonRepository.saveAndFlush(beeAccessPerson);
    }

    @Override
    @Transactional
    public void deleteBeeAccessPerson(Long cId) {
        beeAccessPersonRepository.delete(cId);
    }
}
