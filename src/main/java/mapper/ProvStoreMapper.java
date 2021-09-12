package mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface ProvStoreMapper {
    @Update("update t_ehr " +
            "set c_rou_y_rou=#{c_rou_y_rou}, PB_l=#{PB_l}, Bl_l=#{Bl_l}, sigma_PB_l=#{sigma_PB_l} " +
            "where idP=#{idP} and stage=#{stage}")
    int updPB_l(@Param("idP") String idP, @Param("stage") int stage, @Param("c_rou_y_rou") String c_rou_y_rou,
                @Param("PB_l") String PB_l, @Param("Bl_l") String Bl_l, @Param("sigma_PB_l") String sigma_PB_l);

    @Select("select ck_rou_y_rou from t_ehr " +
            "where idP=#{idP} and stage=#{stage}")
    String selCk_rou_y_rouByStage(@Param("idP") String idP, @Param("stage") int stage);

    @Update("update t_ehr " +
            "set ck_rou_y_rou=#{ck_rou_y_rou} " +
            "where idP=#{idP} and stage=#{stage}")
    int updCk_rou_y_rou(@Param("idP") String idP, @Param("stage") int stage, @Param("ck_rou_y_rou") String ck_rou_y_rou);

    @Select("select PB_l from t_ehr " +
            "where idP=#{idP} and stage=#{stage}")
    String selPB_l(@Param("idP") String idP, @Param("stage") int stage);

    @Select("select Bl_l from t_ehr " +
            "where idP=#{idP} and stage=#{stage}")
    String selBl_l(@Param("idP") String idP, @Param("stage") int stage);

    @Select("select sigma_PB_l from t_ehr " +
            "where idP=#{idP} and stage=#{stage}")
    String selSigma_PB_l(@Param("idP") String idP, @Param("stage") int stage);

    //test
    @Insert("insert into testBlob values(#{test})")
    int insert(@Param("test") String test);

    //test
    @Select("select test from testBlob")
    String select();
}
