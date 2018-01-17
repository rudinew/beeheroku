package com.bee.backend.service.criteria;


import com.bee.backend.domain.data.BeeDocument;
import com.bee.backend.domain.data.BeePerson;

import java.util.List;

/**
 * Created by Rudoman on 23.08.2017.
 */
public interface BeeDocumentSearchService {
    List<BeeDocument> findBySearchTerm(String searchTerm, List<BeePerson> beePerson);
}
