package com.shmtu.fabric_server.Mapper;
import com.shmtu.fabric_server.Pojo.UserHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface UserHistoryMapper {
    List<UserHistory> getHistory(@Param("uId") String uId);
    int setHistory(UserHistory uh);
    UserHistory getHistoryByHid(@Param("historyId") String historyId);
}
