package com.bee.backend.service.criteria;

import com.bee.backend.domain.data.BeePerson;
import com.bee.backend.domain.security.BeeAccessPerson;

import java.util.List;

/**
 * https://www.petrikainulainen.net/programming/spring-framework/spring-data-jpa-tutorial-part-four-jpa-criteria-queries/
 */
public interface BeePersonSearchService {
    List<BeePerson> findBySearchTerm(String searchTerm, List<BeeAccessPerson> beeAccessPerson);
}
