package com.alibaba.mos.pantheon.extend.rpc.spring;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class RpcServerServlet extends HttpServlet {

    public static final String PROTOCOL_HTTP_0_9 = "HTTP/0.9";
    public static final String PROTOCOL_HTTP_1_0 = "HTTP/1.0";
    public static final int HTTP_STATUS_OK = 200;
    public static final int HTTP_STATUS_BAD_REQUEST = 400;
    public static final int HTTP_STATUS_METHOD_NOT_ALLOWED = 405;
    public static final int HTTP_SERVER_ERROR = 405;

    private static final String PATH_INVALID_ERROR_MSG = """
            {
              "code": "api-error",
              "message": "服务调用失败，请稍后重试"
            }
            """;

    private static final String UNSUPPORTED_PROTOCOL_ERROR_MSG = """
            {
              "code": "unsupported-protocol",
              "message": "不支持 HTTP 1.0 以下版本的协议"
            }
            """;

    private static final String EXECUTE_ERROR_MSG = """
            {
              "code": "api-error",
              "message": "服务调用失败，请稍后重试"
            }
            """;


    private final ServerInvoker invoke;

    public RpcServerServlet(ServerInvoker invoke) {
        this.invoke = invoke;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Content-Type", "application/json;charset=utf-8");
        String protocol = req.getProtocol();
        if (PROTOCOL_HTTP_0_9.equalsIgnoreCase(protocol) || PROTOCOL_HTTP_1_0.equalsIgnoreCase(protocol)) {
            resp.setStatus(HTTP_STATUS_BAD_REQUEST);
            resp.getWriter().println(UNSUPPORTED_PROTOCOL_ERROR_MSG);
            return;
        }

        String path = req.getPathInfo();
        if (Objects.isNull(path) || path.isEmpty()) {
            resp.setStatus(HTTP_STATUS_METHOD_NOT_ALLOWED);
            resp.getWriter().println(PATH_INVALID_ERROR_MSG);
            return;
        }
        String[] pathParams = path.split("/");
        if (pathParams.length != 3) {
            resp.setStatus(HTTP_STATUS_BAD_REQUEST);
            resp.getWriter().println(PATH_INVALID_ERROR_MSG);
            return;
        }
        resp.setStatus(HTTP_STATUS_OK);
        Map<String, String> metas = new HashMap<>();
        try {
            invoke.invoke(pathParams[1], pathParams[2], metas, req.getInputStream(), resp.getOutputStream());
        } catch (Exception e) {
            log.error("method:{} invoke error", pathParams[1] + "." + pathParams[2] ,e);
            resp.setStatus(HTTP_SERVER_ERROR);
            resp.getOutputStream().write(EXECUTE_ERROR_MSG.getBytes(StandardCharsets.UTF_8));
        }
    }
}
