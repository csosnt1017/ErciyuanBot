package com.example.image;

import com.sobte.cqp.jcq.util.DigestUtils;
import com.sobte.cqp.jcq.util.StringHelper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Copyright © 2019年 erciyuanboot. All rights reserved.
 *
 * @author 古今
 * <p>
 * xxxxx类
 * @date 2019/09/26
 * <p>
 * Modification History:
 * Date     Author    Version      Description
 * ---------------------------------------------------------*
 * 2019/09/26   古今   v1.0.0       新增
 */
public class CQImage {
    private String md5;
    private String name;
    private int width;
    private int height;
    private long size;
    private String url;
    private Date addTime;
    private boolean load;


    public CQImage(String url, InputStream inputStream, String imageName, long size) throws IOException {
        this.url = url;
        this.size = size;
        this.md5 = DigestUtils.md5Hex(inputStream).toUpperCase();
        this.name = imageName;
        this.addTime = new Date();
    }

    public File downloadUseGet(File file, Map<String, List<String>> requestProperties) throws IOException {
        return this.download((File)file, "GET", requestProperties, (byte[])null, false);
    }

    public File download(File file, String method, Map<String, List<String>> requestProperties, byte[] data, boolean load) throws IOException {
        this.download((OutputStream)(new FileOutputStream(file)), method, requestProperties, data, load);
        return file;
    }

    protected void download(OutputStream outputStream, String method, Map<String, List<String>> requestProperties, byte[] data, boolean load) throws IOException {
        URL url = new URL(this.url);
        URLConnection connection = url.openConnection();
        BufferedInputStream inputStream = null;

        try {
            if (connection instanceof HttpURLConnection) {
                HttpURLConnection urlConnection = (HttpURLConnection)connection;
                if (method != null) {
                    urlConnection.setRequestMethod(method.toUpperCase());
                }

                String redirect;
                if (requestProperties != null) {
                    Iterator var10 = requestProperties.keySet().iterator();

                    while(var10.hasNext()) {
                        redirect = (String)var10.next();
                        List<String> properties = (List)requestProperties.get(redirect);
                        Iterator var13 = properties.iterator();

                        while(var13.hasNext()) {
                            String value = (String)var13.next();
                            urlConnection.addRequestProperty(redirect, value);
                        }
                    }
                }

                urlConnection.setConnectTimeout(3500);
                if (data != null) {
                    urlConnection.setDoOutput(true);
                    OutputStream out = connection.getOutputStream();
                    out.write(data);
                    out.flush();
                    out.close();
                }

                int resCode = urlConnection.getResponseCode();
                if (resCode >= 300 && resCode < 400) {
                    redirect = connection.getHeaderField("Location");
                    if (!StringHelper.isTrimEmpty(new String[]{redirect})) {
                        String oldUrl = this.url;
                        this.url = redirect;
                        this.download((OutputStream)outputStream, method, requestProperties, data, load);
                        this.url = oldUrl;
                        return;
                    }

                    throw new IOException("重定向请求不包含请求地址，返回码：" + urlConnection.getResponseCode());
                }

                if (resCode >= 300 || resCode > 200) {
                    throw new IOException("请求发送失败，返回码：" + urlConnection.getResponseCode());
                }
            }

            inputStream = new BufferedInputStream(connection.getInputStream());
            outputStream = new BufferedOutputStream((OutputStream)outputStream);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] bytes = new byte[2048];

            while(true) {
                int len;
                if ((len = inputStream.read(bytes)) == -1) {
                    ((OutputStream)outputStream).flush();
                    if (load) {
                        this.loadSize(new ByteArrayInputStream(output.toByteArray()));
                    }
                    break;
                }

                ((OutputStream)outputStream).write(bytes, 0, len);
                if (load) {
                    output.write(bytes, 0, len);
                }
            }
        } finally {
            if (outputStream != null) {
                ((OutputStream)outputStream).close();
            }

            if (inputStream != null) {
                inputStream.close();
            }

        }

    }

    private void loadSize(InputStream inputStream) throws IOException {
        BufferedImage image = ImageIO.read(inputStream);
        if (image == null) {
            throw new NullPointerException("图片解析失败，请提供正确的图片文件");
        } else {
            this.width = image.getWidth();
            this.height = image.getHeight();
            this.load = true;
        }
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public boolean isLoad() {
        return load;
    }

    public void setLoad(boolean load) {
        this.load = load;
    }
}