package app.dsm.server.vo;

import com.sun.mail.iap.ByteArray;
import lombok.Data;

/**
 * @ClassName : app.dsm.server.vo.UploadFileReqVO
 * @Description :
 * @Date 2021-09-08 16:00:07
 * @Author ZhangHL
 */
@Data
public class UploadFileReqVO {

    private String fileName;

    private String content;

}
