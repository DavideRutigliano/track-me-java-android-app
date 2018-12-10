package com.github.ferrantemattarutigliano.software.server.config;

import org.apache.commons.compress.utils.IOUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

public class ContentCacheRequestWrapper extends HttpServletRequestWrapper {

    private byte[] rawData;
    private HttpServletRequest request;
    private ResettableServletInputStream servletStream;

    public ContentCacheRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        this.request = request;
        this.servletStream = new ResettableServletInputStream(request.getInputStream());
    }

    public void resetInputStream() {
        servletStream.stream = new ByteArrayInputStream(rawData);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (rawData == null) {
            rawData = IOUtils.toByteArray(this.request.getInputStream());
            servletStream.stream = new ByteArrayInputStream(rawData);
        }
        return servletStream;
    }

    private class ResettableServletInputStream extends ServletInputStream {

        private InputStream stream;

        private final ServletInputStream is;

        private ResettableServletInputStream(ServletInputStream is) {
            this.is = is;
        }

        @Override
        public int read() throws IOException {
            return stream.read();
        }

        @Override
        public boolean isFinished() {
            return this.is.isFinished();
        }

        @Override
        public boolean isReady() {
            return this.is.isReady();
        }

        @Override
        public void setReadListener(ReadListener readListener) {
            this.is.setReadListener(readListener);
        }
    }
}