package utils.other;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import utils.other.test.BattleEventListener;

public class ClassScanner {

    public static void main(String[] args) throws Exception {
        List<String> classNames = scanClassNames(ClassScanner.class);

        classNames.forEach(name->{
            try {
                Class<?> aClass = Class.forName(name);
                Method[] methods = aClass.getMethods();
                for(Method method: methods){
                    if(method.getAnnotation(BattleEventListener.class)!= null){
                        System.out.println(method.getName());
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    public static List<String> scanClassNames(Class<?> clazz) throws Exception {
        URL classUrl = clazz.getResource(clazz.getSimpleName() + ".class");
        Objects.requireNonNull(classUrl, "无法获取类文件路径");

        if (!"file".equals(classUrl.getProtocol())) {
            throw new RuntimeException("该类未在文件系统中");
        }

        String classFilePath = decodeUrl(classUrl.getFile());
        File classFile = new File(classFilePath);
        Path baseDir = getClassRootPath(clazz, classFile);

        List<String> classNames = new ArrayList<>();
        Files.walk(baseDir)
                .filter(path -> path.toString().endsWith(".class"))
                .forEach(path -> classNames.add(convertToClassName(baseDir, path)));
        
        return classNames;
    }

    private static Path getClassRootPath(Class<?> clazz, File classFile) {
        String packageName = clazz.getName();
        if (packageName.isEmpty()) {
            // 默认包：直接使用类所在目录作为根目录
            return classFile.getParentFile().toPath();
        }

        // 将包名转换为文件路径格式（例如 com.example → com/example）
        String packagePath = packageName.replace('.', File.separatorChar);
        String absolutePath = classFile.getAbsolutePath();

        // 定位包路径在完整路径中的位置
        int packageIndex = absolutePath.lastIndexOf(packagePath);
        if (packageIndex == -1) {
            throw new IllegalStateException("类文件路径与包名不匹配: " + absolutePath);
        }

        // 截取根目录路径（包路径的起始位置之前）
        String basePath = absolutePath.substring(0, packageIndex);
        return new File(basePath).toPath();
    }

    private static String convertToClassName(Path baseDir, Path classFile) {
        return baseDir.relativize(classFile)
                .toString()
                .replace(File.separatorChar, '.')
                .replaceAll("\\.class$", "");
    }
    //
    //private static String convertToClassName(Path baseDir, Path classFile) {
    //    // 获取相对路径并转换为类名格式
    //    return baseDir.relativize(classFile)
    //            .toString()
    //            .replace(File.separatorChar, '.')
    //            .replace(".class", "");
    //}

    private static String decodeUrl(String url) throws UnsupportedEncodingException {
        return URLDecoder.decode(url, StandardCharsets.UTF_8.name());
    }
}