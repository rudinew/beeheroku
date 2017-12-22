package com.bee.backend.service.security;


import com.bee.backend.domain.security.BeeUsers;
//import org.springframework.mail.MailException;

import java.util.List;

public interface UserService {
    BeeUsers getUserByLogin(String login);
    List<BeeUsers> findByEmail(String email);
    List<BeeUsers> findAll();
    List<BeeUsers> findAllByLoginNotInOrderByLoginAsc(String login);
    void addUser(BeeUsers bUser);// throws MailException;
    void save(BeeUsers bUser);
    void updateUserPassword(long userId, String password);
}