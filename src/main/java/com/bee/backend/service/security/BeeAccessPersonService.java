package com.bee.backend.service.security;


import com.bee.backend.domain.data.BeePerson;
import com.bee.backend.domain.security.BeeAccessPerson;
import com.bee.backend.domain.security.BeeUsers;

import java.util.List;

public interface BeeAccessPersonService {
    //spring jpa data query
    //https://www.petrikainulainen.net/programming/spring-framework/spring-data-jpa-tutorial-introduction-to-query-methods/

    BeeAccessPerson getBeeAccessPersonByOne(Long cId);
    List<BeeAccessPerson> getBeeAccessPersonAll();
    List<BeeAccessPerson> getBeeAccessPersonByBeePerson(BeePerson beePerson);
    List<BeeAccessPerson> getBeeAccessPersonByBeeUsers(BeeUsers beeUsers);
    void saveBeeAccessPerson(BeeAccessPerson beeAccessPerson);
    void saveAndFlushBeeAccessPerson(BeeAccessPerson beeAccessPerson);
    void deleteBeeAccessPerson(Long cId);




}

