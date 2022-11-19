package com.ssafy.cafe.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ssafy.cafe.model.dao.UserDao;
import com.ssafy.cafe.model.dto.User;

/**
 * @author itsmeyjc
 * @since 2021. 6. 23.
 */
@Service
public class UserServiceImpl implements UserService {
    
    private static UserServiceImpl instance = new UserServiceImpl();

    private UserServiceImpl() {}

    public static UserServiceImpl getInstance() {
        return instance;
    }
    
    @Autowired
    private UserDao userDao;

    @Override
    public void join(User user) {
        userDao.insert(user);
    }
    
    @Override
    public void update(User user) {
        userDao.update(user);
    }
    
    @Override
    public void pushUpdate(User user) {
        userDao.updatePush(user);
    }

    @Override
    public void locationUpdate(User user) {
        userDao.updateLocation(user);
        
    }

    @Override
    public void marketingUpdate(User user) {
        userDao.updateMarketing(user);
    }

    @Override
    public User login(String id, String pass) {
        User user = userDao.login(id);
        if (user != null && user.getPass().equals(pass)) {
            return user;
        } else {
            return null;
        }
    }

    @Override
    public void leave(String id) {
        userDao.updateLeave(id);
    }

    @Override
    public boolean isUsedId(String id) {
        return userDao.select(id) != null;
    }
    
    @Override
    public User selectUser(String id) {
        User user = userDao.select(id);
        if (user != null) {
            return user;
        } else {
            return null;
        }
    }
    
}
