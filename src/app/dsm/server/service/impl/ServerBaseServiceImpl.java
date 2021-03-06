package app.dsm.server.service.impl;

import app.dsm.config.Configer;
import app.dsm.mapper.annotation.TableName;
import app.dsm.mapper.impl.Mapper;
import app.dsm.server.adapter.ListenerAdapter;
import app.dsm.server.annotation.Authority;
import app.dsm.server.annotation.Path;
import app.dsm.server.container.ServerContainer;
import app.dsm.server.container.ServerEntity;
import app.dsm.server.impl.SelectorIOImpl;
import app.dsm.server.service.ServerBaseService;
import app.dsm.server.vo.*;
import app.log.LogSystem;
import app.log.LogSystemFactory;
import app.parser.impl.JSONParserImpl;
import app.utils.SimpleUtils;
import app.utils.TimeFormatter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Path(value = "/server")
@TableName(value = "auth_user_config")
public class ServerBaseServiceImpl implements ServerBaseService {

    private ListenerAdapter listenerAdapter;

    private LogSystem log = LogSystemFactory.getLogSystem();

    private Mapper mapper = new Mapper();

    @Override
    public void invoke(Object obj, String... args) {
        this.listenerAdapter = (ListenerAdapter) obj;
    }

    @Override
    public void setArgs(Object obj, String... args) {
        this.listenerAdapter = (ListenerAdapter) obj;
    }

    /**
     * 返回服务器时间
     * @return @return {@link String }
     * @author zhl
     * @date 2021-08-12 21:58
     * @version V1.0
     */
    @Path(value = "/gettime")
    @Override
    public GetTimeRspVO getTime(GetTimeRspVO args){
        GetTimeRspVO getTimeRspVO = new GetTimeRspVO();
        getTimeRspVO.setTime(SimpleUtils.getTimeStamp2(TimeFormatter.SEC_LEVEL));
        return getTimeRspVO;
    }

    @Path(value = "/calculate")
    @Authority(value = "HIGH")
    @Override
    public CalculatorRspVO calculate(CalculatorReqVO calculatorReqVO) {
        CalculatorRspVO calculatorRspVO = new CalculatorRspVO();
        try {
            long result = Long.parseLong(calculatorReqVO.getX()) + Long.parseLong(calculatorReqVO.getY());
            calculatorRspVO.setResult(String.valueOf(result));
            return calculatorRspVO;
        }catch (Exception e) {
            log.error("计算失败,原因:{}",e);
            CalculatorRspVO baseRspVO = new CalculatorRspVO();
            baseRspVO.setCode("999999");
            baseRspVO.setMsg(e.getMessage());
            return  baseRspVO;
        }

    }

    /**
     * 设置服务器名字
     *
     * @param args
     * @return @return {@link BaseRspVO }
     * @author zhl
     * @date 2021-08-14 11:37
     * @version V1.0
     */
    @Path(value = "/setname")
    @Override
    public BaseRspVO setName(String args) {
        SetNameReqVO setNameReqVO = (SetNameReqVO) new JSONParserImpl().parser(args.getBytes(StandardCharsets.UTF_8),SetNameReqVO.class);
        SocketChannel channel = listenerAdapter.getChannel();
        SelectorIOImpl selectorIO = listenerAdapter.getSelectorIO();
        ServerContainer serverContainer = selectorIO.getServerContainer();
        List<ServerEntity> entityList = serverContainer.getList();
        ListIterator<ServerEntity> iterator = entityList.listIterator();
        while (iterator.hasNext()){
            ServerEntity entity = iterator.next();
            if(entity.getSocketChannel() == channel){
                entity.setName(setNameReqVO.getName());
            }
        }
        BaseRspVO baseReqVO = new BaseRspVO();
        baseReqVO.setCode("200");
        baseReqVO.setMsg("success");
        return baseReqVO;
    }

    @Path(value = "/getuser")
    public List<GetUserInfoRspVO> getUser(GetUserInfoReqVO getUserInfoReqVO){
        mapper.initialize(this.getClass());
        Object[] objects = mapper.selectList(new GetUserInfoRspVO(),getUserInfoReqVO);
        List<GetUserInfoRspVO> result = new ArrayList<>();
        for (Object object : objects){
            result.add((GetUserInfoRspVO) object);
        }
        return result;
    }

    @Path("/upload")
    public UploadFileRspVO uploadFile(UploadFileReqVO uploadFileReqVO){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File("./"+uploadFileReqVO.getFileName()));
            fileOutputStream.write(uploadFileReqVO.getContent().getBytes(StandardCharsets.UTF_8));
            fileOutputStream.close();
            String id = UUID.randomUUID().toString();
            UploadFileRspVO uploadFileRspVO = new UploadFileRspVO();
            uploadFileRspVO.setId(id);
            return uploadFileRspVO;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (UploadFileRspVO) new BaseRspVO();
    }

    @Path("/download")
    public DownLoadRspVO download(DownLoadReqVO downLoadReqVO){
        Configer configer = new Configer();
        DownLoadRspVO downLoadRspVO = new DownLoadRspVO();
        String path = configer.readConfigBySpecificPath("./fs/index.txt", downLoadReqVO.getId());
        String content = new String(SimpleUtils.readFile(path));
        downLoadRspVO.setContent(content);
        return downLoadRspVO;
    }

}
