package com.bee.backend.service.data;


import com.bee.backend.domain.data.BeePerson;

import com.bee.backend.domain.data.BeePersonFile;
import com.bee.backend.domain.security.BeeAccessPerson;
import com.bee.web.exceptions.FileNotUploadException;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BeePersonService {
    //spring jpa data query
    //https://www.petrikainulainen.net/programming/spring-framework/spring-data-jpa-tutorial-introduction-to-query-methods/

    //BeePerson
    Page<BeePerson> getBeePersonPage(Integer pageNumber);
    BeePerson getBeePersonByLastname(String lastname);
    BeePerson getBeePersonByOne(Long cId);
    BeePerson getBeePersonByOneAndBeeAccessPersonsIn(Long cId, List<BeeAccessPerson> beeAccessPerson);


    List<BeePerson> getBeePersonAll();
    List<BeePerson> getBeePersonAllForSearch(String cFirstname,
                                             String cLastname,
                                             List<BeeAccessPerson> beeAccessPerson);

    List<BeePerson> getBeePersonOrderByLastnameAsc(); ///
    List<BeePerson> getBeePersonByBeeAccessPersonsOrderByLastnameAsc(List<BeeAccessPerson> beeAccessPerson);

    void saveBeePerson(BeePerson beePerson);
    void saveAndFlushBeePerson(BeePerson beePerson);
    void addBeePerson(BeePerson beePerson, MultipartFile photo, User user, String typeOperation)throws FileNotUploadException, IOException;
    void deleteBeePerson(Long cId);

    //BeePersonFile
    BeePersonFile getBeePersonFileByOne(Long cId);
    List<BeePersonFile> getBeePersonFileByBeePerson(BeePerson beePerson);
    void saveBeePersonFile(BeePersonFile beePersonFile);
    void saveAndFlushBeePersonFile(BeePersonFile beePersonFile);
    void uploadFileOne(BeePerson beePerson, MultipartFile file)  throws FileNotUploadException, IOException;

    void deleteBeePersonFile(Long cId);





}

