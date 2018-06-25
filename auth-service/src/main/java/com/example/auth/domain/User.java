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

package com.example.auth.domain;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by rayt on 6/27/17.
 */
public class User {
  private String username;
  private String password;

  private Set<String> roles = new LinkedHashSet<>();

  public User() {

  }

  public User(User original) {
    this.username = original.username;
    this.password = original.password;
    this.roles.addAll(original.roles);
  }

  public User(String username, String password, Set<String> roles) {
    this.username = username;
    this.password = password;
    this.roles = roles;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Set<String> getRoles() {
    return roles;
  }

  public void setRoles(Set<String> roles) {
    this.roles = roles;
  }

  public void addRole(String role) {
    this.roles.add(role);
  }

  public void removeRole(String role) {
    this.roles.remove(role);
  }

  @Override
  public String toString() {
    String rolesStr = roles.stream().collect(Collectors.joining(", "));
    return username + "," + password + "," + rolesStr;
  }
}
