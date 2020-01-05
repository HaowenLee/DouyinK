package com.example.douyink;

import java.io.IOException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

public class UnsafeOkHttpClient {

    /* renamed from: com.niming.framework.image.UnsafeOkHttpClient$1 */
    static class C22951 implements X509TrustManager {

        public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
        }

        C22951() {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    /* renamed from: com.niming.framework.image.UnsafeOkHttpClient$2 */
    static class C22962 implements HostnameVerifier {
        public boolean verify(String str, SSLSession sSLSession) {
            return true;
        }

        C22962() {
        }
    }

    /* renamed from: com.niming.framework.image.UnsafeOkHttpClient$3 */
    static class C22973 extends ProxySelector {
        public void connectFailed(URI uri, SocketAddress socketAddress, IOException iOException) {
        }

        C22973() {
        }

        public List<Proxy> select(URI uri) {
            return Collections.singletonList(Proxy.NO_PROXY);
        }
    }

    public static OkHttpClient getUnsafeOkHttpClient() {
        try {
            TrustManager[] trustManagerArr = new TrustManager[]{new C22951()};
            SSLContext instance = SSLContext.getInstance("SSL");
            instance.init(null, trustManagerArr, new SecureRandom());
            SSLSocketFactory socketFactory = instance.getSocketFactory();
            OkHttpClient.Builder c3435a = new OkHttpClient.Builder();
            c3435a.sslSocketFactory(socketFactory, (X509TrustManager) trustManagerArr[0]);
            c3435a.hostnameVerifier(new C22962());
            c3435a.proxySelector(new C22973());
            c3435a.connectTimeout(30, TimeUnit.SECONDS);
            c3435a.readTimeout(30, TimeUnit.SECONDS);
            return c3435a.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}