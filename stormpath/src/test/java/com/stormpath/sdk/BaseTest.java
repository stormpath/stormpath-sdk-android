package com.stormpath.sdk;

import com.stormpath.sdk.utils.MockAndroidPlatform;
import com.stormpath.sdk.utils.ResourceUtils;

import org.junit.After;
import org.junit.Before;

import java.net.HttpURLConnection;
import java.util.concurrent.TimeUnit;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

public class BaseTest {

    private MockWebServer mockWebServer;

    private Platform mockPlatform;

    @Before
    public void setUp() throws Exception {
        mockPlatform = new MockAndroidPlatform();
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @After
    public void tearDown() {
        Stormpath.reset();
        try {
            mockWebServer.shutdown();
        } catch (Throwable t) {
            // Frankly, my dear, I don't give a damn
        }
    }

    protected String mockServerUrl() {
        return mockWebServer.url("/").toString();
    }

    protected Platform mockPlatform() {
        return mockPlatform;
    }

    protected void initWithDefaults() {
        StormpathConfiguration config = new StormpathConfiguration.Builder().baseUrl(mockServerUrl()).build();
        Stormpath.init(mockPlatform, config);
    }

    protected void enqueueResponse(MockResponse mockResponse) {
        mockWebServer.enqueue(mockResponse);
    }

    protected void enqueueStringResponse(String body) {
        MockResponse mockResponse = new MockResponse().setBody(body).setResponseCode(HttpURLConnection.HTTP_OK);
        mockWebServer.enqueue(mockResponse);
    }

    protected void enqueueResponse(String filename) {
        String body = ResourceUtils.readFromFile(filename);
        MockResponse mockResponse = new MockResponse().setBody(body).setResponseCode(HttpURLConnection.HTTP_OK);
        mockWebServer.enqueue(mockResponse);
    }

    protected void enqueueResponse(String filename, int statusCode) {
        String body = ResourceUtils.readFromFile(filename);
        MockResponse mockResponse = new MockResponse().setBody(body).setResponseCode(statusCode);
        mockWebServer.enqueue(mockResponse);
    }

    protected void enqueueEmptyResponse(int statusCode) {
        MockResponse mockResponse = new MockResponse().setBody("").setResponseCode(statusCode);
        mockWebServer.enqueue(mockResponse);
    }

    protected RecordedRequest takeLastRequest() throws InterruptedException {
        int requestCount = mockWebServer.getRequestCount();
        while (requestCount > 1) {
            mockWebServer.takeRequest(1, TimeUnit.SECONDS);
            requestCount--;
        }

        return mockWebServer.takeRequest(1, TimeUnit.SECONDS);
    }
}
