package dsm.base;

import java.io.Serializable;

/**
 * @ClassName : utils.base.entity.BaseEntity
 * @Description :
 * @Date 2021-03-31 19:38:16
 * @Author 张怀栏
 */
public interface BaseEntity extends Serializable {
    String getHashCode();
    void setHashCode(String hashCode);
}