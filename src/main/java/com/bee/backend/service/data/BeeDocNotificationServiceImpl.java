package com.bee.backend.service.data;

import com.bee.backend.domain.data.BeeDocNotification;
import com.bee.backend.domain.security.BeeUsers;
import com.bee.backend.repositories.data.BeeDocNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//import com.bee.web.domain.PersonEmtyDocPojo;

@Service
public class BeeDocNotificationServiceImpl implements BeeDocNotificationService {
//http://www.byteslounge.com/tutorials/spring-jpa-hibernate-example
///пробую
  //  @PersistenceContext
 //   private EntityManager entityManager;

    @Autowired
    private BeeDocNotificationRepository beeDocNotificationRepository;

    @Override
    public BeeDocNotification getBeeDocNotificationByOne(Long cId) {
        return beeDocNotificationRepository.findOne(cId);
    }

    @Override
    public List<BeeDocNotification> getBeeDocNotificationAll() {
        return beeDocNotificationRepository.findAll();
    }

    @Override
    public List<BeeDocNotification> getBeeDocNotificationByUsers(BeeUsers users) {
        return beeDocNotificationRepository.findAllByBeeUsers(users);
    }

    @Transactional
    @Override
    public void saveBeeDocNotification(BeeDocNotification beeDocNotification) {
        beeDocNotificationRepository.save(beeDocNotification);
    }
    @Transactional
    @Override
    public void saveAndFlushBeeDocNotification(BeeDocNotification beeDocNotification) {
        beeDocNotificationRepository.saveAndFlush(beeDocNotification);
    }
/*
    @Override
    public Collection<PersonEmtyDocPojo> findPersonEmtyDocPojo(Long beeUsersId, List<Long> beeAccessPersons) {
        return beeDocNotificationRepository.findPersonEmtyDocPojo(beeUsersId );
    }*/

   /* @Override
    public List<PersonEmtyDocPojo> findPersonEmtyDocPojo2(Long beeUsers, List<BeeAccessPerson> beeAccessPersons) {

            String queryString = "select  new com.bee.web.domain.PersonEmtyDocPojo(p.id, p.lastname, p.firstname, p.middlename, n.beeDocType.id, n.beeDocType.name) " +
                    "from BeeDocNotification n, BeePerson p" +
                    " where n.beeUsers.id = :beeUsersId";
            Query query = entityManager.createQuery(queryString);

            query.setParameter("beeUsersId", beeUsers);
            return query.getResultList();

    }*/

    ///пробую
  //  @Override
   // public List<PersonEmtyDocPojo> getNot(BeeUsers users) {
    /*    String queryString ="SELECT p.id, p.L_NAME, p.F_NAME, t.id, t.name"+
                "FROM BeeDocType t, BeePerson p, BeeDocNotification n"+
                "WHERE n.beeUsers = :beeUsers" +
                "  and n.is_required = 1" +
                "  and (p.id, n.type_id) not in" +
                "       (select d.Person_id, d.type_id" +
                "          from BeeDocument d)"   ;

        Query query = entityManager.createQuery(queryString);
        query.setParameter("beeUsers", users);*/


    //  query.setParameter("lastName", StringUtils.lowerCase(lastName));
    //    return query.getResultList();
      /*  CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BeeDocNotification> cq = builder.createQuery(BeeDocNotification.class);
        Root<BeeDocNotification> root = cq.from(BeeDocNotification.class);
        cq.select(root);
        return entityManager.createQuery(cq).getResultList();*/
    // }
  /*  @Override

    public List<PersonEmtyDocPojo> getNot(BeeUsers beeUsers) {
        return null;
    }*/

    /*@Override
    public List<Object[]> findDocType() {
        return beeDocNotificationRepository.findDocType();
    }*/
/*JPQL*/
    /*public List<String> getPersonAndEmptyDocType(BeeUsers beeUsers)
     {
       String queryString ="SELECT p.id, p.L_NAME, p.F_NAME, t.id, t.name"+
               "FROM BeeDocType t, BeePerson p, BeeDocNotification n"+
               "WHERE n.beeUsers = :beeUsers" +
               "  and n.is_required = 1" +
               "  and (p.id, n.type_id) not in" +
               "       (select d.Person_id, d.type_id" +
               "          from BeeDocument d)"   ;
        Query query = getEntityManager().createQuery(queryString);

        query.setParameter("lastName", StringUtils.lowerCase(lastName));
        return query.getResultList();
    }*/
    /*
    * ОПОВІЩЕННЯ
select t.id, t.name, p.L_NAME, p.F_NAME
FROM BeeDocType t, BeePerson p, BeeDocNotification n
 where n.type_id = t.id
       and n.user_id = 1
       and n.is_required = 1
       and (p.id, n.type_id) not in
       (select d.Person_id, d.type_id
          from dbdiplom.bee_document d)
          */
}
