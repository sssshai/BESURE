package mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

public interface RegistrationMapper {
    @Insert("insert into t_ks (idP,pwP_star,au,eP) " +
            "values(#{idP},#{pwP_star},#{au},#{eP})")
    int toKS(@Param("idP") String idP, @Param("pwP_star") String pwP_star,
             @Param("au") String au, @Param("eP") int eP);

    @Insert("insert into t_cs (idP,auCS,cskP) " +
            "values(#{idP},#{auCS},#{cskP})")
    int toCS(@Param("idP") String idP, @Param("auCS") String auCS, @Param("cskP") String cskP);

    @Insert("insert into t_h (idP,auH) " +
            "values(#{idP},#{auH})")
    int toH(@Param("idP") String idP, @Param("auH") String auH);
}
