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

package com.example.auth.grpc;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.auth.*;
import com.example.auth.domain.User;
import com.example.auth.repository.UserRepository;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

/**
 * Created by rayt on 6/27/17.
 */
// TODO Extend gRPC's AuthenticationServiceBaseImpl
public class AuthServiceImpl extends AuthenticationServiceGrpc.AuthenticationServiceImplBase {
  private final UserRepository repository;
  private final String issuer;
  private final Algorithm algorithm;
  private final JWTVerifier verifier;

  public AuthServiceImpl(UserRepository repository, String issuer, Algorithm algorithm) {
    this.repository = repository;
    this.issuer = issuer;
    this.algorithm = algorithm;
    this.verifier = JWT.require(algorithm)
        .withIssuer(issuer)
        .build();
  }

  protected String generateToken(String username) {
    return JWT.create()
        .withIssuer(issuer)
        .withSubject(username)
        .sign(algorithm);
  }

  protected DecodedJWT jwtFromToken(String token) {
    return verifier.verify(token);
  }

  // TODO Override authenticate methods
  @Override
  public void authenticate(AuthenticationRequest request, StreamObserver<AuthenticationResponse> responseObserver) {
    User user = repository.findUser(request.getUsername());

    if (user == null || !user.getPassword().equals(request.getPassword())) {
      responseObserver.onError(Status.UNAUTHENTICATED.asRuntimeException());
      return;
    }
    String token = generateToken(request.getUsername());
    responseObserver.onNext(AuthenticationResponse.newBuilder()
        .setToken(token)
        .build());
    responseObserver.onCompleted();
  }

  // TODO Override authorization method
  public void authorization(AuthorizationRequest request, StreamObserver<AuthorizationResponse> responseObserver) {
    try {
      DecodedJWT jwt = jwtFromToken(request.getToken());

      String username = jwt.getSubject();
      User user = repository.findUser(username);

      if (user == null) {
        // send error
        return;
      }

      responseObserver.onNext(AuthorizationResponse.newBuilder()
          .addAllRoles(user.getRoles())
          .build());
      responseObserver.onCompleted();

    } catch (JWTVerificationException e) {
      responseObserver.onError(Status.UNAUTHENTICATED.asRuntimeException());
      return;
    }
  }
}
