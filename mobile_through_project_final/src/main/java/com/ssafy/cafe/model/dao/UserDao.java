package com.ssafy.cafe.model.dao;

import java.util.List;
import com.ssafy.cafe.model.dto.User;

public interface UserDao {
    /**
     * 사용자 정보를 추가한다.
     * @param user
     * @return
     */
    int insert(User user);

    /**
     * 사용자의 Stamp 정보를 수정한다.
     * @param user
     * @return
     */
    int updateStamp(User user);
    
    /**
     * 사용자의 push 동의여부를 수정한다.
     * @param user
     * @return
     */
    int updatePush(User user);
    
    /**
     * 사용자의 location 동의여부를 수정한다.
     * @param user
     * @return
     */
    int updateLocation(User user);
    
    /**
     * 사용자의 marketing 동의여부를 수정한다.
     * @param user
     * @return
     */
    int updateMarketing(User user);
    
    /**
     * 사용자의 탈퇴여부를 수정한다.
     * @param user
     * @return
     */
    int updateLeave(String userId);
    
    /**
     * 사용자 정보를 조회한다.
     * @param userId
     * @return
     */
    User select(String userId);
    
    /**
     * 로그인 할 사용자의 정보를 조회한다.
     * @param userId
     * @return
     */
    User login(String userId);

    /**
     * 사용자 정보를 삭제한다.
     * @param userId
     * @return
     */
    int delete(String userId);
    List<User> selectAll(); 
    
    /**
     * 사용자 정보를 갱신한다.
     * @param user
     * @return
     */
    int update(User user);
    
    
}
