package http.client;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HTTP;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class HttpClientPool {
	private final static Logger LOGGER = LoggerFactory.getLogger(HttpClientPool.class);

	private static final String CONTENT_TYPE_TEXT_HTML = "text/xml";
	private static final String CONTENT_TYPE_JSON_URL = "application/json;charset=utf-8";
	private static final String CONTENT_TYPE_WWW_FORM = "application/x-www-form-urlencoded;charset=utf-8";

	private final String CHARSET;
	private PoolingHttpClientConnectionManager pool;
	private ConnectionConfig connectionConfig;
	private RequestConfig requestConfig;
	private CloseableHttpClient httpClient;

	public HttpClientPool() {
		this("UTF-8");
	}

	public HttpClientPool(String charset) {
		this(charset, 10);
	}

	public HttpClientPool(String charset, int timeout) {
		this.CHARSET = charset;
		setConnectionConfig(4 * 1024);

		timeout *= 1000;
		setTimeoutConfig(timeout, timeout, timeout);
	}

	public HttpClientPool init(int poolSize) {
		try {
			SSLContextBuilder builder = new SSLContextBuilder()
					.loadTrustMaterial(null, new TrustSelfSignedStrategy());

			SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(builder.build());

			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
					.register("http", PlainConnectionSocketFactory.getSocketFactory())
					.register("https", socketFactory).build();

			pool = new PoolingHttpClientConnectionManager(socketFactoryRegistry);

			pool.setMaxTotal(poolSize);
			pool.setDefaultMaxPerRoute(20);
		} catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
			LOGGER.error("", e);
		}

		return this;
	}

	public void setTimeoutConfig(int socketTimeout, int connectTimeout, int requestTimeout) {
		requestConfig = RequestConfig.custom()
				.setSocketTimeout(socketTimeout)
				.setConnectTimeout(connectTimeout)
				.setConnectionRequestTimeout(requestTimeout).build();

	}

	public void setConnectionConfig(int size) {
		connectionConfig = ConnectionConfig.custom()
				.setBufferSize(size)
				.build();
	}

	public CloseableHttpClient getClient() {
		if (null != httpClient) {
			return httpClient;
		}

		synchronized (this) {
			if (null == httpClient) {
				httpClient = HttpClients.custom()
						.setConnectionManager(pool)
						.setDefaultRequestConfig(requestConfig)
						.setDefaultConnectionConfig(connectionConfig)
						.setKeepAliveStrategy((httpResponse, httpContext) -> {
							Header[] headers = httpResponse.getAllHeaders();
							if (null != headers) {
								Header header;
								HeaderElement headerElement;
								for (Header header1 : headers) {
									header = header1;
									HeaderElement[] headerElements = header.getElements();
									if (null == headerElements) {
										continue;
									}

									for (HeaderElement headerElement1 : headerElements) {
										headerElement = headerElement1;
										if (!headerElement.getName().toUpperCase().contains(HTTP.CONN_KEEP_ALIVE.toUpperCase())) {
											continue;
										}

										if (isNullOrEmpty(headerElement.getValue())) {
											return 10 * 1000;
										}

										return Long.parseLong(headerElement.getValue()) * 1000;
									}
								}
							}

							return 10 * 1000;
						})
						.setRetryHandler(new DefaultHttpRequestRetryHandler())
						.build();
			}
		}

		return httpClient;
	}

	public String sendPost(HttpPost httpPost) {
		String content = null;
		CloseableHttpResponse httpResponse = null;
		try {
			httpPost.setConfig(requestConfig);

			CloseableHttpClient httpClient = getClient();
			httpResponse = httpClient.execute(httpPost, HttpClientContext.create());

			if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				LOGGER.error("httpPost:{}",httpPost.toString());
				throw new RuntimeException("HTTP Request is not success, Response code is " + httpResponse.getStatusLine().getStatusCode());
			} else {
				HttpEntity entity = httpResponse.getEntity();
				if (null != entity) {
					content = EntityUtils.toString(entity, CHARSET);
					EntityUtils.consume(entity);
				}
			}
		} catch (Exception e) {
			LOGGER.error("", e);
		} finally {
			if (null != httpResponse) {
				try {
					httpResponse.close();
				} catch (Exception e) {
					LOGGER.error("", e);
				}
			}
		}

		return content;
	}

	public String sendGet(HttpGet httpGet) {
		String content = null;
		CloseableHttpResponse httpResponse = null;
		try {
			httpGet.setConfig(requestConfig);
			CloseableHttpClient httpClient = getClient();
			httpResponse = httpClient.execute(httpGet, HttpClientContext.create());

			if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new RuntimeException("HTTP Request is not success, Response code is " + httpResponse.getStatusLine().getStatusCode());
			} else {
				HttpEntity entity = httpResponse.getEntity();
				if (null != entity) {
					content = EntityUtils.toString(entity, CHARSET);
					EntityUtils.consume(entity);
				}
			}
		} catch (Exception e) {
			LOGGER.error("", e);
		} finally {
			if (null != httpResponse) {
				try {
					httpResponse.close();
				} catch (Exception e) {
					LOGGER.error("", e);
				}
			}
		}

		return content;
	}

	public String sendPost(String url, Map<String, String> header) {
		if (isNullOrEmpty(url)) {
			return null;
		}

		HttpPost httpPost = new HttpPost(url);
		if (null != header && !header.isEmpty()) {
			for (Map.Entry<String, String> entry : header.entrySet()) {
				httpPost.addHeader(entry.getKey(), entry.getValue());
			}
		}

		return sendPost(httpPost);
	}

	public String sendPostHeadBody(String url, Map<String, String> header, String content) {
		if (isNullOrEmpty(url)) {
			return null;
		}

		HttpPost httpPost = new HttpPost(url);
		if (null != header && !header.isEmpty()) {
			for (Map.Entry<String, String> entry : header.entrySet()) {
				httpPost.addHeader(entry.getKey(), entry.getValue());
			}
		}
		if (!isNullOrEmpty(content)) {
			StringEntity stringEntity = new StringEntity(content, CHARSET);
			stringEntity.setContentType(CONTENT_TYPE_JSON_URL);
			httpPost.setEntity(stringEntity);
		}

		return sendPost(httpPost);
	}

	public String sendPost(String url, String content) {
		if (isNullOrEmpty(url)) {
			return null;
		}

		HttpPost httpPost = new HttpPost(url);
		if (!isNullOrEmpty(content)) {
			StringEntity stringEntity = new StringEntity(content, CHARSET);
			stringEntity.setContentType(CONTENT_TYPE_JSON_URL);
			httpPost.setEntity(stringEntity);
		}

		return sendPost(httpPost);
	}

	public String sendPosFormHeadBody(String url, Map<String, String> header, String content) {
		if (isNullOrEmpty(url)) {
			return null;
		}

		HttpPost httpPost = new HttpPost(url);
		if (null != header && !header.isEmpty()) {
			for (Map.Entry<String, String> entry : header.entrySet()) {
				httpPost.addHeader(entry.getKey(), entry.getValue());
			}
		}
		if (!isNullOrEmpty(content)) {
			StringEntity stringEntity = new StringEntity(content, CHARSET);
			stringEntity.setContentType(CONTENT_TYPE_WWW_FORM);
			httpPost.setEntity(stringEntity);
		}

		return sendPost(httpPost);
	}

	public String sendPostForm(String url, String content) {
		if (isNullOrEmpty(url)) {
			return null;
		}
		HttpPost httpPost = new HttpPost(url);
		if (!isNullOrEmpty(content)) {
			StringEntity stringEntity = new StringEntity(content, CHARSET);
			stringEntity.setContentType(CONTENT_TYPE_WWW_FORM);
			httpPost.setEntity(stringEntity);
		}

		return sendPost(httpPost);
	}

	public String sendGet(String url) {
		if (isNullOrEmpty(url)) {
			return null;
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("{}", url);
		}

		HttpGet httpGet = new HttpGet(url);
		return sendGet(httpGet);
	}

	public String sendGetHead(String url, Map<String, String> header) {
		if (isNullOrEmpty(url)) {
			return null;
		}

		HttpGet httpGet = new HttpGet(url);
		if (null != header && !header.isEmpty()) {
			for (Map.Entry<String, String> entry : header.entrySet()) {
				httpGet.addHeader(entry.getKey(), entry.getValue());
			}
		}
		return sendGet(httpGet);
	}

	public String sendDelete(HttpDelete httpDelete) {
		String content = null;
		CloseableHttpResponse httpResponse = null;
		try {
			httpDelete.setConfig(requestConfig);

			CloseableHttpClient httpClient = getClient();
			httpResponse = httpClient.execute(httpDelete, HttpClientContext.create());

			if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new RuntimeException("HTTP Request is not success, Response code is " + httpResponse.getStatusLine().getStatusCode());
			} else {
				HttpEntity entity = httpResponse.getEntity();
				if (null != entity) {
					content = EntityUtils.toString(entity, CHARSET);
					EntityUtils.consume(entity);
				}
			}
		} catch (Exception e) {
			LOGGER.error("", e);
		} finally {
			if (null != httpResponse) {
				try {
					httpResponse.close();
				} catch (Exception e) {
					LOGGER.error("", e);
				}
			}
		}

		return content;
	}

	private static boolean isNullOrEmpty(String data) {
		return (null == data || data.isEmpty());
	}
}
