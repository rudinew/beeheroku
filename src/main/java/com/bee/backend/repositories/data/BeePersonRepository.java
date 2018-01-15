package com.bee.backend.repositories.data;

import com.bee.backend.domain.data.BeePerson;
import com.bee.backend.domain.security.BeeAccessPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

//https://www.petrikainulainen.net/programming/spring-framework/spring-data-jpa-tutorial-part-four-jpa-criteria-queries/

@Repository
public interface BeePersonRepository extends JpaRepository<BeePerson, Long>, JpaSpecificationExecutor<BeePerson> {
/*@Query("SELECT u FROM BeePerson u where u.name = :cName")
BeePerson findByName(@Param("cName") String cName);*/
       BeePerson findByLastname(String lastname);


        List<BeePerson> findAll();
       //http://docs.spring.io/spring-data/jpa/docs/1.10.4.RELEASE/reference/html/#repositories.query-methods.details
        // Enabling static ORDER BY for a query
     /*  @Query(value = "SELECT p FROM BeePerson p " +
               "WHERE (p.user_id = :userId or p.beeAccessPersons.user_id = :userId)" +
               "ORDER BY p.l_name Asc")*/
    //findDistinctPeopleByLastnameOrFirstname
     //  List<BeePerson> findAllByBeeUsersOrBeeAccessPersonsOrderByLastnameAsc(@Param("userId") long userId);
       List<BeePerson> findAllByOrderByLastnameAsc();
      //BeeAccessPerson
      List<BeePerson> findAllByBeeAccessPersonsInOrderByLastnameAsc(List<BeeAccessPerson> beeAccessPerson);
    BeePerson findByIdAndBeeAccessPersonsIn(Long cId, List<BeeAccessPerson> beeAccessPerson);
    List<BeePerson> findAllByFirstnameLikeOrLastnameLikeAndBeeAccessPersonsIn(String cFirstname,
                                                                              String cLastname,
                                                                              List<BeeAccessPerson> beeAccessPerson);


}


