package app.dsm.flow;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

public class FlowEngineX {



    public FlowChain getChainByName(String name){

        File f = new File("./flowconfig.yaml");
        Yaml yaml = new Yaml();
        try {
            Map<String,String> map = yaml.load(new FileInputStream(f));
            Iterator<Map.Entry<String,String>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry values = iterator.next();
                if(values.getKey().equals(name)){
                    Map value =(Map) values.getValue();
                    Iterator i1 = value.entrySet().iterator();
                    while (i1.hasNext()){
                        Map.Entry m1 = (Map.Entry) i1.next();
                        if(m1.getKey().equals("value")){
                            return generateChain((String) m1.getValue());
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private FlowChain generateChain(String filepath){
        File f = new File(filepath);
        Yaml yaml = new Yaml();
        try {
            FlowChain chain = new FlowChain();
            chain.init();
            Map<String,String> flow = yaml.load(new FileInputStream(f));
            Iterator<Map.Entry<String,String>> iterator = flow.entrySet().iterator();
            while (iterator.hasNext()){
                FlowNode node=null;
                String className = null;
                Map.Entry values = iterator.next();
                Map sub =(Map)values.getValue();
                Iterator i1 = sub.entrySet().iterator();
                while (i1.hasNext()){
                    Map.Entry e = (Map.Entry)i1.next();
                    if(e.getKey().equals("class")){
                        className = (String) e.getValue();
                    }else {
                        FlowUnit unit = generateFlowUnit(className, (String) e.getValue());
                        FlowNode fn = new FlowNode();
                        fn.setName((String) e.getValue());
                        fn.setClassName(className);
                        fn.setUnit(unit);
                        if(node==null){
                            node = fn;
                            chain.add(node);
                        }else {
                            node.setNext(fn);
                            node = node.getNext();
                        }
                    }
                }
            }
            return chain;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private FlowUnit generateFlowUnit(String className,String methodName){
        try {
            Class c = Class.forName(className);
            Method method = c.getMethod(methodName,Object.class);
            Method check = c.getMethod(methodName+"Check",Object.class);
            FlowUnit unit = new FlowUnit(check,method);
            return unit;
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

}
