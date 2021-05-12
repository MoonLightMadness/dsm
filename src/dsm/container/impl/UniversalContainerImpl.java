package dsm.container.impl;

import dsm.container.Container;

import java.util.*;

/**
 * @ClassName : dsm.container.impl.UniversalContainerImpl
 * @Description :
 * @Date 2021-05-04 17:23:29
 * @Author ZhangHL
 */
public class UniversalContainerImpl<T> implements Container<T> {
    private HashMap<String,T> dictionary;
    public void init(){
        dictionary=new HashMap<>();
    }
    @Override
    public void add(T object,String name) {
        synchronized (this){
            dictionary.put(name,object);
        }
    }

    @Override
    public T get(String name) {
        T o=null;
        Set<Map.Entry<String, T>> s =  dictionary.entrySet();
        Iterator<Map.Entry<String, T>> iterator = s.iterator();
        while (iterator.hasNext()){
            Map.Entry<String,T> entry=iterator.next();
            if(entry.getKey().equals(name)){
                o=entry.getValue();
                break;
            }
        }
        return o;
    }

    @Override
    public void delete(String name) {
        synchronized (this){
            Set<Map.Entry<String, T>> s =  dictionary.entrySet();
            Iterator<Map.Entry<String, T>> iterator = s.iterator();
            while (iterator.hasNext()){
                Map.Entry<String,T> entry=iterator.next();
                if(entry.getKey().equals(name)){
                    iterator.remove();
                    break;
                }
            }
        }
    }

    @Override
    public String getNames() {
        StringBuilder sb=new StringBuilder();
        Set<Map.Entry<String, T>> s =  dictionary.entrySet();
        Iterator<Map.Entry<String, T>> iterator = s.iterator();
        while (iterator.hasNext()){
            Map.Entry<String,T> entry=iterator.next();
            sb.append(entry.getKey()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public void reName(String src,String dest) {
        Set<Map.Entry<String, T>> s =  dictionary.entrySet();
        Iterator<Map.Entry<String, T>> iterator = s.iterator();
        while (iterator.hasNext()){
            Map.Entry<String,T> entry=iterator.next();
            if(entry.getKey().equals(src)){
                T t = entry.getValue();
                iterator.remove();
                add(t,dest);
            }
        }
    }
}
