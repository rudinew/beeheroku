package com.bee.backend.service.data;


import com.bee.backend.domain.data.*;
import com.bee.backend.domain.security.BeeUsers;
import com.bee.backend.repositories.data.BeeDocumentFileRepository;
import com.bee.backend.repositories.data.BeeDocumentRepository;
import com.bee.backend.repositories.data.BeePersonDocumentTypeRepository;
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
public class BeeDocumentServiceImpl implements BeeDocumentService{
    private static final int PAGE_SIZE = 10;
    @Autowired
    BeeDocumentRepository beeDocumentRepository;

    @Autowired
    BeeDocumentFileRepository beeDocumentFileRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BeePersonDocumentTypeRepository beePersonDocumentTypeRepository;
    //BeeDocumentRepository
    @Override
    public Page<BeeDocument> getBeeDocumentPage(Integer pageNumber) {
        PageRequest request = new PageRequest(pageNumber - 1, PAGE_SIZE, Sort.Direction.DESC, "num");
        return beeDocumentRepository.findAll(request);
    }

    @Override
    public BeeDocument getBeeDocumentByOne(Long cId) {
        return beeDocumentRepository.findOne(cId);
    }

    @Override
    public List<BeeDocument> getBeeDocumentAll() {
        return beeDocumentRepository.findAll();
    }

    @Override
    public List<BeeDocument> getBeeDocumentByBeePerson(BeePerson beePerson) {
        return beeDocumentRepository.findByBeePerson(beePerson);
    }

    @Override
    public List<BeeDocument> getByBeeDocType(BeeDocType beeDocType) {
        return beeDocumentRepository.findByBeeDocType(beeDocType);
    }

    @Override
    public List<BeeDocument> getByBeePersonAndBeeDocType(BeePerson beePerson, BeeDocType beeDocType) {
        return beeDocumentRepository.findByBeePersonAndBeeDocType(beePerson, beeDocType);
    }

    @Override
    @Transactional
    public void saveBeeDocument(BeeDocument beeDocument) {
        beeDocumentRepository.save(beeDocument);
    }

    @Override
    @Transactional
    public void saveAndFlushBeeDocument(BeeDocument beeDocument) {
        beeDocumentRepository.saveAndFlush(beeDocument);
    }

    @Override
    @Transactional
    public void addBeeDocument(BeeDocument beeDocument, MultipartFile[] photo, User user)throws FileNotUploadException, IOException {

        //save person
        BeeUsers beeUsers =  userRepository.findByLogin(user.getUsername());
        beeDocument.setBeeUsers(beeUsers); //користувач
        beeDocument.setDtFrom(new LocalDate()); //дата зміни (поки тут)
        saveBeeDocument(beeDocument);
        //upload file
        uploadFiles(beeDocument, photo);

        //Налаштування
        //ВИДАЛЕННЯ внесеного типу з таблиці оповіщень BeePersonDocumentType
        List<BeePersonDocumentType> beePersonDocumentTypes = beePersonDocumentTypeRepository.findByBeePersonAndBeeDocType(beeDocument.getBeePerson(), beeDocument.getBeeDocType());
        for (BeePersonDocumentType item : beePersonDocumentTypes) {
            beePersonDocumentTypeRepository.delete(item.getId());
         }
        //

    }

    @Override
    @Transactional
    public void deleteBeeDocument(Long cId) {
        beeDocumentRepository.delete(cId);
    }

///BeeDocumentFileRepository
    @Override
    public BeeDocumentFile getBeeDocumentFileByOne(Long cId) {
        return beeDocumentFileRepository.findOne(cId);
    }

    @Override
    public List<BeeDocumentFile> getBeeDocumentFileByBeeDocument(BeeDocument beeDocument) {
        return beeDocumentFileRepository.findByBeeDocument(beeDocument);
    }

    @Override
    @Transactional
    public void saveBeeDocumentFile(BeeDocumentFile beeDocumentFile) {
        beeDocumentFileRepository.save(beeDocumentFile);
    }

    @Override
    @Transactional
    public void saveAndFlushBeeDocumentFile(BeeDocumentFile beeDocumentFile) {
        beeDocumentFileRepository.saveAndFlush(beeDocumentFile);
    }

    @Override
    @Transactional
    public void deleteBeeDocumentFile(Long cId) {
        beeDocumentFileRepository.delete(cId);
    }

    @Override
    public void uploadFiles(BeeDocument beeDocument, MultipartFile[] files) throws FileNotUploadException, IOException {

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
               /*getName() Return the name of the parameter in the multipart form.
                getOriginalFilename() Return the original filename in the client's filesystem.*/
                BeeDocumentFile beeDocumentFile = new BeeDocumentFile(file.getBytes(), file.getName(), String.valueOf(file.getSize()), file.getContentType(), beeDocument /*, beeUsers*/);
                saveAndFlushBeeDocumentFile(beeDocumentFile);

            }

        }
    }
}
