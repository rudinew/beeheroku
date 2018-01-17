package com.bee.backend.repositories.data;

import com.bee.backend.domain.data.BeeDocNotification;
import com.bee.backend.domain.security.BeeUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;




@Repository
public interface BeeDocNotificationRepository extends JpaRepository<BeeDocNotification, Long> {

    List<BeeDocNotification> findAll();
    List<BeeDocNotification> findAllByBeeUsers(BeeUsers beeUsers);



}


