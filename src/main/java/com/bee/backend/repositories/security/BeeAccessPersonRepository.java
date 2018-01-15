package com.bee.backend.repositories.security;


import com.bee.backend.domain.data.BeePerson;
import com.bee.backend.domain.security.BeeAccessPerson;
import com.bee.backend.domain.security.BeeUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BeeAccessPersonRepository extends JpaRepository<BeeAccessPerson, Long> {

     List<BeeAccessPerson> findAll();

     List<BeeAccessPerson> findAllByBeePerson(BeePerson beePerson);

     List<BeeAccessPerson> findAllByBeeUsers(BeeUsers users);




}


