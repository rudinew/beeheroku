package com.bee.backend.service.data;



import com.bee.backend.domain.data.BeeActions;
import com.bee.backend.domain.security.BeeUsers;

import java.util.List;

public interface BeeActionsService {
    //spring jpa data query
    //https://www.petrikainulainen.net/programming/spring-framework/spring-data-jpa-tutorial-introduction-to-query-methods/

    BeeActions getActionByOne(Long cId);
    List<BeeActions> getActionsAll();
    List<BeeActions> getActionsByUsersOrderByDtFromAsc(BeeUsers users);
    List<BeeActions> getActionsAllOrderByDtFromAsc();
    void saveAction(BeeActions action);
    void saveAndFlushActions(BeeActions action);
  //  void deleteAction(Long cId);




}

