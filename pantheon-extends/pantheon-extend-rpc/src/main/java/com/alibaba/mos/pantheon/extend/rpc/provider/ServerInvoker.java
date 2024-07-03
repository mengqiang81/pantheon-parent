package com.alibaba.mos.pantheon.extend.rpc.provider;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public interface ServerInvoker {

    void invoke(String service, String method, Map<String, String> metas, InputStream input, OutputStream output) throws IOException;

}
