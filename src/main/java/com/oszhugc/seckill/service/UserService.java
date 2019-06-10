package com.oszhugc.seckill.service;

import com.oszhugc.seckill.dao.UserDao;
import com.oszhugc.seckill.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author oszhugc
 * @Date 2019\6\8 0008 11:51
 **/
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public User getById (int id){
        return userDao.getById(id);
    }

    @Transactional
    public boolean tx(){
        User user1 = new User();
        user1.setId(2);
        user1.setName("222");
        userDao.insert(user1);

        User user2 = new User();
        user2.setName("111");
        user2.setId(1);
        userDao.insert(user2);

        return true;

    }
}
