package com.bee.backend.repositories.security;

import com.bee.backend.domain.security.BeeUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<BeeUsers, Long> {
    @Query("SELECT u FROM BeeUsers u where u.login = :login and u.is_active = 1")   //and u.is_active = 1 ли корректно
    BeeUsers findByLogin(@Param("login") String login);

    @Query("SELECT u FROM BeeUsers u where u.login = :email and u.is_active = 1")
    List<BeeUsers> findByEmail(@Param("email") String email);

    @Query("SELECT u FROM BeeUsers u where u.is_active = 1")
    List<BeeUsers> findAll();

    List<BeeUsers> findAllByLoginNotInOrderByLoginAsc(@Param("login") String login);

    @Transactional
    @Modifying
    @Query("update BeeUsers u set u.password = :password where u.id = :userId")
    void updateUserPassword(@Param("userId") long userId, @Param("password") String password);
}
