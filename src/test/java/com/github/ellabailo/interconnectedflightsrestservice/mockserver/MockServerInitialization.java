package com.github.ellabailo.interconnectedflightsrestservice.mockserver;

import org.mockserver.client.MockServerClient;
import org.mockserver.client.initialize.PluginExpectationInitializer;

public class MockServerInitialization implements PluginExpectationInitializer {
    @Override
    public void initializeExpectations(MockServerClient mockServerClient) {

        MockServerExpectationInitializer initializer = new MockServerExpectationInitializer(mockServerClient);
        initializer.initializeForIntegrationTest();
    }
}
