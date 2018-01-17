package com.bee.backend.service.criteria;

import com.bee.backend.domain.data.BeePerson;
import com.bee.backend.domain.security.BeeAccessPerson;
import com.bee.backend.repositories.data.BeePersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Rudoman on 22.08.2017.
 */
@Service
final public class BeePersonSearchServiceImpl implements BeePersonSearchService {
    private final BeePersonRepository beePersonRepository;

    @Autowired
    public BeePersonSearchServiceImpl(BeePersonRepository beePersonRepository) {
        this.beePersonRepository = beePersonRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<BeePerson> findBySearchTerm(String searchTerm, List<BeeAccessPerson> beeAccessPerson) {
        Specification<BeePerson> searchSpec = BeePersonSpecifications.firstnameOrLastnameContainsIgnoreCase(searchTerm, beeAccessPerson);
        List<BeePerson> searchResults = beePersonRepository.findAll(searchSpec);
        return searchResults;
    }
}
