package spy.project.web;

import org.apache.commons.io.IOUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

public class RepeatReadWrapper extends HttpServletRequestWrapper {

    private byte [] body;
    private HttpServletRequest request;

    public RepeatReadWrapper(HttpServletRequest request) {
        super(request);
        this.request = request;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {

        if(this.body == null) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            IOUtils.copy(this.request.getInputStream(), bos);
            this.body = bos.toByteArray();
        }

        final ByteArrayInputStream bis = new ByteArrayInputStream(this.body);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return bis.read();
            }
        };
    }


    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }


}
