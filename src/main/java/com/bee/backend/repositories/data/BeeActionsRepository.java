package com.bee.backend.repositories.data;


import com.bee.backend.domain.data.BeeActions;
import com.bee.backend.domain.security.BeeUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BeeActionsRepository extends JpaRepository<BeeActions, Long> {

    //перевірка чи можна по користувачу
    List<BeeActions> findAllByBeeUsersOrderByDtFromAsc(BeeUsers user);

    List<BeeActions> findAllByOrderByDtFromAsc();
}
