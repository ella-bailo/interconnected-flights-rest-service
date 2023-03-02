// package com.github.ellabailo.interconnectedflightsrestservice.client;

// import org.junit.jupiter.api.AfterEach;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockserver.integration.ClientAndServer;
// import org.springframework.web.reactive.function.client.WebClient;

// import com.fasterxml.jackson.core.JsonProcessingException;

// public class RyanairScheduleClient {
//     private ClientAndServer mockServer;
//     private RyanairSchedulesClient ryanairSchedulesClient;

//     @BeforeEach
//     public void setupMockServer() {
//         mockServer = ClientAndServer.startClientAndServer();
//         ryanairSchedulesClient = new RyanairSchedulesClient();
//     }

//     @AfterEach
//     public void tearDownMockerver() {
//         mockServer.stop();
//     }

//     @Test
//     void invokesApi() throws JsonProcessingException {
//         mockServer.enqueue()
//     }


