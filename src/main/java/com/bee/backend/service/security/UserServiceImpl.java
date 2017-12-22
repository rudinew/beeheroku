package com.bee.backend.service.security;

import com.bee.backend.domain.data.BeeDocNotification;
import com.bee.backend.domain.data.BeeDocType;
import com.bee.backend.domain.security.BeeUsers;
import com.bee.backend.repositories.data.BeeDocNotificationRepository;
import com.bee.backend.repositories.security.UserRepository;
import com.bee.backend.service.data.BeeTypeService;
//import com.bee.backend.service.data.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.MailException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

  //  @Autowired
  //  private EmailService emailService;

    @Autowired
    private BeeDocNotificationRepository beeDocNotificationRepository;

    @Autowired
    private BeeTypeService beeTypeService;

    @Override
    @Transactional(readOnly = true)
    public BeeUsers getUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public List<BeeUsers> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<BeeUsers> findAll() {
        return userRepository.findAll();
    }

    @Override
    public List<BeeUsers> findAllByLoginNotInOrderByLoginAsc(String login) {
        return userRepository.findAllByLoginNotInOrderByLoginAsc(login);
    }

    @Override
    @Transactional
    public void addUser(BeeUsers bUser) /*throws MailException*/ {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(bUser.getPassword());
        String hashedConfirmPassword = passwordEncoder.encode(bUser.getConfirmPassword());

        bUser.setPassword(hashedPassword);
        bUser.setConfirmPassword(hashedConfirmPassword);

        userRepository.save(bUser);
/* Heroku emails like Add-ons
        String subjectMail = "success registration on bee.com";
        String textMail = "Dear " + bUser.getFirstname() + "! \n Thank you for registration on bee.com \n Your login - " +
                bUser.getLogin() +
                "\n Have a nice day!";
        emailService.sendSimpleMessage(bUser.getEmail(), subjectMail, textMail);
        */

        //Налаштування
        //вставка всіх типів доків, для налаштування користувачем чи обовязково до заповнення
        List<BeeDocType> beeDocTypes = beeTypeService.getBeeDocTypeAll();
        for (BeeDocType itemType : beeDocTypes){
                BeeDocNotification beeDocNotification = new BeeDocNotification(itemType.getIs_required(), itemType, bUser);
                beeDocNotificationRepository.saveAndFlush(beeDocNotification);
            }


    }

    @Override
    @Transactional
    public void save(BeeUsers bUser) {userRepository.save(bUser);    }

    @Override
    @Transactional
    public void updateUserPassword(long userId, String password) {
        userRepository.updateUserPassword(userId, password);

    }
}
