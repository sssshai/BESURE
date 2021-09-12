package mapper;

import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ProvenanceMapper {
    @Select("select Bl_l from t_ehr")
    List<String> selAllBlock();
}
