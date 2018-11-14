package com.crawler.artifact.http.httpclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

/**
 * Created by liuzhixiong on 2018/11/14.
 */

@SuppressWarnings("deprecation")
public class HttpComponentsUtil {

    private final String DEFLAUT_CHARSET = "utf-8";
    private CloseableHttpClient httpClient;

    LoginConfig loginConfig;

    private BasicCookieStore cookieStore = new BasicCookieStore();
    HttpClientContext context;
    RequestConfig requestConfig;
    CredentialsProvider credsProvider;

    // 创建连接管理器
    private PoolingHttpClientConnectionManager connManager = null;

    // 单例开始
    public HttpComponentsUtil(boolean enableSNI) {
        if (enableSNI == false)
            System.setProperty("jsse.enableSNIExtension", "false");
        creatHttpClientConnectionManager();
    }

    public HttpComponentsUtil(LoginConfig loginConfig) {
        this.loginConfig = loginConfig;
        creatHttpClientConnectionManager();
    }

    public HttpComponentsUtil() {
        creatHttpClientConnectionManager();
    }

    /**
     * 创建连接管理器
     */
    private void creatHttpClientConnectionManager() {
        try {
            if (httpClient != null) {
                return;
            }
            // 创建SSLSocketFactory
            // 定义socket工厂类 指定协议（Http、Https）
            org.apache.http.config.Registry<ConnectionSocketFactory> registry = RegistryBuilder
                    .<ConnectionSocketFactory>create().register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .register("https", createSSLConnSocketFactory()).build();

            // 创建连接管理器
            connManager = new PoolingHttpClientConnectionManager(registry);
            connManager.setMaxTotal(loginConfig.getMaxTotalConnections());// 设置最大连接数
            connManager.setDefaultMaxPerRoute(loginConfig.getMaxConnectionPerHost());// 设置每个路由默认连接数

            // 创建httpClient对象
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String string, SSLSession ssls) {
                    return true;
                }
            });
            httpClient = HttpClients.custom().addInterceptorLast(new HttpRequestInterceptor() {
                public void process(final HttpRequest request, final HttpContext context)
                        throws HttpException, IOException {
                }
            }).setConnectionManager(connManager).setRetryHandler(httpRequestRetry()).build();
            context = HttpClientContext.create();
            buildRequestConfig(true);

        } catch (Exception e) {
            System.out.println("获取httpClient(https)对象池异常:" + e.getMessage());
        }
    }

    public class TrustAllStrategy implements TrustStrategy {
        @Override
        public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            return true;
        }
    }

    /**
     * 创建SSL连接
     * @throws Exception
     */
    private SSLConnectionSocketFactory createSSLConnSocketFactory() throws Exception {
        // 创建TrustManager
        X509TrustManager xtm = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        // TLS1.0与SSL3.0基本上没有太大的差别，可粗略理解为TLS是SSL的继承者，但它们使用的是相同的SSLContext
        SSLContext ctx = SSLContext.getInstance("TLS");
        // 使用TrustManager来初始化该上下文，TrustManager只是被SSL的Socket所使用
        ctx.init(null, new TrustManager[] { xtm }, null);
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(ctx,
                SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        return sslsf;
    }

    /**
     * 配置请求连接重试机制
     */
    private HttpRequestRetryHandler httpRequestRetry() {
        HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                if (executionCount >= loginConfig.getMaxTryTimesToFetch()) {// 如果已经重试MAX_EXECUT_COUNT次，就放弃
                    return false;
                }
                if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
                    System.out.println("httpclient 服务器连接丢失");
                    return true;
                }
                if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
                    System.out.println("httpclient SSL握手异常");
                    return false;
                }
                if (exception instanceof InterruptedIOException) {// 超时
                    System.out.println("httpclient 连接超时");
                    return false;
                }
                if (exception instanceof UnknownHostException) {// 目标服务器不可达
                    System.out.println("httpclient 目标服务器不可达");
                    return false;
                }
                if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
                    System.out.println("httpclient 连接被拒绝");
                    return false;
                }
                if (exception instanceof SSLException) {// ssl握手异常
                    System.out.println("httpclient SSLException");
                    return false;
                }

                HttpClientContext clientContext = HttpClientContext.adapt(context);
                HttpRequest request = clientContext.getRequest();
                // 如果请求类型不是HttpEntityEnclosingRequest，被认为是幂等的，重试
                // HttpEntityEnclosingRequest指的是有请求体的request，比HttpRequest多一个Entity属性
                // Rest一般用GET请求获取数据，故幂等，POST用于新增数据，故不幂等
                if (!(request instanceof HttpEntityEnclosingRequest)) {
                    return true;
                }
                return false;
            }
        };
        return httpRequestRetryHandler;
    }

    /**
     * 关闭连接
     */
    public synchronized void close() {
        if (connManager == null) {
            return;
        }
        // 关闭连接池
        connManager.closeExpiredConnections();
        connManager.shutdown();
        // 设置httpClient失效
        try {
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connManager = null;
        httpClient = null;
    }

    /**
     * @return 连接是否开启(true or false)
     */
    public synchronized boolean isOpen() {
        if (connManager == null) {
            return false;
        }
        return true;
    }

/////////////////////////////////////////////get请求//////////////////////////////////////////////////

    public Response get(String url) {
        return get(url, null,DEFLAUT_CHARSET);
    }

    public Response get(String url, String charset) {
        return get(url, null, charset);
    }

    public Response get(String url, Map<String, String> headers) {
        return get(url, headers, DEFLAUT_CHARSET);
    }

    public Response get(String url, Map<String, String> headers, String charset) {
        context.setCookieStore(cookieStore);

        if (charset == null) {
            charset = DEFLAUT_CHARSET;
        }

        HttpGet httpGet = new HttpGet(url);
        setHeaders(httpGet, headers);
        setRequestConfig(httpGet);

        Response response = new Response();
        try {
            CloseableHttpResponse httpResponse = execute(httpGet);
            try {
                int stateCode = httpResponse.getStatusLine().getStatusCode();
                response.setStatus(Status.match(stateCode));
                if (stateCode != 0) {
                    InputStream in = getInputStream(httpResponse);
                    String content = getHTMLContent(in, charset);
                    response.setContent(content);
                    response.setProxy(getProxy());
                    response.setCookieStore(cookieStore);
                    response.setResponseHeaders(httpResponse.getAllHeaders());
                    Header location = httpResponse.getFirstHeader("location");
                    if (location != null)
                        response.setLocation(location.toString());
                }
            } finally {
                EntityUtils.consume(httpResponse.getEntity());
                httpResponse.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("connect to " + url + " fault!" + e);
        }
        return response;
    }

/////////////////////////////////////////////post请求//////////////////////////////////////////////////

    public Response post(String url, Map<String, String> params) {
        return post(url, params, null, DEFLAUT_CHARSET);
    }

    public Response post(String url, Map<String, String> params, String charset) {
        return post(url, params, null, charset);
    }

    public Response post(String url, Map<String, String> params, Map<String, String> headers) {
        return post(url, params, headers, DEFLAUT_CHARSET);
    }

    public Response post(String url, Map<String, String> params, Map<String, String> headers, String charset) {

        context.setCookieStore(cookieStore);

        if (charset == null) {
            charset = DEFLAUT_CHARSET;
        }
        Response response = new Response();
        try {
            HttpPost httpPost = new HttpPost(url);
            setHeaders(httpPost, headers);
            setNvps(httpPost, params, charset);
            setRequestConfig(httpPost);

            CloseableHttpResponse httpResponse = execute(httpPost);
            try {
                int stateCode = httpResponse.getStatusLine().getStatusCode();
                response.setStatus(Status.match(stateCode));
                if (stateCode != 0) {
                    InputStream in = getInputStream(httpResponse);
                    String content = getHTMLContent(in, charset);
                    response.setContent(content);
                    response.setCookieStore(cookieStore);
                    response.setProxy(getProxy());
                    response.setResponseHeaders(httpResponse.getAllHeaders());
                    Header location = httpResponse.getFirstHeader("location");
                    if (location != null)
                        response.setLocation(location.toString());
                }
            } finally {
                EntityUtils.consume(httpResponse.getEntity());
                httpResponse.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("connect to " + url + " fault！" + e.getMessage());
        }
        return response;
    }

    public Response post(String url, String params) {
        return post(url, params, null, DEFLAUT_CHARSET);
    }

    public Response post(String url, String params, Map<String, String> headers) {
        return post(url, params, headers, DEFLAUT_CHARSET);
    }

    public Response post(String url, String params, Map<String, String> headers, String charset) {

        context.setCookieStore(cookieStore);

        if (charset == null) {
            charset = DEFLAUT_CHARSET;
        }

        Response response = new Response();
        try {
            HttpPost httpPost = new HttpPost(url);
            setHeaders(httpPost, headers);
            setRequestConfig(httpPost);
            setNvps(httpPost, params);

            CloseableHttpResponse httpResponse = execute(httpPost);
            try {
                int stateCode = httpResponse.getStatusLine().getStatusCode();
                response.setStatus(Status.match(stateCode));
                if(stateCode != 0){
                    InputStream in = getInputStream(httpResponse);
                    String content = getHTMLContent(in, charset);
                    response.setContent(content);
                    response.setProxy(getProxy());
                    response.setCookieStore(cookieStore);
                    response.setResponseHeaders(httpResponse.getAllHeaders());
                    Header location = httpResponse.getFirstHeader("location");
                    if (location != null)
                        response.setLocation(location.toString());
                }
            } finally {
                EntityUtils.consume(httpResponse.getEntity());
                httpResponse.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("connect to " + url + " fault！" + e.getMessage());
        }
        return response;
    }

/////////////////////////////////////////////option请求//////////////////////////////////////////////////

    public Response option(String url) {
        return option(url,null, DEFLAUT_CHARSET);
    }

    public Response option(String url, String charset) {
        return option(url, null, charset);
    }

    public Response option(String url,  Map<String, String> headers) {
        return option(url, headers, DEFLAUT_CHARSET);
    }

    public Response option(String url, Map<String, String> headers, String charset) {
        context.setCookieStore(cookieStore);
        if (charset == null) {
            charset = DEFLAUT_CHARSET;
        }

        HttpOptions httpOptions = new HttpOptions(url);
        setHeaders(httpOptions, headers);
        setRequestConfig(httpOptions);

        Response response = new Response();
        try {
            CloseableHttpResponse httpResponse = execute(httpOptions);
            try {
                int stateCode = httpResponse.getStatusLine().getStatusCode();
                response.setStatus(Status.match(stateCode));
                if(stateCode != 0){
                    InputStream in = getInputStream(httpResponse);
                    String content = getHTMLContent(in, charset);
                    response.setContent(content);
                    response.setProxy(getProxy());
                    response.setCookieStore(cookieStore);
                    response.setResponseHeaders(httpResponse.getAllHeaders());
                    Header location = httpResponse.getFirstHeader("location");
                    if (location != null)
                        response.setLocation(location.toString());
                }
            } finally {
                EntityUtils.consume(httpResponse.getEntity());
                httpResponse.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("connect to " + url + " fault！" + e);
        }
        return response;
    }

    public Response option(String url, String charset, HttpHost proxy) {
        return option(url, null, charset, proxy);
    }

    public Response option(String url, Map<String, String> headers, String charset, HttpHost proxy) {
        setProxy(proxy);
        context.setCookieStore(cookieStore);
        if (charset == null) {
            charset = DEFLAUT_CHARSET;
        }
        HttpOptions httpOptions = new HttpOptions(url);
        setHeaders(httpOptions, headers);
        buildRequestConfig(true);
        setRequestConfig(httpOptions);

        Response response = new Response();
        try {
            CloseableHttpResponse httpResponse = execute(httpOptions);
            try {
                int stateCode = httpResponse.getStatusLine().getStatusCode();
                response.setStatus(Status.match(stateCode));
                if(stateCode != 0){
                    InputStream in = getInputStream(httpResponse);
                    String content = getHTMLContent(in, charset);
                    response.setContent(content);
                    response.setProxy(getProxy());
                    response.setCookieStore(cookieStore);
                    response.setResponseHeaders(httpResponse.getAllHeaders());
                    Header location = httpResponse.getFirstHeader("location");
                    if (location != null)
                        response.setLocation(location.toString());
                }
            } finally {
                EntityUtils.consume(httpResponse.getEntity());
                httpResponse.close();
            }
        } catch (Exception e) {
            System.out.println("connect to " + url + " fault！" + e);
        }
        return response;
    }

    /////////////////////////////////////////////getImage//////////////////////////////////////////////////
    public Response getImage(String url) {
        context.setCookieStore(cookieStore);

        HttpGet httpGet = new HttpGet(url);
        setRequestConfig(httpGet);

        Response response = new Response();
        try {
            CloseableHttpResponse httpResponse = execute(httpGet);
            try {
                int stateCode = httpResponse.getStatusLine().getStatusCode();
                response.setStatus(Status.match(stateCode));
                if (stateCode != 0) {
                    HttpEntity entity = httpResponse.getEntity();
                    byte[] content = EntityUtils.toByteArray(entity);
                    response.setBufferedImage(content);
                    response.setProxy(getProxy());
                    response.setCookieStore(cookieStore);
                    response.setResponseHeaders(httpResponse.getAllHeaders());
                    Header location = httpResponse.getFirstHeader("location");
                    if (location != null)
                        response.setLocation(location.toString());
                }
            } finally {
                EntityUtils.consume(httpResponse.getEntity());
                httpResponse.close();
            }
        } catch (Exception e) {
            System.out.println("connect to " + url + " fault！" + e);
        }
        return response;
    }

    public Response getImage(String url, Map<String, String> headers) {
        context.setCookieStore(cookieStore);

        HttpGet httpGet = new HttpGet(url);
        setHeaders(httpGet, headers);
        setRequestConfig(httpGet);

        Response response = new Response();
        try {
            CloseableHttpResponse httpResponse = execute(httpGet);
            try {
                int stateCode = httpResponse.getStatusLine().getStatusCode();
                response.setStatus(Status.match(stateCode));
                if (stateCode != 0) {
                    HttpEntity entity = httpResponse.getEntity();
                    byte[] content = EntityUtils.toByteArray(entity);
                    response.setProxy(getProxy());
                    response.setBufferedImage(content);
                    response.setCookieStore(cookieStore);
                    response.setResponseHeaders(httpResponse.getAllHeaders());
                    Header location = httpResponse.getFirstHeader("location");
                    if (location != null)
                        response.setLocation(location.toString());
                }
            } finally {
                EntityUtils.consume(httpResponse.getEntity());
                httpResponse.close();
            }
        } catch (Exception e) {
            System.out.println("connect to " + url + " fault！" + e);
        }
        return response;
    }

    public Response getImage(String url, HttpHost proxy) {
        setProxy(proxy);
        context.setCookieStore(cookieStore);
        HttpGet httpGet = new HttpGet(url);

        buildRequestConfig(true);
        setRequestConfig(httpGet);

        Response response = new Response();
        try {
            CloseableHttpResponse httpResponse = execute(httpGet);
            try {
                int stateCode = httpResponse.getStatusLine().getStatusCode();
                response.setStatus(Status.match(stateCode));
                if (stateCode != 0) {
                    HttpEntity entity = httpResponse.getEntity();
                    byte[] content = EntityUtils.toByteArray(entity);
                    response.setBufferedImage(content);
                    response.setResponseHeaders(httpResponse.getAllHeaders());
                    Header location = httpResponse.getFirstHeader("location");
                    if (location != null)
                        response.setLocation(location.toString());
                    response.setProxy(getProxy());
                    response.setCookieStore(cookieStore);
                }
            } finally {
                EntityUtils.consume(httpResponse.getEntity());
                httpResponse.close();
            }
        } catch (Exception e) {
            System.out.println("connect to " + url + " fault！" + e);
        }
        return response;
    }

/////////////////////////////////////////////cookies//////////////////////////////////////////////////
    /**
     * 打印当前cookies
     */
    public void printCookies() {
        System.out.println("---查看当前Cookie---");
        List<Cookie> cookies = cookieStore.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                System.out.println(c.getName() + " : " + c.getValue());
                System.out.println("domain : " + c.getDomain());
                System.out.println("expires : " + c.getExpiryDate());
                System.out.println("path : " + c.getPath());
                System.out.println("version : " + c.getVersion());
            }
        }else{
            System.out.println("Cookie 为空");
        }
    }

    public BasicCookieStore getCookieStore() {
        return cookieStore;
    }

    public void setCookieStore(BasicCookieStore cookieStore) {
        this.cookieStore = cookieStore;
    }

    public void addCookies(BasicClientCookie cookie) {
        if (cookie.getDomain() == null)
            cookie.setDomain("127.0.0.1");
        cookieStore.addCookie(cookie);

    }

    public void addCookies(String key, String vaule, String domain, String path) {
        BasicClientCookie cookie = new BasicClientCookie(key, vaule);
        cookie.setDomain(domain);
        cookie.setPath(path);
        cookieStore.addCookie(cookie);

    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 获取网页文本信息
     * @param in
     * @param charset
     * @return 返回文本
     * @throws UnsupportedEncodingException
     */
    private static String getHTMLContent(InputStream in, String charset) throws UnsupportedEncodingException {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(in, charset));
        try {
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    public HttpClientContext getContext() {
        return context;
    }

    public void setContext(HttpClientContext context) {
        this.context = context;
    }

    public CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public RequestConfig getRequestConfig() {
        return requestConfig;
    }

    public void setRequestConfig(RequestConfig requestConfig) {
        this.requestConfig = requestConfig;
    }

    public CredentialsProvider getCredsProvider() {
        return credsProvider;
    }

    public void setCredsProvider(CredentialsProvider credsProvider) {
        this.credsProvider = credsProvider;
    }

    public PoolingHttpClientConnectionManager getConnManager() {
        return connManager;
    }

    public void setConnManager(PoolingHttpClientConnectionManager connManager) {
        this.connManager = connManager;
    }

    public String get_DEFLAUT_CHARSET() {
        return DEFLAUT_CHARSET;
    }

    public int getMaxTryTimesToFetch() {
        return this.loginConfig.getMaxTryTimesToFetch();
    }

    public void setMaxTryTimesToFetch(int maxTryTimesToFetch) {
        this.loginConfig.setMaxTryTimesToFetch(maxTryTimesToFetch);
    }

    public int getMaxTotalConnections() {
        return this.loginConfig.getMaxTotalConnections();
    }

    public void setMaxTotalConnections(int maxTotalConnections) {
        this.loginConfig.setMaxTotalConnections(maxTotalConnections);
    }

    public int getMaxConnectionPerHost() {
        return this.loginConfig.getMaxConnectionPerHost();
    }

    public void setMaxConnectionPerHost(int maxConnectionPerHost) {
        this.loginConfig.setMaxConnectionPerHost(maxConnectionPerHost);
    }

    public boolean getisFollowRedirects() {
        return this.loginConfig.isFollowRedirects();
    }

    public void setisFollowRedirects(boolean followRedirects) {
        this.loginConfig.setFollowRedirects(followRedirects);
    }

    /**
     * 设置请求头
     * @param httpRequestBase
     * @param headers
     */
    private void setHeaders(HttpRequestBase httpRequestBase, Map<String, String> headers) {
        if (headers != null) {
            for (String key : headers.keySet()) {
                httpRequestBase.setHeader(key, headers.get(key));
            }
        }
    }

    /**
     * 设置请求配置信息
     * @param httpRequestBase
     */
    private void setRequestConfig(HttpRequestBase httpRequestBase) {
        httpRequestBase.setConfig(requestConfig);
    }

    /**
     * 执行请求
     * @param httpRequestBase
     * @return
     */
    private CloseableHttpResponse execute(HttpRequestBase httpRequestBase) {
        try {
            return httpClient.execute(httpRequestBase, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取IO流
     * @param httpResponse
     * @return
     */
    private InputStream getInputStream(CloseableHttpResponse httpResponse) {
        InputStream in = null;

        HttpEntity entity = httpResponse.getEntity();
        Header header = entity.getContentEncoding();
        try {
            if (header != null && header.getValue().indexOf("gzip") != -1) { // 判断返回内容是否为gzip压缩格式
                GzipDecompressingEntity gzipEntity = new GzipDecompressingEntity(entity);
                in = gzipEntity.getContent();
            } else {
                in = entity.getContent();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return in;
    }

    /**
     * 构建RequestConfig
     * @param enable
     * @return
     */
    private RequestConfig buildRequestConfig(boolean enable) {
        return RequestConfig.custom()
                .setConnectionRequestTimeout(loginConfig.getConnectTimeout())// 设置从连接池获取连接实例的超时
                .setConnectTimeout(loginConfig.getConnectTimeout())// 设置连接超时
                .setSocketTimeout(loginConfig.getSocketTimeout())// 设置读取超时
                .setRedirectsEnabled(loginConfig.isFollowRedirects())//设置是否重定向
                .setCircularRedirectsAllowed(true)//设置是否循环重定向
                .setProxy(loginConfig.getProxy())//设置代理
                .setStaleConnectionCheckEnabled(true)//检查是否为陈旧的连接
                .build();
    }

    /**
     * 设置NameValuePairs
     * @param httpPost
     * @param params
     * @param charset
     */
    private void setNvps(HttpPost httpPost, Map<String, String> params, String charset) {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        if (params != null) {
            for (String key : params.keySet()) {
                nvps.add(new BasicNameValuePair(key, params.get(key)));
            }
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nvps, charset));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置NameValuePairs
     * @param httpPost
     * @param params
     */
    private void setNvps(HttpPost httpPost, String params) {
        try {
            httpPost.setEntity(new StringEntity(params));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置代理
     * @param proxy
     */
    public void setProxy(HttpHost proxy){
        this.loginConfig.setProxy(proxy);
        //读取loginConfig配置
        this.requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(loginConfig.getConnectTimeout())// 设置从连接池获取连接实例的超时
                .setConnectTimeout(loginConfig.getConnectTimeout())// 设置连接超时
                .setSocketTimeout(loginConfig.getSocketTimeout())// 设置读取超时
                .setRedirectsEnabled(loginConfig.isFollowRedirects())//设置是否重定向
                .setCircularRedirectsAllowed(true)//设置是否循环重定向
                .setProxy(proxy)//设置代理
                .setStaleConnectionCheckEnabled(true)//检查是否为陈旧的连接
                .build();
    }

    /**
     * @return 获取到的代理
     */
    public HttpHost getProxy() {
        return this.loginConfig.getProxy();
    }

    /**
     * 认证机制
     * @param userName 代理账户名
     * @param password 代理密码
     */
    public void setCredentials(String userName, String password) {
        credsProvider = new BasicCredentialsProvider();
//		System.out.println("loginConfig.getProxyIP()=" + loginConfig.getProxyIP());
        credsProvider.setCredentials(new AuthScope(loginConfig.getProxyIP(), loginConfig.getProxyPort()),
                new UsernamePasswordCredentials(userName, password));
        // 创建认证缓存
        AuthCache authCache = new BasicAuthCache();
        // 创建基础认证机制 添加到缓存
        BasicScheme basicAuth = new BasicScheme();
        authCache.put(loginConfig.getProxy(), basicAuth);
        context.setCredentialsProvider(credsProvider);
        // context.setAuthCache(authCache);
        httpClient = HttpClients.custom().addInterceptorLast(new HttpRequestInterceptor() {
            public void process(final HttpRequest request, final HttpContext context)
                    throws HttpException, IOException {
            }
        }).setConnectionManager(connManager).setSSLHostnameVerifier(new DefaultHostnameVerifier(null))
                .setRetryHandler(httpRequestRetry()).setDefaultCredentialsProvider(credsProvider).build();
    }

    /**
     * 同时设置host以及认证信息
     * @param proxy 代理
     * @param userName 账户名
     * @param password 密码
     */
    public void setProxyAndCredentials(HttpHost proxy, String userName, String password) {

        this.loginConfig.setProxy(proxy);
        //读取loginConfig配置
        this.requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(loginConfig.getConnectTimeout())// 设置从连接池获取连接实例的超时
                .setConnectTimeout(loginConfig.getConnectTimeout())// 设置连接超时
                .setSocketTimeout(loginConfig.getSocketTimeout())// 设置读取超时
                .setRedirectsEnabled(loginConfig.isFollowRedirects())//设置是否重定向
                .setCircularRedirectsAllowed(true)//设置是否循环重定向
                .setProxy(proxy)//设置代理
                .setStaleConnectionCheckEnabled(true)//检查是否为陈旧的连接
                .build();

        credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(new AuthScope(proxy.getHostName(), proxy.getPort()),
                new UsernamePasswordCredentials(userName, password));
        // 创建认证缓存
        AuthCache authCache = new BasicAuthCache();
        // 创建基础认证机制 添加到缓存
        BasicScheme basicAuth = new BasicScheme();
        authCache.put(proxy, basicAuth);
        context.setCredentialsProvider(credsProvider);
        // context.setAuthCache(authCache);
        httpClient = HttpClients.custom().addInterceptorLast(new HttpRequestInterceptor() {
            public void process(final HttpRequest request, final HttpContext context)
                    throws HttpException, IOException {
            }
        }).setConnectionManager(connManager).setSSLHostnameVerifier(new DefaultHostnameVerifier(null))
                .setRetryHandler(httpRequestRetry()).setDefaultCredentialsProvider(credsProvider).build();

    }

    /**
     * 设置统一超时时间
     * @param millsSeconds 自定义时间(毫秒)
     */
    public void setTimeout(int millsSeconds) {
        this.loginConfig.setConnectTimeout(millsSeconds);
        this.loginConfig.setSocketTimeout(millsSeconds);
        if (loginConfig.getProxy() != null) {
            this.requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(loginConfig.getConnectTimeout())// 设置从连接池获取连接实例的超时
                    .setConnectTimeout(loginConfig.getConnectTimeout())// 设置连接超时
                    .setSocketTimeout(loginConfig.getSocketTimeout())// 设置读取超时
                    .setRedirectsEnabled(loginConfig.isFollowRedirects())//设置是否重定向
                    .setCircularRedirectsAllowed(true)//设置是否循环重定向
                    .setProxy(loginConfig.getProxy())//设置代理
                    .setStaleConnectionCheckEnabled(true)//检查是否为陈旧的连接
                    .build();
        } else {
            requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(loginConfig.getConnectTimeout())// 设置从连接池获取连接实例的超时
                    .setConnectTimeout(loginConfig.getConnectTimeout())// 设置连接超时
                    .setSocketTimeout(loginConfig.getSocketTimeout())// 设置读取超时
                    .setRedirectsEnabled(loginConfig.isFollowRedirects())//设置是否重定向
                    .setCircularRedirectsAllowed(true)//设置是否循环重定向
                    .setStaleConnectionCheckEnabled(true)//检查是否为陈旧的连接
                    .build();
        }
    }

    /**
     * @return 统一timeout时间
     */
    public int getTimeout() {
        return this.loginConfig.getConnectTimeout();
    }

    /**
     * 设置connection超时时间，其余超时默认
     * @param millsSeconds 自定义时间(毫秒)
     */
    public void setConnectionTimeout(int millsSeconds){
        this.loginConfig.setConnectTimeout(millsSeconds);
        if (loginConfig.getProxy() != null) {
            this.requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(loginConfig.getConnectTimeout())// 设置从连接池获取连接实例的超时
                    .setConnectTimeout(loginConfig.getConnectTimeout())// 设置连接超时
                    .setSocketTimeout(loginConfig.getSocketTimeout())// 设置读取超时
                    .setRedirectsEnabled(loginConfig.isFollowRedirects())//设置是否重定向
                    .setCircularRedirectsAllowed(true)//设置是否循环重定向
                    .setProxy(loginConfig.getProxy())//设置代理
                    .setStaleConnectionCheckEnabled(true)//检查是否为陈旧的连接
                    .build();
        } else {
            this.requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(loginConfig.getConnectTimeout())// 设置从连接池获取连接实例的超时
                    .setConnectTimeout(loginConfig.getConnectTimeout())// 设置连接超时
                    .setSocketTimeout(loginConfig.getSocketTimeout())// 设置读取超时
                    .setRedirectsEnabled(loginConfig.isFollowRedirects())//设置是否重定向
                    .setCircularRedirectsAllowed(true)//设置是否循环重定向
                    .setStaleConnectionCheckEnabled(true)//检查是否为陈旧的连接
                    .build();
        }
    }

    /**
     * @return 设定的connection超时时间
     */
    public int getConnectionTimeout() {
        return this.loginConfig.getConnectTimeout();
    }

    /**
     * 设置Socket超时时间，其余超时默认
     * @param millsSeconds 自定义时间(毫秒)
     */
    public void setSocketTimeout(int millsSeconds) {
        this.loginConfig.setSocketTimeout(millsSeconds);
        if (loginConfig.getProxy() != null) {
            requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(loginConfig.getConnectTimeout())// 设置从连接池获取连接实例的超时
                    .setConnectTimeout(loginConfig.getConnectTimeout())// 设置连接超时
                    .setSocketTimeout(loginConfig.getSocketTimeout())// 设置读取超时
                    .setRedirectsEnabled(loginConfig.isFollowRedirects())//设置是否重定向
                    .setCircularRedirectsAllowed(true)//设置是否循环重定向
                    .setProxy(loginConfig.getProxy())//设置代理
                    .setStaleConnectionCheckEnabled(true)//检查是否为陈旧的连接
                    .build();
        } else {
            requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(loginConfig.getConnectTimeout())// 设置从连接池获取连接实例的超时
                    .setConnectTimeout(loginConfig.getConnectTimeout())// 设置连接超时
                    .setSocketTimeout(loginConfig.getSocketTimeout())// 设置读取超时
                    .setRedirectsEnabled(loginConfig.isFollowRedirects())//设置是否重定向
                    .setCircularRedirectsAllowed(true)//设置是否循环重定向
                    .setStaleConnectionCheckEnabled(true)//检查是否为陈旧的连接
                    .build();
        }
    }

    /**
     * @return 设定的socket超时时间
     */
    public int getSocketTimeout() {
        return this.loginConfig.getSocketTimeout();
    }

}