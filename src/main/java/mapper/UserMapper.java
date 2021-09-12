package mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import pojo.DO.User;

import java.util.List;

public interface UserMapper {
    /**
     * 用户登录验证，无用户则返回0
     */
    @Select("select ifnull(max(uid),0) " +
            "from t_user " +
            "where uname = #{uname} and password = #{password}")
    int selByUnamePwdRole(@Param("uname") String uname,
                          @Param("password") String password);

    /**
     * 新增用户记录，自增的主键无需传入
     */
    @Insert("insert t_user (uname,password)  " +
            "values(#{uname},#{password})")
    int insUser(@Param("uname") String uname,
                @Param("password") String password);

    @Select("select uid,uname " +
            "from t_user")
    List<User> selAll();
}
