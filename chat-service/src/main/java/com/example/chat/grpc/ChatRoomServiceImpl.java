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

import com.example.auth.AuthenticationServiceGrpc;
import com.example.chat.ChatRoomServiceGrpc;
import com.example.chat.Empty;
import com.example.chat.Room;
import com.example.chat.repository.ChatRoomRepository;
import io.grpc.stub.StreamObserver;

/**
 * Created by rayt on 6/27/17.
 */
public class ChatRoomServiceImpl extends ChatRoomServiceGrpc.ChatRoomServiceImplBase {
  private final ChatRoomRepository repository;
  private final AuthenticationServiceGrpc.AuthenticationServiceBlockingStub authService;

  public ChatRoomServiceImpl(ChatRoomRepository repository, AuthenticationServiceGrpc.AuthenticationServiceBlockingStub authService) {
    this.repository = repository;
    this.authService = authService;
  }

  protected <T> boolean failBecauseNoAdminRole(StreamObserver<T> responseObserver) {
    // TODO Retrieve JWT from Constant.JWT_CTX_KEY

    // TODO Retrieve the roles

    // TODO If not in the admin role, return Status.PERMISSION_DENIED

    return false;

  }

  @Override
  public void createRoom(Room request, StreamObserver<Room> responseObserver) {
    if (failBecauseNoAdminRole(responseObserver)) {
      return;
    }

    Room room = repository.save(request);
    responseObserver.onNext(room);
    responseObserver.onCompleted();
  }

  @Override
  public void deleteRoom(Room request, StreamObserver<Room> responseObserver) {
    if (failBecauseNoAdminRole(responseObserver)) {
      return;
    }

    Room room = repository.delete(request);
    responseObserver.onNext(room);
    responseObserver.onCompleted();
  }

  @Override
  public void getRooms(Empty request, StreamObserver<Room> responseObserver) {
    repository.getRooms().forEach(responseObserver::onNext);
    responseObserver.onCompleted();
  }
}
