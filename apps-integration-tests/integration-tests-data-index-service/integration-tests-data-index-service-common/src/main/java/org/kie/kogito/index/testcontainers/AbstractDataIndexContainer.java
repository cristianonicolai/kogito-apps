/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.kogito.index.testcontainers;

import org.kie.kogito.resources.TestResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;

/**
 * This container wraps Data Index Service container
 */
public abstract class AbstractDataIndexContainer extends GenericContainer<AbstractDataIndexContainer> implements TestResource {

    public static final int PORT = 8180;
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDataIndexContainer.class);

    public AbstractDataIndexContainer() {
        addExposedPort(PORT);
        withLogConsumer(new Slf4jLogConsumer(LOGGER));
        waitingFor(Wait.forListeningPort());
        withClasspathResourceMapping("approvals.proto", "/home/kogito/data/protobufs/approvals.proto", BindMode.READ_ONLY);
        addEnv("KOGITO_PROTOBUF_FOLDER", "/home/kogito/data/protobufs/");
        setDockerImageName(getImageName());
    }

    public void setKafkaURL(String kafkaURL) {
        addEnv("KAFKA_BOOTSTRAP_SERVERS", kafkaURL);
    }

    @Override
    public int getMappedPort() {
        return getMappedPort(PORT);
    }

    protected abstract String getImageName();
}
