package com.bee.backend.repositories.data;

import com.bee.backend.domain.data.BeeCitizenship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BeeCitizenshipRepository extends JpaRepository<BeeCitizenship, Long> {
    @Query("SELECT u FROM BeeCitizenship u where u.name = :cName")
    BeeCitizenship findByName(@Param("cName") String cName);

    List<BeeCitizenship> findAll();

}
