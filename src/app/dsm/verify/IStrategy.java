package app.dsm.verify;

public interface IStrategy {
    String[] read();

    /**
     * 验证
     *
     * @param ip
     * @return int 1-成功 -1-失败
     */
    int verify(String ip);

}
