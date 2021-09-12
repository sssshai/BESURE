package mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface ParamsMapper {
    @Insert("insert into t_params values(#{P},#{P_pub},#{s},#{k});")
    void insParams(@Param("P") String P, @Param("P_pub") String P_pub, @Param("s") String s, @Param("k") String k);

    /**
     * 取到之后再进行数据类型转换
     */
    @Select("select ifnull(P,'') from t_params")
    String selP();

    @Select("select ifnull(P_pub,'') from t_params")
    String selP_pub();

    @Select("select ifnull(s,'') from t_params")
    String sels();

    @Select("select ifnull(k,'') from t_params")
    String selk();
}
