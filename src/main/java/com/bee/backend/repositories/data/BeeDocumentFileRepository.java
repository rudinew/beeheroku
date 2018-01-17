package com.bee.backend.repositories.data;

import com.bee.backend.domain.data.BeeDocument;
import com.bee.backend.domain.data.BeeDocumentFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BeeDocumentFileRepository extends JpaRepository<BeeDocumentFile, Long> {
    List<BeeDocumentFile> findByBeeDocument(BeeDocument beeDocument);
}
