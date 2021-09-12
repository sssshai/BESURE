package filter;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;
import util.DeployUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.io.InputStream;

/**
 * 部署合约
 * 清理数据库，方便测试
 */
@WebFilter(filterName = "DeployContractAndClearDataInView", value = "/*")
public class DeployContractAndClearDataInView implements Filter {
    Logger logger = Logger.getLogger(DeployContractAndClearDataInView.class);

    @Override
    public void init(FilterConfig filterconfig) {
        logger.warn("开始部署合约");
        String address = contract.DeployUtil.deploy();
        if (address != null && !address.equals("")) {
            ServletContext sc = filterconfig.getServletContext();
            sc.setAttribute("contract", address);
        } else {
            logger.error("合约部署失败");
        }
    }

    /**
     * 每次请求都会过滤，包含js、css请求
     */
    @Override
    public void doFilter(ServletRequest servletrequest, ServletResponse servletresponse, FilterChain filterchain) {
        //设置请求和响应编码格式已由SpringMVC管理
        try {
            filterchain.doFilter(servletrequest, servletresponse);
        } catch (IOException | ServletException e) {
            e.printStackTrace();
        }
    }


    /**
     * 此处只能使用mybatis配置文件，spring加载mapper、controller、service均为空？
     */
    @Override
    public void destroy() {
        logger.warn("数据库开始清理");
        SqlSession session = null;
        try (InputStream is = Resources.getResourceAsStream("mybatis-config.xml")) {
            SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(is);
            session = factory.openSession();
            session.delete("clean.deleteCS");
            session.delete("clean.deleteH");
            session.delete("clean.deleteKS");
            session.delete("clean.deleteUser");
            session.delete("clean.deleteEhr");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.commit();
                session.close();
                logger.warn("数据库清理成功");
            } else {
                logger.error("数据库清理失败");
            }
        }
        logger.warn("清理合约资源文件");
        DeployUtil.cleanUpResourcesFile();
    }
}
