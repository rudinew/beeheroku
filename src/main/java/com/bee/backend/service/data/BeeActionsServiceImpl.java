package com.bee.backend.service.data;

import com.bee.backend.domain.data.BeeActions;
import com.bee.backend.domain.security.BeeUsers;
import com.bee.backend.repositories.data.BeeActionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class BeeActionsServiceImpl implements BeeActionsService {


    @Autowired
    private BeeActionsRepository beeActionsRepository;

    @Override
    public BeeActions getActionByOne(Long cId) {
        return beeActionsRepository.findOne(cId);
    }

    @Override
    public List<BeeActions> getActionsAll() {
        return beeActionsRepository.findAll();
    }

    @Override
    public List<BeeActions> getActionsByUsersOrderByDtFromAsc(BeeUsers users) {
        return beeActionsRepository.findAllByBeeUsersOrderByDtFromAsc(users);
    }

    @Override
    public List<BeeActions> getActionsAllOrderByDtFromAsc() {
        return beeActionsRepository.findAllByOrderByDtFromAsc();
    }

    @Override
    @Transactional
    public void saveAction(BeeActions action) {
        beeActionsRepository.save(action);
    }

    @Override
    @Transactional
    public void saveAndFlushActions(BeeActions action) {
        beeActionsRepository.saveAndFlush(action);
    }
}
