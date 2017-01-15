package com.example.myhttpsdemo;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import android.annotation.SuppressLint;

public class HttpsUtil {
	/**
	 * �а�ȫ֤���SSLContext
	 * 
	 * @param inStream
	 * @return SSLContext
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 * @throws CertificateException
	 * @throws KeyStoreException
	 * @throws UnrecoverableKeyException
	 * @throws KeyManagementException
	 */
	public static SSLContext getSSLContextWithCer() throws NoSuchAlgorithmException, IOException, CertificateException,
			KeyStoreException, UnrecoverableKeyException, KeyManagementException {
		// ʵ����SSLContext
		SSLContext sslContext = SSLContext.getInstance("SSL");

		// ��assets�м���֤��
		// ��HTTPSͨѶ����õ���cer/crt��pem
		InputStream inStream = MyApplication.getApplication().getAssets().open("lin.cer");

		/*
		 * X.509 ��׼�涨��֤����԰���ʲô��Ϣ����˵���˼�¼��Ϣ�ķ��� ������X.509֤���ʽ������
		 * cer/crt�����ڴ��֤�飬����2������ʽ��ŵģ�����˽Կ��
		 * pem��crt/cer������������Ascii����ʾ���������ڴ��֤���˽Կ��
		 */
		// ֤�鹤��
		CertificateFactory cerFactory = CertificateFactory.getInstance("X.509");
		Certificate cer = cerFactory.generateCertificate(inStream);

		// ��Կ��
		/*
		 * Pkcs12Ҳ��֤���ʽ PKCS#12�ǡ�������Ϣ�����﷨����������������x.509��֤���֤���Ӧ��˽Կ��������н�����
		 */
		KeyStore keyStory = KeyStore.getInstance("PKCS12");
//		 keyStory.load(inStream, "123456".toCharArray());
		keyStory.load(null, null);
		// ����֤�鵽��Կ����
		keyStory.setCertificateEntry("tsl", cer);

		// ��Կ������
		KeyManagerFactory kMFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		kMFactory.init(keyStory, null);
		// ���ι�����
		TrustManagerFactory tmFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmFactory.init(keyStory);

		// ��ʼ��sslContext
		sslContext.init(kMFactory.getKeyManagers(), tmFactory.getTrustManagers(), new SecureRandom());
		inStream.close();
		return sslContext;

	}

	/**
	 * û�а�ȫ֤���SSLContext
	 * 
	 * @return SSLContext
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	@SuppressLint("TrulyRandom")
	public static SSLContext getSSLContextWithoutCer() throws NoSuchAlgorithmException, KeyManagementException {
		// ʵ����SSLContext
		// �������������TSL Ҳ������SSL
		SSLContext sslContext = SSLContext.getInstance("SSL");
		sslContext.init(null, new TrustManager[] { trustManagers }, new SecureRandom());
		return sslContext;

	}

	private static TrustManager trustManagers = new X509TrustManager() {

		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[0];
		}
	};
	/**
	 * ��֤������
	 */
	public static HostnameVerifier hostnameVerifier = new HostnameVerifier() {

		@Override
		public boolean verify(String hostname, SSLSession session) {
			// TODO Auto-generated method stub
			return true;
		}
	};
}
