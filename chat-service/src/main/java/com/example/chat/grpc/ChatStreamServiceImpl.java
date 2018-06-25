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

package com.example.chat.grpc;

import com.example.chat.ChatMessage;
import com.example.chat.ChatMessageFromServer;
import com.example.chat.ChatStreamServiceGrpc;
import com.example.chat.Room;
import com.example.chat.repository.ChatRoomRepository;
import com.google.protobuf.Timestamp;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by rayt on 6/27/17.
 */
public class ChatStreamServiceImpl extends ChatStreamServiceGrpc.ChatStreamServiceImplBase {
  private static final Logger logger = Logger.getLogger(ChatStreamServiceImpl.class.getName());
  private final ChatRoomRepository repository;
  //A mapping of room name to the set of stream observers for a room
  private Map<String, Set<StreamObserver<ChatMessageFromServer>>> roomObservers = new ConcurrentHashMap<>();

  public ChatStreamServiceImpl(ChatRoomRepository repository) {
    this.repository = repository;
  }

  protected <T> boolean failedBecauseRoomNotFound(String roomName, StreamObserver<T> responseObserver) {
    Room room = repository.findRoom(roomName);
    if (room == null) {
      responseObserver.onError(new StatusRuntimeException(Status.NOT_FOUND.withDescription("Room not found: " + roomName)));
      return true;
    }
    return false;
  }

  protected Set<StreamObserver<ChatMessageFromServer>> getRoomObservers(String room) {
    roomObservers.putIfAbsent(room, Collections.newSetFromMap(new ConcurrentHashMap<>()));
    return roomObservers.get(room);
  }

  protected void removeObserverFromAllRooms(StreamObserver<ChatMessageFromServer> responseObserver) {
    roomObservers.entrySet().stream().forEach(e -> {
      e.getValue().remove(responseObserver);
    });
  }

  @Override
  public StreamObserver<ChatMessage> chat(StreamObserver<ChatMessageFromServer> responseObserver) {
    // Not implemented until metadata and interceptors exercise
    //final String username = Constant.USER_ID_CTX_KEY.get();

    return new StreamObserver<ChatMessage>() {
      @Override
      public void onNext(ChatMessage chatMessage) {

      }

      @Override
      public void onError(Throwable throwable) {

      }

      @Override
      public void onCompleted() {

      }
    };
  }
}
