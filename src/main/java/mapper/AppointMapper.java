package mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface AppointMapper {
    @Select("select idP from t_h where idP = #{idP} and auH = #{auH}")
    int selIdP(@Param("idP") String idP, @Param("auH") String auH);
}
