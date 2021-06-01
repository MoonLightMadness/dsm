package app.utils.guid;

/**
 * guid生成器
 *
 * @author ZhangHL
 * @version 1.0.0
 * @date 2021/05/11
 */
public interface GuidGenerator {
    /**
     * 生成guid
     *
     * @param args 机器id
     * @return {@link String}
     * @throws Exception 异常
     */
    String generateGuid(String... args) throws Exception;
}
