package com.quick.core.net;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;


/**
 * Created by dailichun on 2017/12/7.
 * http网络请求帮助类
 */
public class HttpUtil {

    public static final int CONNECTION_TIME_OUT_TIME = 20 * 1000;

    /**
     * 调用默认数据类型接口
     *
     * @param url
     * @param body
     * @return
     */
    public static String httpPost(String url, String body) {
        return httpPost(url, body, "application/octet-stream");
    }

    /**
     * 指定ContentType调用接口
     *
     * @param url
     * @param body
     * @param ContentType
     * @return
     */
    public static String httpPost(String url, String body, String ContentType) {
        String bs = null;
        try {
            URL uri = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setReadTimeout(CONNECTION_TIME_OUT_TIME);
            conn.setConnectTimeout(CONNECTION_TIME_OUT_TIME);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", ContentType);

            DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
            outStream.write(body.toString().getBytes());

            outStream.flush();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = conn.getInputStream();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buffer = new byte[256];
                int n;
                while ((n = in.read(buffer)) >= 0) {
                    out.write(buffer, 0, n);
                }
                byte[] b = out.toByteArray();
                in.close();
                bs = new String(b);
            }
            outStream.close();
            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bs;
    }

    public static String get(String url) {
        String responseString = null;

        //创建HttpClient对象
        HttpClient httpClient = new DefaultHttpClient();
        //设置get请求对象的链接超时时间
        httpClient.getParams().setParameter(
                CoreConnectionPNames.CONNECTION_TIMEOUT,
                CONNECTION_TIME_OUT_TIME);
        //设置get请求的数据读取时间
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
                CONNECTION_TIME_OUT_TIME);
        //创建get对象
        HttpGet get = new HttpGet(url);
        try {
            //执行请求
            HttpResponse httpResponse = httpClient.execute(get);
            HttpEntity entity = httpResponse.getEntity();
            //判断请求数据是否正常返回
            if (httpResponse.getStatusLine().getStatusCode() == 200 && entity != null) {
                //获取数据流，保存到byte字节数组中
                InputStream in = entity.getContent();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int n;
                while ((n = in.read(buffer)) >= 0) {
                    out.write(buffer, 0, n);
                }
                byte[] b = out.toByteArray();
                out.close();
                in.close();
                //将字节数组转为String对象
                responseString = new String(b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseString;
    }

    @Deprecated
    public static String getData(String url) {
        return get(url);
    }

    /**
     * 提交Post请求
     *
     * @param url
     * @param params
     * @param fList
     * @return
     */
    public static String post(String url, Map<String, String> params, List<File> fList) throws IOException {
        String returnStr = null;

        String BOUNDARY = java.util.UUID.randomUUID().toString();
        String PREFIX = "--";
        String LINEND = "\r\n";

        URL uri = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
        conn.setReadTimeout(CONNECTION_TIME_OUT_TIME);
        conn.setConnectTimeout(CONNECTION_TIME_OUT_TIME);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Connection", "keep-alive");
        conn.setRequestProperty("Charsert", "UTF-8");
        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + BOUNDARY);

        StringBuilder formStr = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            formStr.append(PREFIX);
            formStr.append(BOUNDARY);
            formStr.append(LINEND);
            formStr.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINEND);
            formStr.append("Content-Type: text/plain; charset=UTF-8" + LINEND);
            formStr.append("Content-Transfer-Encoding: 8bit" + LINEND);
            formStr.append(LINEND);
            formStr.append(entry.getValue());
            formStr.append(LINEND);
        }
        DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
        outStream.write(formStr.toString().getBytes());
        for (File file : fList) {
            StringBuilder fileStr = new StringBuilder();
            fileStr.append(PREFIX);
            fileStr.append(BOUNDARY);
            fileStr.append(LINEND);
            fileStr.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"" + LINEND);
            fileStr.append("Content-Type: application/octet-stream; charset=UTF-8" + LINEND);
            fileStr.append(LINEND);
            outStream.write(fileStr.toString().getBytes());
            InputStream is = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            is.close();
            outStream.write(LINEND.getBytes());
        }

        //请求结束标志
        byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
        outStream.write(end_data);
        outStream.flush();

        if (conn.getResponseCode() == 200) {
            InputStream in = conn.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[256];
            int n;
            while ((n = in.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            byte[] b = out.toByteArray();
            in.close();
            returnStr = new String(b);
        }
        outStream.close();
        conn.disconnect();

        return returnStr;
    }

    @Deprecated
    public static String post(String Url, List<NameValuePair> params) {
        String strResult = null;
        HttpResponse httpResponse;
        HttpPost httpRequest = new HttpPost(Url);
        try {
            httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            httpResponse = new DefaultHttpClient().execute(httpRequest);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                strResult = EntityUtils.toString(httpResponse.getEntity());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strResult;
    }
}
