package io.mosn.layotto.examples.pubsub.publisher;

import io.mosn.layotto.v1.RuntimeClientBuilder;
import spec.sdk.runtime.v1.client.RuntimeClient;

public class Publisher {

    public static void main(String[] args) {
        RuntimeClient client = new RuntimeClientBuilder().withPort(34904).build();
        client.publishEvent("redis", "hello", "world".getBytes());
        client.publishEvent("redis", "topic1", "message1".getBytes());

    }
}
