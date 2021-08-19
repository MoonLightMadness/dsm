package app.dsm.swarm.domain;

import lombok.Data;

/**
 * @ClassName : app.dsm.swarm.domain.IndicatorRemote
 * @Description :
 * @Date 2021-08-19 09:42:46
 * @Author ZhangHL
 */
@Data
public class IndicatorRemote {

    /**
     * 类全路径
     */
    private String classPath;


    /**
     * 相对路径(抽象路径)
     */
    private String relativePath;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 权限
     */
    private String authority;

    /**
     * 角色
     */
    private String character;

}
