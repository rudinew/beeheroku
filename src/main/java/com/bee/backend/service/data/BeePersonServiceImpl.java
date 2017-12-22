package com.bee.backend.service.data;


import com.bee.backend.domain.data.BeeDocNotification;
import com.bee.backend.domain.data.BeePerson;
import com.bee.backend.domain.data.BeePersonDocumentType;
import com.bee.backend.domain.data.BeePersonFile;
import com.bee.backend.domain.security.BeeAccessPerson;
import com.bee.backend.domain.security.BeeUsers;
import com.bee.backend.domain.security.UserPrivileges;
import com.bee.backend.repositories.data.BeeDocNotificationRepository;
import com.bee.backend.repositories.data.BeePersonDocumentTypeRepository;
import com.bee.backend.repositories.data.BeePersonFileRepository;
import com.bee.backend.repositories.data.BeePersonRepository;
import com.bee.backend.repositories.security.BeeAccessPersonRepository;
import com.bee.backend.repositories.security.UserRepository;
import com.bee.web.exceptions.FileNotUploadException;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class BeePersonServiceImpl implements BeePersonService {
    private static final int PAGE_SIZE = 10;

    @Autowired
    private BeePersonRepository beePersonRepository;

    @Autowired
    private BeePersonFileRepository beePersonFileRepository;

    @Autowired
    private UserRepository userRepository; // UserService userService;

    @Autowired
    private BeeAccessPersonRepository beeAccessPersonRepository;  //BeeAccessPersonService beeAccessPersonService;

    @Autowired
    private BeeDocNotificationRepository beeDocNotificationRepository;

    @Autowired
    private BeePersonDocumentTypeRepository beePersonDocumentTypeRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<BeePerson> getBeePersonPage(Integer pageNumber) {
        PageRequest request = new PageRequest(pageNumber - 1, PAGE_SIZE, Sort.Direction.DESC, "l_name");
        return beePersonRepository.findAll(request);
    }
    @Override
    @Transactional(readOnly = true)
    public BeePerson getBeePersonByLastname(String lastname) {
        return beePersonRepository.findByLastname(lastname);
    }

    @Override
    @Transactional(readOnly = true)
    public BeePerson getBeePersonByOne(Long cId) {
        return beePersonRepository.findOne(cId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BeePerson> getBeePersonAll() {
        return beePersonRepository.findAll();
    }

    @Override
 //   @PostFilter("hasPermission(filterObject, 'READ')")
 //   @PostFilter("hasPermission(filterObject, 'read') or hasPermission(filterObject, 'admin')")
    public List<BeePerson> getBeePersonOrderByLastnameAsc() { ///
        return beePersonRepository.findAllByOrderByLastnameAsc();
    }

    @Override
    public BeePerson getBeePersonByOneAndBeeAccessPersonsIn(Long cId, List<BeeAccessPerson> beeAccessPerson) {
        return beePersonRepository.findByIdAndBeeAccessPersonsIn(cId, beeAccessPerson);
    }

    @Override
    public List<BeePerson> getBeePersonByBeeAccessPersonsOrderByLastnameAsc(List<BeeAccessPerson> beeAccessPerson) {
        return beePersonRepository.findAllByBeeAccessPersonsInOrderByLastnameAsc(beeAccessPerson);
    }

    @Override
    public List<BeePerson> getBeePersonAllForSearch(String cFirstname, String cLastname, List<BeeAccessPerson> beeAccessPerson) {
        return beePersonRepository.findAllByFirstnameLikeOrLastnameLikeAndBeeAccessPersonsIn(cFirstname, cLastname, beeAccessPerson);
    }

    @Override
    @Transactional
    public void saveBeePerson(BeePerson beePerson) {
        beePersonRepository.save(beePerson);

    }

    @Override
    @Transactional
    public void saveAndFlushBeePerson(BeePerson beePerson) {
        beePersonRepository.saveAndFlush(beePerson);

    }

 /*
 the Rollback rules for the transaction
Note that – by default, rollback happens for runtime, unchecked exceptions only.
The checked exception does not trigger a rollback of the transaction;
the behavior can, of course, be configured with the rollbackFor and noRollbackFor annotation parameters.
*/
    @Override
    @Transactional
    public void addBeePerson(BeePerson beePerson, MultipartFile photo, User user, String typeOperation)throws FileNotUploadException, IOException {

        //save person
        BeeUsers beeUsers =  userRepository.findByLogin(user.getUsername());  //userService.getUserByLogin(user.getUsername());
        beePerson.setBeeUsers(beeUsers); //користувач
        beePerson.setDtFrom(new LocalDate()); //дата зміни (поки тут)
        saveBeePerson(beePerson);
        //upload file
        uploadFileOne(beePerson, photo, beeUsers);
        //access
        if (beePerson.getNrOfBeeAccessPersons() == 0){
            BeeAccessPerson beeAccessPerson = new BeeAccessPerson(beePerson, beeUsers, UserPrivileges.EDIT, null);
            beeAccessPersonRepository.saveAndFlush(beeAccessPerson);
            //beeAccessPersonService.saveAndFlushBeeAccessPerson(beeAccessPerson);
        }
        //notification if insert
        //Налаштування
        //вставка всіх типів доків,які обовязкові до заповнення
        if (typeOperation == "insert") {
            List<BeeDocNotification> beeDocNotifications = beeDocNotificationRepository.findAllByBeeUsers(beeUsers);  //beeDocNotificationService.getBeeDocNotificationByUsers(beeUsers);
            for (BeeDocNotification item : beeDocNotifications) {
                if (item.getIs_required()) {
                    BeePersonDocumentType beePersonDocumentType = new BeePersonDocumentType(beePerson, item.getBeeDocType());
                    beePersonDocumentTypeRepository.save(beePersonDocumentType);
                    // this.beePersonDocumentTypeService.saveAndFlushBeePersonDocumentType(beePersonDocumentType);
                }
            }
        }

    }

    @Override
    @Transactional
    public void deleteBeePerson(Long cId) {
       beePersonRepository.delete(cId);
    }
    //beePersonPhotoRepository
    @Override
    public BeePersonFile getBeePersonFileByOne(Long cId) {
        return beePersonFileRepository.findOne(cId);
    }

    @Override
    public List<BeePersonFile> getBeePersonFileByBeePerson(BeePerson beePerson) {
        return beePersonFileRepository.findByBeePerson(beePerson);
    }

    @Override
    @Transactional
    public void saveBeePersonFile(BeePersonFile beePersonFile) {
       beePersonFileRepository.save(beePersonFile);
    }

    @Override
    @Transactional
    public void saveAndFlushBeePersonFile(BeePersonFile beePersonFile) {
        beePersonFileRepository.saveAndFlush(beePersonFile);

    }

    @Override
    @Transactional
    public void deleteBeePersonFile(Long cId) {
        beePersonFileRepository.delete(cId);
    }

    @Override
    public void uploadFileOne(BeePerson beePerson, MultipartFile file, BeeUsers beeUsers) throws FileNotUploadException, IOException {

          /*  if (file.getSize() > 4194304) {
                LOG.info("file is too large " + file.getOriginalFilename() + " (" + file.getSize() +" > 4194304)");
            }
            else {*/
            if (!file.isEmpty()) {
                // LOG.info("start Upload file " + file.getOriginalFilename());  ///when debug or info
                BeePersonFile beePersonFile = null;
                //пока можно загрузить одну аватарку
                //если еще не загружено то инсерт
                if (beePerson.getNrOfBeePersonFiles() == 0) {
                    beePersonFile = new BeePersonFile(file.getBytes(), file.getOriginalFilename() /*getName()*/, String.valueOf(file.getSize()), file.getContentType(), beePerson, beeUsers  );
                }
                //иначе -  апдейт
                else {
                    beePersonFile = beePerson.getBeePersonFiles().get(0);
                    beePersonFile.setFile(file.getBytes());
                    beePersonFile.setName(file.getOriginalFilename()); //getName()
                    beePersonFile.setSize(String.valueOf(file.getSize()));
                    beePersonFile.setType(file.getContentType());
                    beePersonFile.setBeeUsers(beeUsers);
                }
                //сохранение
                saveAndFlushBeePersonFile(beePersonFile);


                /**
                 * https://spring.io/guides/gs/uploading-files/
                 */
                // redirectAttributes.addFlashAttribute("message",
                //          "You successfully uploaded " + photo.getOriginalFilename() + "!");

                //  LOG.info("end Upload file " + file.getOriginalFilename());  ///when debug or info
            }


    }
}
