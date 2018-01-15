package com.bee.backend.repositories.security;


import com.bee.backend.domain.security.BeeUsers;
import com.bee.backend.domain.security.BeeUsersRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface UsersRelationRepository extends JpaRepository<BeeUsersRelation, Long> {
    List<BeeUsersRelation> findByBeeParentUsers(BeeUsers beeUsers);
    List<BeeUsersRelation> findAll();

}
