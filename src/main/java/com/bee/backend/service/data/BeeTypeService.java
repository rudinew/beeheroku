package com.bee.backend.service.data;

import com.bee.backend.domain.data.BeeCitizenship;
import com.bee.backend.domain.data.BeeDocType;

import java.util.List;

/**
 * Справочники
 *   BeeCitizenship
 *   BeeDocType
 *
 */
public interface BeeTypeService {
    //BeeCitizenship
    //Page<BeeCitizenship> getBeeCitizenshipPage(Integer pageNumber);
    BeeCitizenship getBeeCitizenshipByName(String cName);
    BeeCitizenship getBeeCitizenshipByOne(Long cId);
    List<BeeCitizenship> getBeeCitizenshipAll();
    void saveBeeCitizenship(BeeCitizenship beeCitizenship);
    void saveAndFlushBeeCitizenship(BeeCitizenship beeCitizenship);
    void deleteBeeCitizenship(Long cId);
    //BeeDocType
  //  Page<BeeDocType> getBeeDocTypePage(Integer pageNumber);
    BeeDocType getBeeDocTypeByName(String cName);
    BeeDocType getBeeDocTypeByAlias(String cAlias);
    BeeDocType getBeeDocTypeByOne(Long cId);
    List<BeeDocType> getBeeDocTypeAll();
    void saveBeeDocType(BeeDocType beeDocType);
    void saveAndFlushBeeDocType(BeeDocType beeDocType);
    void deleteBeeDocType(Long cId);
   /* //BeePassportType
    BeePassportType getBeePassportTypeByName(String cName);
    BeePassportType getBeePassportTypeByOne(Long cId);
    List<BeePassportType> getBeePassportTypeAll();
    void saveBeePassportType(BeePassportType beePassportType);
    void saveAndFlushBeePassportType(BeePassportType beePassportType);
    void deleteBeePassportType(Long cId);


    //BeeCertificateType
    BeeCertificateType getByName(String cName);
    BeeCertificateType getByAlias(String cAlias);*/
}
