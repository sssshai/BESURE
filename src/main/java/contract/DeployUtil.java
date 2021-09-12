package contract;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

import java.io.*;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * 根据Maven项目目录重写了相应方法
 */
public class DeployUtil {
    private static final BigInteger ethBase = BigInteger.valueOf(10).pow(18);   // 1 eth = 10^18 wei
    public static Provenance_sol_Provenance contract;
    //req.getServletContext().getRealPath("/WEB-INF")要依赖tomcat启动
    public static StringBuilder classpath;
    public static String path;
    public static String propertiesPath;
    public static String propertiesSuffix = "contract.properties";
    private static Web3j web3j;
    private static Credentials credentials;    //第一个账户的私钥
    private static String CONTRACT_ADDRESS;   //部署的合约地址

    static {
        classpath = new StringBuilder(Objects.requireNonNull(
                DeployUtil.class.getClassLoader().getResource("")).getPath());
        //path D:/java/IdeaProjects/BESURE/target/MavenWeb/
        path = "/" + DeployUtil.getPath(classpath);
        propertiesPath = path + propertiesSuffix;
    }

    public DeployUtil() {
        try {
            web3j = Web3j.build(new HttpService("http://localhost:7545"));  //本地的ganache gui
//            web3j = Web3j.build(new HttpService("https://ropsten.infura.io/v3/5e8867a88096409aa3373f4b4b15ed0b")); //ropsten测试链，地址来源于infura代理
//            credentials = Credentials.create("45acc77ea0ffb6d3f246f500f12fa2af5b11827310abc285c0d48889b96de1ea");  //ropsten账户私钥，来源于metamask申请的账户和faucet
            credentials = Credentials.create("4a893ad53f379ffcf8e9e7bc9b898ae0fca5e4311744762d4dc19a173549f79c");  //ganache第一个用户私钥
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 在windows下调用dos命令
     */
    public static String deploy() {
        new DeployUtil();
        try {
            contract = Provenance_sol_Provenance.deploy(
                            web3j, credentials,
                            new BigInteger("22000000000"), new BigInteger("5100000"))
                    .send();
        } catch (Exception e) {
            e.printStackTrace();
        }

        CONTRACT_ADDRESS = contract.getContractAddress();

        //将合约地址写入资源文件
        try (FileOutputStream fos = new FileOutputStream(propertiesPath)) {
            Properties properties = new Properties();
            properties.setProperty("Provenance", CONTRACT_ADDRESS);
            properties.store(fos, "address");
            return writeResourceFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //核对是否正确存入资源文件
        return CONTRACT_ADDRESS.equals(writeResourceFile()) ? CONTRACT_ADDRESS : null;
    }

    public static TransactionReceipt Store(int idP, String content) {
        contract = Provenance_sol_Provenance.load(
                CONTRACT_ADDRESS, web3j, credentials,
                new BigInteger("22000000000"), new BigInteger("510000"));
        TransactionReceipt tx = null;
        try {
            tx = contract.create(BigInteger.valueOf(idP), content).send();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tx;
    }

    public static TransactionReceipt Audit(int idP) {
        contract = Provenance_sol_Provenance.load(
                CONTRACT_ADDRESS, web3j, credentials,
                new BigInteger("22000000000"), new BigInteger("510000"));
        TransactionReceipt tx = null;
        try {
            tx = contract.getProv(BigInteger.valueOf(idP)).send();
            System.out.println(contract.getContentEvents(tx).get(0)._content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tx;
    }

    public static void Verify(List<BigInteger> params1, List<BigInteger> params2,
                       List<BigInteger> params3, List<BigInteger> params4) {
        try {
            TransactionReceipt tr = (contract.verify(params1, params2, params3, params4)).send();
            System.out.println("contract verify:" + contract.getNotifyEvents(tr).get(0).note);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public static int getConfirmedNumber(TransactionReceipt tx) {
        try {
            BigInteger current = web3j.ethBlockNumber().send().getBlockNumber();
            BigInteger create = web3j.ethGetTransactionByHash(tx.getTransactionHash()).send().getTransaction().get().getBlockNumber();
            return current.intValue() - create.intValue() + 1;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 将/D:/java/IdeaProjects/BESURE/web/WEB-INF/classes/转为D:/java/IdeaProjects/BESURE/web/
     */
    public static String getPath(StringBuilder path) {
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
