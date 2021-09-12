package util;

import java.io.*;
import java.util.Objects;
import java.util.Properties;

/**
 * 根据Maven项目目录重写了相应方法
 */
public class DeployUtil {
    //req.getServletContext().getRealPath("/WEB-INF")要依赖tomcat启动
    public static StringBuilder classpath;
    public static String path;
    public static String propertiesPath;
    public static String propertiesSuffix = "contract.properties";

    static {
        classpath = new StringBuilder(Objects.requireNonNull(
                DeployUtil.class.getClassLoader().getResource("")).getPath());
        //path D:/java/IdeaProjects/BESURE/target/MavenWeb/
        path = new DeployUtil().getPath(classpath);
        propertiesPath = path + propertiesSuffix;
    }

    //最好私有，但为了测试，用来公开构造
    public DeployUtil() {
    }

    /**
     * 在windows下调用dos命令
     */
    public static String deploy() {
//        String strCmd = "cd " + "D:/java/IdeaProjects/BESURE/src/main/web"+ " && truffle migrate";
        String strCmd = "cd " + path + " && truffle compile && truffle migrate";
        System.out.println(strCmd);
        //通过cmd程序执行cmd命令来部署智能合约
        Process process = null;
        try {
            //cmd /c表示命令运行之后关闭窗口
            process = Runtime.getRuntime().exec("cmd /c " + strCmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (process == null) {
            return null;
        }
        //读取屏幕输出找到合约地址
        BufferedReader strCon = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        String address = null;
        String contractAddress;
        try {
            int index = 0;
            while ((line = strCon.readLine()) != null) {
                if (line.indexOf("contract address:") > 0) {
                    index++;
                }
                //第一个contract address是migration合约的，provenance的合约是第二个
                if (index == 2 && line.indexOf("contract address:") > 0) {
                    address = line.substring(line.indexOf("0x"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (address == null) {
            return null;
        }
        contractAddress = address.substring(address.indexOf(" ") + 1);
        //将合约地址写入资源文件
        try (FileOutputStream fos = new FileOutputStream(propertiesPath)) {
            Properties properties = new Properties();
            properties.setProperty("Provenance", contractAddress);
            properties.store(fos, "address");
            return writeResourceFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //核对是否正确存入资源文件
        return address.equals(writeResourceFile()) ? address : null;
    }

    /**
     * 写入合约资源文件
     * 使用try-with-resource改写
     */
    public static String writeResourceFile() {
        Properties properties = new Properties();
        String address = "";
        try (InputStream in = new BufferedInputStream(new FileInputStream(propertiesPath))) {
            properties.load(in);
            for (String key : properties.stringPropertyNames()) {
                address = properties.getProperty(key);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }

    /**
     * 清理合约资源文件
     * 使用try-with-resource改写
     */
    public static void cleanUpResourcesFile() {
        try (FileOutputStream fos = new FileOutputStream(propertiesPath)) {
            Properties properties = new Properties();
            properties.setProperty("Provenance", "");
            properties.store(fos, "address");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将/D:/java/IdeaProjects/BESURE/web/WEB-INF/classes/转为D:/java/IdeaProjects/BESURE/web/
     */
    public String getPath(StringBuilder path) {
        //最后一个/的位置
        int index = path.lastIndexOf("/");
        //去掉开头的/和结尾/之后的
        path.delete(0, 1).delete(index, path.length());
        for (int i = 0; i < 3; i++) {
            index = path.lastIndexOf("/");
            path.delete(index, path.length());
        }
        path.delete(index, path.length()).append("/");
        return path.toString();
    }
}
