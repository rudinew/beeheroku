package com.bee.backend.service.criteria;


import com.bee.backend.domain.data.BeeDocument;
import com.bee.backend.domain.data.BeeDocument_;
import com.bee.backend.domain.data.BeePerson;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.List;

final public class BeeDocumentSpecifications {
    private BeeDocumentSpecifications() {}

    /*
    SELECT p.L_NAME, d.*
FROM dbdiplom.bee_document d,
     dbdiplom.bee_person p,
     dbdiplom.bee_access_person a
where d.person_id = p.id
  and p.id = a.person_id
  and a.user_id = 4
  and d.num like '%1%';
    * */

    static Specification<BeeDocument> docTypeOrNumOrSeriesContainsIgnoreCase(String searchTerm, List<BeePerson> beePerson) {
      /*Interface Specification<T>
      toPredicate(javax.persistence.criteria.Root<T> root, javax.persistence.criteria.CriteriaQuery<?> query, javax.persistence.criteria.CriteriaBuilder cb)
Creates a WHERE clause for a query of the referenced entity in form of a Predicate for the given Root and CriteriaQuery.*/
        return (root, query, cb) -> {
            Join<BeeDocument, BeePerson> beeDocumentBeePersonJoin = root.join(BeeDocument_.beePerson);
            Predicate beeDocumentSpecification = beeDocumentBeePersonJoin.in(beePerson);
            String containsLikePattern = getContainsLikePattern(searchTerm);
            //final Join<PersonEntity, AddressEntity> addresses = root.join(PersonEntity.address, JoinType.LEFT);
            query.distinct(true);
            query.where(beeDocumentSpecification, cb.or(
                  //  cb.like(cb.lower(root.<String>get(BeeDocument_.beeDocType.getName())), containsLikePattern),
                    cb.like(cb.lower(root.<String>get(BeeDocument_.num)), containsLikePattern),
                    cb.like(cb.lower(root.<String>get(BeeDocument_.series)), containsLikePattern)


            ));

            return query.getRestriction(); //cb.createQuery().getRestriction();
         /*   return cb.or(
                    cb.like(cb.lower(beePersonBeeAccessPersonSetJoin.<String>get(BeePerson_.firstname)), containsLikePattern),
                    cb.like(cb.lower(root.<String>get(BeePerson_.lastname)), containsLikePattern)

            );
*/
            // return beePersonSpecification;

           /* return cb.or(
                    cb.like(cb.lower(root.<String>get(BeePerson_.firstname)), containsLikePattern),
                    cb.like(cb.lower(root.<String>get(BeePerson_.lastname)), containsLikePattern)

            );*/
        };
    }

    private static String getContainsLikePattern(String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            return "%";
        }
        else {
            return "%" + searchTerm.toLowerCase() + "%";
        }
    }
}
