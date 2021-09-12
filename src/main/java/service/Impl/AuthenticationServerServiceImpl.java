package service.Impl;

import mapper.UserMapper;
import org.springframework.stereotype.Service;
import pojo.DO.User;
import service.AuthenticationServerService;

import javax.annotation.Resource;

@Service
public class AuthenticationServerServiceImpl implements AuthenticationServerService {
    @Resource
    private UserMapper userMapper;

    @Override
    public int createUser(String username, String password) {
        int ins = userMapper.insUser(username, password);
        return ins > 0 ? ins : -1;
    }

    @Override
    public User saveUser(int uid, String username, String password) {
        return new User(uid, username, password);
    }

    @Override
    public int checkUser(String username, String password) {
        if (userMapper.selByUnamePwdRole(username, password) > 0) {
            return userMapper.selByUnamePwdRole(username, password);
        } else {
            return -1;
        }
    }
}
