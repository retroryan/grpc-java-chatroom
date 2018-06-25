package com.example.auth;

import java.util.Optional;

public class EnvVars {

    public static final String DATABASE_PATH = Optional.ofNullable(System.getenv("DB_PATH")).orElse(".");

    public static final String AUTH_SERVICE_HOST = Optional.ofNullable(System.getenv("AUTH_SERVICE_HOST")).orElse("localhost");
    public static final int AUTH_SERVICE_PORT = Optional.ofNullable(System.getenv("AUTH_SERVICE_PORT")).map(Integer::parseInt).orElse(9091);
    public static final String AUTH_SERVICE_URL = AUTH_SERVICE_HOST + ":" + AUTH_SERVICE_PORT;

    public static final String CHAT_SERVICE_HOST = Optional.ofNullable(System.getenv("CHAT_SERVICE_HOST")).orElse("localhost");
    public static final int CHAT_SERVICE_PORT = Optional.ofNullable(System.getenv("CHAT_SERVICE_PORT")).map(Integer::parseInt).orElse(9092);
    public static final String CHAT_SERVICE_URL = CHAT_SERVICE_HOST + ":" + CHAT_SERVICE_PORT;

    public static final String ZIPKIN_HOST = Optional.ofNullable(System.getenv("ZIPKIN_HOST")).orElse("http://localhost");
    public static final int ZIPKIN_PORT = Optional.ofNullable(System.getenv("ZIPKIN_PORT")).map(Integer::parseInt).orElse(9411);
    public static final String ZIPKIN_URL = ZIPKIN_HOST + ":" + ZIPKIN_PORT + "/api/v1/spans";

}
