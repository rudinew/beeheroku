package com.bee.backend.service.security;


import com.bee.backend.domain.security.BeeUsers;
import com.bee.backend.domain.security.BeeUsersRelation;
import com.bee.backend.repositories.security.UsersRelationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsersRelationServiceImpl implements UsersRelationService {
    @Autowired
    private UsersRelationRepository usersRelationRepository;

    @Override
    public BeeUsersRelation getBeeUsersRelationByOne(Long cId) {
        return usersRelationRepository.findOne(cId);
    }

    @Override
    public List<BeeUsersRelation> findByBeeParentUsers(BeeUsers beeUsers) {
        return usersRelationRepository.findByBeeParentUsers(beeUsers);
    }

    @Override
    @Transactional
    public void saveBeeUsersRelation(BeeUsersRelation beeUsersRelation) {
        usersRelationRepository.save(beeUsersRelation);
    }

    @Override
    public List<BeeUsersRelation> findAll() {
        return usersRelationRepository.findAll();
    }

    @Override
    @Transactional
    public void saveAndFlushBeeUsersRelation(BeeUsersRelation beeUsersRelation) {
        usersRelationRepository.saveAndFlush(beeUsersRelation);
    }

    @Override
    @Transactional
    public void deleteBeeUsersRelation(long userId) {
        usersRelationRepository.delete(userId);
    }
}
