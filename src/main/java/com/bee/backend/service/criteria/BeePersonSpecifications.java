package com.bee.backend.service.criteria;

import com.bee.backend.domain.data.BeePerson;
import com.bee.backend.domain.data.BeePerson_;
import com.bee.backend.domain.security.BeeAccessPerson;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.List;

/**
 * CRITERIA еще разобрать
 * https://www.petrikainulainen.net/programming/spring-framework/spring-data-jpa-tutorial-part-four-jpa-criteria-queries/
 */
final public class BeePersonSpecifications {
    private BeePersonSpecifications() {}

    static Specification<BeePerson> firstnameOrLastnameContainsIgnoreCase(String searchTerm, List<BeeAccessPerson> beeAccessPerson) {
      /*Interface Specification<T>
      toPredicate(javax.persistence.criteria.Root<T> root, javax.persistence.criteria.CriteriaQuery<?> query, javax.persistence.criteria.CriteriaBuilder cb)
Creates a WHERE clause for a query of the referenced entity in form of a Predicate for the given Root and CriteriaQuery.*/
        return (root, query, cb) -> {
            Join<BeePerson, BeeAccessPerson> beePersonBeeAccessPersonSetJoin = root.join(BeePerson_.beeAccessPersons);
            Predicate beePersonSpecification = beePersonBeeAccessPersonSetJoin.in(beeAccessPerson);
            String containsLikePattern = getContainsLikePattern(searchTerm);
            //final Join<PersonEntity, AddressEntity> addresses = root.join(PersonEntity.address, JoinType.LEFT);
           query.distinct(true);
           query.where(beePersonSpecification, cb.or(
                    cb.like(cb.lower(root.<String>get(BeePerson_.firstname)), containsLikePattern),
                    cb.like(cb.lower(root.<String>get(BeePerson_.middlename)), containsLikePattern),
                    cb.like(cb.lower(root.<String>get(BeePerson_.lastname)), containsLikePattern)


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
