package dsm.security.flow;

import dsm.base.impl.UniversalEntity;
import dsm.flow.ComponentMethod;
import dsm.utils.SimpleUtils;

import java.util.Map;

public class MQSender extends ComponentMethod {

    private UniversalEntity entity;

    @Override
    public boolean preRun() {
        Map<String,Object> map = this.getAttachment();
        entity = (UniversalEntity) map.get("entity");
        return !SimpleUtils.isEmptyString(entity.getCompressCode());
    }

    @Override
    public void run() {

        //TODO 此包下三个类是为了实现以流程引擎为基础的验证功能

    }
}
