package app.dsm.compress;

public interface Compressor {

    /**
     * 编码
     *
     * @param text 文本
     * @return {@link String}
     */
    String encode(String text);

    /**
     * 解码
     *
     * @param text 文本
     * @return {@link String}
     */
    String decode(String text);

    void save(String encode);
}
