package com.bee.backend.repositories.data;

import com.bee.backend.domain.data.BeePerson;
import com.bee.backend.domain.data.BeePersonFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BeePersonFileRepository extends JpaRepository<BeePersonFile, Long> {
    List<BeePersonFile> findByBeePerson(BeePerson beePerson);
}
