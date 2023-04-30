package com.shmtu.fabric_server.Service;

import com.shmtu.fabric_server.Mapper.UserHistoryMapper;
import com.shmtu.fabric_server.Pojo.UserHistory;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional
@Service
public class HistoryService {

    @Autowired
    UserHistoryMapper hm;

    public List<UserHistory> getHistory(String uId){
        return hm.getHistory(uId);
    }
    public int setHistory(UserHistory uh){
       return hm.setHistory(uh);
    }
    public UserHistory getHistoryByHid(String historyId){
        return hm.getHistoryByHid(historyId);
    }
}
