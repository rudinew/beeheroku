package com.bee.backend.service.security;


import com.bee.backend.domain.security.BeeUsers;
import com.bee.backend.domain.security.BeeUsersRelation;

import java.util.List;

public interface UsersRelationService {
    BeeUsersRelation getBeeUsersRelationByOne(Long cId);
    List<BeeUsersRelation> findByBeeParentUsers(BeeUsers beeUsers);
    List<BeeUsersRelation> findAll();

    void saveBeeUsersRelation(BeeUsersRelation beeUsersRelation);
    void saveAndFlushBeeUsersRelation(BeeUsersRelation beeUsersRelation);
    void deleteBeeUsersRelation(long userId);
}