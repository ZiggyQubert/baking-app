package com.ziggyqubert.android.baking_app.utilities;

import android.annotation.SuppressLint;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class SSLCertificateChecking {

    /**
     * Disables the SSL certificate checking for new instances of {@link HttpsURLConnection} This has been created to
     * aid testing on a local box, not for use on production. call in onCreate or before any network calls are made
     * code taken from https://gist.github.com/aembleton/889392
     */
    public static void disableSSLCertificateChecking() {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @SuppressLint("TrustAllX509TrustManager")
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {

            }

            @SuppressLint("TrustAllX509TrustManager")
            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {

            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

        }};

        try {
            SSLContext sc = SSLContext.getInstance("TLS");

            sc.init(null, trustAllCerts, new java.security.SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }
}
