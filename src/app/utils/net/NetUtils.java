package app.utils.net;

import app.dsm.server.annotation.Authority;
import app.dsm.server.annotation.Path;
import app.dsm.server.constant.AuthorityEnum;
import app.dsm.server.constant.Indicators;
import app.utils.SimpleUtils;
import app.utils.datastructure.ReflectIndicator;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @ClassName : app.utils.net.NetUtils
 * @Description :
 * @Date 2021-09-02 10:56:04
 * @Author ZhangHL
 */
public class NetUtils {


    public static void scanPackage(String packageName, Indicators indicator) {
        String workingPath = getJarSelfPath();
        String[] paths;
        //判断程序是否是以jar包形式启动的
        if (workingPath.endsWith(".jar")) {
            paths = scanJarFile(workingPath).split("\n");
        } else {
            workingPath = ".";
            paths = scanDirectory(workingPath).split("\n");
        }
        //寻找以packageName开头且有Path注解的类和方法
        for (String path : paths) {
            path = path.trim().replace(SimpleUtils.getFilePathSeparator(), ".");
            if (path.startsWith(packageName)) {
                constructReflectIndicator(path, indicator);
            }
        }
    }

    public static void constructReflectIndicator(String className, Indicators indicator) {
        ReflectIndicator temp = null;
        Class clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (Exception e) {

        }
        if (clazz != null && clazz.isAnnotationPresent(Path.class)) {
            Path classPath = (Path) clazz.getDeclaredAnnotation(Path.class);
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                method.setAccessible(true);
                if (method.isAnnotationPresent(Path.class)) {
                    Path methodPath = method.getDeclaredAnnotation(Path.class);
                    temp = new ReflectIndicator();
                    temp.setClassPath(className);
                    temp.setMethodName(method.getName());
                    temp.setRelativePath(classPath.value() + methodPath.value());
                    //判断有无权限注解
                    if (method.isAnnotationPresent(Authority.class)) {
                        Authority authority = method.getDeclaredAnnotation(Authority.class);
                        temp.setAuthority(authority.value());
                    } else {
                        //如果没有权限注解,则添加默认权限
                        temp.setAuthority(AuthorityEnum.NORMAL.msg());
                    }
                    indicator.add(temp);
                }
            }
        }
    }



    public static String scanDirectory(String directory) {
        File file = new File(directory);
        StringBuilder sb = new StringBuilder();
        if (!file.exists()) {
            return sb.toString();
        }
        if (file.isDirectory()) {
            String[] files = file.list();
            for (String f : files) {
                sb.append(scanDirectory(directory + "/" + f));
            }
        } else {
            if (file.getPath().endsWith(".java")) {
                sb.append(file.getPath().replace(SimpleUtils.getFilePathSeparator(), ".").substring(6, file.getPath().lastIndexOf('.'))).append("\n");
            }
        }
        return sb.toString();
    }

    public static String scanJarFile(String path) {
        File f = new File(path);
        StringBuilder sb = new StringBuilder();
        try {
            JarFile jarFile = new JarFile(f);
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                File temp = new File(entry.getName());
                if (entry.getName().endsWith(".class")) {
                    sb.append(temp.getPath().replace(SimpleUtils.getFilePathSeparator(), ".").substring(0, temp.getPath().lastIndexOf('.'))).append("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 获取以Jar形式包启动的自身路径(注意不是获取工作路径)
     * 该方法在以ide启动时会返回工作路径
     *
     * @return @return {@link String }
     * @author zhl
     * @date 2021-08-12 23:10
     * @version V1.0
     */
    public static String getJarSelfPath() {
        URL url = SimpleUtils.class.getProtectionDomain().getCodeSource().getLocation();
        String path = null;
        try {
            //转化为utf-8编码，支持中文
            path = URLDecoder.decode(url.getPath(), "utf-8");
            return path;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
