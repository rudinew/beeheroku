package com.bee.backend.repositories.data;

import com.bee.backend.domain.data.BeeDocType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BeeDocTypeRepository extends JpaRepository<BeeDocType, Long> {
    @Query("SELECT u FROM BeeDocType u where u.name = :cName")
    BeeDocType findByName(@Param("cName") String cName);

    BeeDocType findByAlias(String cAlias);

    List<BeeDocType> findAll();
}
