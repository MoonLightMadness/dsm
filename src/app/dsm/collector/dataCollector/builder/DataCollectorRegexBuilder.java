package app.dsm.collector.dataCollector.builder;

import app.dsm.collector.dataCollector.domain.Target;

/**
 * @ClassName : app.dsm.collector.dataCollector.builder.DataCollectorRegexBuilder
 * @Description :
 * @Date 2021-09-03 10:59:24
 * @Author ZhangHL
 */
public class DataCollectorRegexBuilder {
    
    private Target target;


    public DataCollectorRegexBuilder(Target target){
        this.target =target;
    }

    public DataCollectorRegexBuilder(String mainProperty,String classProperty){
        target = new Target();
        target.setMainProperty(mainProperty);
        target.setClassProperty(classProperty);
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("<").append(target.getMainProperty()).append("(.*?)").append("class=\"");
        sb.append(target.getClassProperty()).append("\"(.*?)>(.*?)</").append(target.getMainProperty()).append(">");
        return sb.toString();
    }
    
    
}
