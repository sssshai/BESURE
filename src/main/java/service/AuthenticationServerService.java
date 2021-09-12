package service;

import pojo.DO.User;

public interface AuthenticationServerService {
    /**
     * 创建用户信息
     */
    int createUser(String username, String password);

    /**
     * 保存用户信息
     */
    User saveUser(int uid, String username, String password);

    /**
     * 从数据库中检查是否存在此用户，如果存在返回其id，不存在返回-1
     */
    int checkUser(String username, String password);
}
