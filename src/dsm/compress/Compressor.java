package dsm.compress;

public interface Compressor {

    /**
     * 编码
     *
     * @param text 文本
     * @return {@link String}
     */
    public String encode(String text);

    /**
     * 解码
     *
     * @param text 文本
     * @return {@link String}
     */
    public String decode(String text);

    public void save();
}
