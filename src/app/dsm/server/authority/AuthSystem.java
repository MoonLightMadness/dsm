package app.dsm.server.authority;

import app.dsm.server.constant.AuthorityEnum;

/**
 * 权限判定系统
 * @ClassName : app.dsm.server.authority.AuthSystem
 * @Description :
 * @Date 2021-08-17 15:39:53
 * @Author ZhangHL
 */
public class AuthSystem {

    /**
     * 判定权限大小关系
     *
     * @param source 即触发方法所需权限
     * @param target 请求触发者的权限
     * @return 大(等于)-true 小-false
     * @author zhl
     * @date 2021-08-17 15:40
     * @version V1.0
     */
    public static boolean judge(String source,String target){
        AuthorityEnum src = AuthorityEnum.getByMsg(source);
        AuthorityEnum tgt = AuthorityEnum.getByMsg(target);
        int isrc = Integer.parseInt(src.code());
        int itgt = Integer.parseInt(tgt.code());
        if(itgt >= isrc){
            return true;
        }
        return false;
    }

}
