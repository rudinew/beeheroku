package com.bee.backend.service.data;

import com.bee.backend.domain.data.BeeDocNotification;
import com.bee.backend.domain.security.BeeUsers;

import java.util.List;

//import com.bee.web.domain.PersonEmtyDocPojo;

public interface BeeDocNotificationService {

    BeeDocNotification getBeeDocNotificationByOne(Long cId);
    List<BeeDocNotification> getBeeDocNotificationAll();
    List<BeeDocNotification> getBeeDocNotificationByUsers(BeeUsers users);



    void saveBeeDocNotification(BeeDocNotification beeDocNotification);
    void saveAndFlushBeeDocNotification(BeeDocNotification beeDocNotification);

  //  Collection<PersonEmtyDocPojo> findPersonEmtyDocPojo(Long beeUsersId, List<Long> beeAccessPersons);
    /*JPQL*/
 //   List<PersonEmtyDocPojo> findPersonEmtyDocPojo2(Long beeUsers, List<BeeAccessPerson> beeAccessPersons);


  //  List<Object[]> findDocType();

   // List<PersonEmtyDocPojo> getNot(BeeUsers users);
  //  void deleteAction(Long cId);

    /*JPQL*/
 /*   List<String> getPersonAndEmptyDocType(BeeUsers beeUsers);*/



}

