package com.github.codingdebugallday.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.codingdebugallday.factory.BeanFactory;
import com.github.codingdebugallday.pojo.Result;
import com.github.codingdebugallday.service.TransferService;
import com.github.codingdebugallday.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/9/5 2:37
 * @since 1.0.0
 */
@WebServlet(name = "transferServlet", urlPatterns = "/transferServlet")
public class TransferServlet extends HttpServlet {

    private final Logger logger = LoggerFactory.getLogger(TransferServlet.class);

    /**
     * 首先从BeanFactory获取到proxyFactory代理工厂的实例化对象
     * 从工厂获取委托对象（委托对象是增强了事务控制的功能）
     */
    // private final ProxyFactory proxyFactory = (ProxyFactory) BeanFactory.getBean("proxyFactory");
    // private final TransferService transferService =
    //         (TransferService) proxyFactory.getJdkProxy(BeanFactory.getBean("transferService"));

    private final TransferService transferService =
            (TransferService) BeanFactory.getBean("transferService");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        Result result = new Result();
        try {
            // 设置请求体的字符编码
            req.setCharacterEncoding(StandardCharsets.UTF_8.name());

            String fromCardNo = req.getParameter("fromCardNo");
            String toCardNo = req.getParameter("toCardNo");
            String moneyStr = req.getParameter("money");
            BigDecimal money = new BigDecimal(moneyStr);

            // 调用service层方法
            transferService.transfer(fromCardNo, toCardNo, money);
            result.setStatus("200");

        } catch (Exception e) {
            logger.error("transfer error", e);
            result.setStatus("500");
            result.setMessage(e.toString());
        }
        // 响应
        resp.setContentType("application/json;charset=utf-8");
        try {
            resp.getWriter().print(JsonUtils.object2Json(result));
        } catch (IOException e) {
            logger.error("response error", e);
        }
    }
}
