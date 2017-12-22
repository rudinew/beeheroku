package com.bee.backend.repositories.data;

import com.bee.backend.domain.data.BeeDocNotification;
import com.bee.backend.domain.security.BeeUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//import com.bee.web.domain.PersonEmtyDocPojo;


@Repository
public interface BeeDocNotificationRepository extends JpaRepository<BeeDocNotification, Long> {
    //,  JpaSpecificationExecutor<BeeDocNotification>
//QueryDslPredicateExecutor<BeeDocNotification>
    List<BeeDocNotification> findAll();
    List<BeeDocNotification> findAllByBeeUsers(BeeUsers beeUsers);

 /*   @Query("select new com.bee.web.domain.PersonEmtyDocPojo(p.id, p.lastname, p.firstname, p.middlename, n.beeDocType.id, n.beeDocType.name) " +
            "from BeeDocNotification n, BeePerson p" +
            " where n.beeUsers.id = :beeUsersId" // +
         // не поняла как ин  "  and p.beeAccessPersons.id = :beeAccessPersons"
            )
    Collection<PersonEmtyDocPojo> findPersonEmtyDocPojo(@Param("beeUsersId") Long beeUsersId); //, @Param("beeAccessPersons")List<Long> beeAccessPersons
*/

    /*JPQL*/
  //  List<PersonEmtyDocPojo> findPersonEmtyDocPojo2(BeeUsers beeUsers, List<BeeAccessPerson> beeAccessPersons);
    /* {
       String queryString = "select  new com.bee.web.domain.PersonEmtyDocPojo(p.id, p.lastname, p.firstname, p.middlename, n.beeDocType.id, n.beeDocType.name) " +
         "from BeeDocNotification n, BeePerson p" +
                 " where n.beeUsers.id = :beeUsersId";
        Query query = getEntityManager().createQuery(queryString);

        query.setParameter("lastName", StringUtils.lowerCase(lastName));
        return query.getResultList();
    }*/




    /* @Query("SELECT t FROM Todo t WHERE " +
            "LOWER(t.title) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR " +
            "LOWER(t.description) LIKE LOWER(CONCAT('%',:searchTerm, '%'))")
    List<Todo> findBySearchTerm(@Param("searchTerm") String searchTerm);*/

    /* this.personId = personId;
        this.lastname = lastname;
        this.middlename = middlename;
        this.firstname = firstname;
        this.typeId = typeId;
        this.typename = typename;*/

 /*   @Query(value = "SELECT beeDocType.name FROM BeeDocNotification", nativeQuery = true)
   List<Object[]> findDocType();*/


  //  List<PersonEmtyDocPojo> findAllString(Specification<PersonEmtyDocPojo> specification);

/*JPQL*/
   /* List<String> getPersonAndEmptyDocType(BeeUsers beeUsers);*/
    /* {
       String queryString = "SELECT a FROM Author a " +
                "WHERE LOWER(a.lastName) = :lastName";
        Query query = getEntityManager().createQuery(queryString);

        query.setParameter("lastName", StringUtils.lowerCase(lastName));
        return query.getResultList();
    }*/
    /*
    * ОПОВІЩЕННЯ
select t.id, t.name, p.L_NAME, p.F_NAME
from dbdiplom.bee_document_type t,
     dbdiplom.bee_person p,
     bee_doc_notification n
 where n.type_id = t.id
       and n.user_id = 1
       and n.is_required = 1
       and (p.id, n.type_id) not in
       (select d.Person_id, d.type_id
          from dbdiplom.bee_document d)
          */
   /* @Query("SELECT u FROM BeeUsers u where u.login = :email and u.is_active = 1")
    List<BeeUsers> findByEmail(@Param("email") String email);*/

    /*Решения
    * sqlresultsetmapping spring data jpa example
    * https://stackoverflow.com/questions/6180214/how-to-select-multiple-columns-with-the-same-name-using-jpa-native-query
    *
    *
    * https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods
    *   @Query("select u.id, LENGTH(u.firstname) as fn_len from User u where u.lastname like ?1%")
  List<Object[]> findByAsArrayAndSort(String lastname, Sort sort);
  @Query("select u from User u where u.firstname = :firstname or u.lastname = :lastname")
  User findByLastnameOrFirstname(@Param("lastname") String lastname,
                                 @Param("firstname") String firstname);*/


}


