package com.bee.backend.service.criteria;

import com.bee.backend.domain.data.BeeDocument;
import com.bee.backend.domain.data.BeePerson;
import com.bee.backend.repositories.data.BeeDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Rudoman on 23.08.2017.
 */
@Service
final public class BeeDocumentSearchServiceImpl implements BeeDocumentSearchService {
    private final BeeDocumentRepository beeDocumentRepository;

    @Autowired
    public BeeDocumentSearchServiceImpl(BeeDocumentRepository beeDocumentRepository) {
        this.beeDocumentRepository = beeDocumentRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<BeeDocument> findBySearchTerm(String searchTerm, List<BeePerson> beePerson) {
        Specification<BeeDocument> searchSpec = BeeDocumentSpecifications.docTypeOrNumOrSeriesContainsIgnoreCase(searchTerm, beePerson);
        List<BeeDocument> searchResults = beeDocumentRepository.findAll(searchSpec);
        return searchResults;
    }
}
