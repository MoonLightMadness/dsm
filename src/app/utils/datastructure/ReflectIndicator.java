package app.utils.datastructure;

import lombok.Data;

/**
 * @ClassName : app.utils.datastructure.ReflecIndicator
 * @Description :
 * @Date 2021-08-13 08:21:44
 * @Author ZhangHL
 */
@Data
public class ReflectIndicator {

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

}
