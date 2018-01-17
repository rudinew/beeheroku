package com.bee.backend.service.data;

import com.bee.backend.domain.data.BeeDocType;
import com.bee.backend.domain.data.BeeDocument;
import com.bee.backend.domain.data.BeeDocumentFile;
import com.bee.backend.domain.data.BeePerson;
import com.bee.web.exceptions.FileNotUploadException;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BeeDocumentService {
    //BeeDocument
    Page<BeeDocument> getBeeDocumentPage(Integer pageNumber);
    BeeDocument getBeeDocumentByOne(Long cId);
    List<BeeDocument> getBeeDocumentAll();
    List<BeeDocument> getBeeDocumentByBeePerson(BeePerson beePerson);
    List<BeeDocument> getByBeeDocType(BeeDocType beeDocType);
    List<BeeDocument> getByBeePersonAndBeeDocType(BeePerson beePerson, BeeDocType beeDocType);
    void saveBeeDocument(BeeDocument beeDocument);
    void saveAndFlushBeeDocument(BeeDocument beeDocument);
    void addBeeDocument(BeeDocument beeDocument, MultipartFile[] photo, User user)throws FileNotUploadException, IOException;
    void deleteBeeDocument(Long cId);
    //BeeDocumentFile
    BeeDocumentFile getBeeDocumentFileByOne(Long cId);
    List<BeeDocumentFile> getBeeDocumentFileByBeeDocument(BeeDocument beeDocument);
    void saveBeeDocumentFile(BeeDocumentFile beeDocumentFile);
    void saveAndFlushBeeDocumentFile(BeeDocumentFile beeDocumentFile);
    void deleteBeeDocumentFile(Long cId);
    void uploadFiles(BeeDocument beeDocument, MultipartFile[] files) throws FileNotUploadException, IOException;
}
