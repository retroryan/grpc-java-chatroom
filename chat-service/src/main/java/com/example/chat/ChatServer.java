/*
 * Copyright 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.chat;

import com.auth0.jwt.algorithms.Algorithm;
import com.example.auth.AuthenticationServiceGrpc;
import com.example.chat.grpc.ChatRoomServiceImpl;
import com.example.chat.grpc.ChatStreamServiceImpl;
import com.example.chat.grpc.JwtServerInterceptor;
import com.example.chat.repository.ChatRoomRepository;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by rayt on 6/27/17.
 */
public class ChatServer {
  private static final Logger logger = Logger.getLogger(ChatServer.class.getName());

  public static void main(String[] args) throws IOException, InterruptedException {
    final ChatRoomRepository repository = new ChatRoomRepository();
    final JwtServerInterceptor jwtServerInterceptor = new JwtServerInterceptor("auth-issuer", Algorithm.HMAC256("secret"));

    // TODO Initial tracer

    // TODO Add trace interceptor
    final ManagedChannel authChannel = ManagedChannelBuilder.forTarget("localhost:9091")
        .usePlaintext(true)
        .build();

    final AuthenticationServiceGrpc.AuthenticationServiceBlockingStub authService = AuthenticationServiceGrpc.newBlockingStub(authChannel);
    final ChatRoomServiceImpl chatRoomService = new ChatRoomServiceImpl(repository, authService);
    final ChatStreamServiceImpl chatStreamService = new ChatStreamServiceImpl(repository);

    // TODO Add JWT Server Interceptor, then later, trace interceptor
    final Server server = ServerBuilder.forPort(9092)
        .addService(chatRoomService)
        .addService(chatStreamService)
        .build();

    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        server.shutdownNow();
        authChannel.shutdownNow();
      }
    });

    server.start();
    logger.info("Server Started on port 9092");
    server.awaitTermination();
  }
}
