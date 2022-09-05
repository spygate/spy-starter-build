package spy.project.utils;

import okhttp3.*;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * okhttp
 * 支持http，https请求
 */
public class HttpUtils {

    private static final Integer OKHTTP_READ_TIMEOUT = 5;
    private static final Integer OKHTTP_CONNECT_TIMEOUT = 10;
    private static final Integer OKHTTP_WRITE_TIMEOUT = 5;
    private static final OkHttpClient HTTP_CLIENT = okHttpBuilder().build();
    private static final OkHttpClient HTTPS_CLIENT = okHttpBuilder().connectionSpecs(Collections.singletonList(ConnectionSpec.COMPATIBLE_TLS)).build();

    private enum ProtocolType {
        HTTP, HTTPS
    }

    public static <T> T get(String url, Class<T> responseType) {
        Request request = new Request.Builder().url(url).get().build();
        OkHttpClient okHttpClient = getHttpClient(url);
        try(Response response = okHttpClient.newCall(request).execute()) {
            assert response.body() != null;
            return JsonUtils.toObject(response.body().string(), responseType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static <T> T post(String url, String jsonStr, Class<T> responseType) {
        RequestBody requestBody = RequestBody.create(MediaType.get("application/json;charset=utf-8"), jsonStr);
        Request request = new Request.Builder().url(url).post(requestBody).build();
        OkHttpClient okHttpClient = getHttpClient(url);
        try (Response response = okHttpClient.newCall(request).execute()){
            assert response.body() != null;
            return JsonUtils.toObject(response.body().string(), responseType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private static OkHttpClient getHttpClient(String url) {
        if(url.startsWith(ProtocolType.HTTPS.name().toLowerCase())) {
            return HTTPS_CLIENT;
        } else {
            return HTTP_CLIENT;
        }
    }

    private static OkHttpClient.Builder okHttpBuilder() {
        try {
            TrustManager[] trustManagers = buildTrustManagers();
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustManagers, new SecureRandom());
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.readTimeout(OKHTTP_READ_TIMEOUT, TimeUnit.SECONDS);
            builder.connectTimeout(OKHTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS);
            builder.writeTimeout(OKHTTP_WRITE_TIMEOUT, TimeUnit.SECONDS);
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustManagers[0]);
            builder.hostnameVerifier((s, sslSession) -> true);
            return builder;
        } catch (Exception e) {
            e.printStackTrace();
            return new OkHttpClient.Builder();
        }
    }

    private static TrustManager[] buildTrustManagers() {
        return new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }};
    }


}
