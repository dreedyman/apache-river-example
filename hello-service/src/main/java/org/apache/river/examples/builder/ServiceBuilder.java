/*
 * Copyright to the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.river.examples.builder;

import net.jini.config.Configuration;
import net.jini.config.ConfigurationException;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.server.ExportException;

/**
 * @author Dennis Reedy
 */
public class ServiceBuilder {
    private String component;
    private static Service service;

    private ServiceBuilder() {
        service = new Service();
    }

    public static ServiceBuilder service(Configuration config) {
        ServiceBuilder serviceBuilder = new ServiceBuilder();
        service.setConfiguration(config);
        return serviceBuilder;
    }

    public ServiceBuilder usingComponent(String component) throws ConfigurationException {
        this.component = component;
        return this;
    }

    public ServiceBuilder attributes(String entry) throws ConfigurationException {
        return attributes(component, entry);
    }

    public ServiceBuilder attributes(String component, String entry) throws ConfigurationException {
        service.setAttributes(component, entry);
        return this;
    }

    public ServiceBuilder groups(String entry) throws ConfigurationException {
        return groups(component, entry);
    }

    public ServiceBuilder groups(String component, String entry) throws ConfigurationException {
        service.setGroups(component, entry);
        return this;
    }

    public ServiceBuilder exporter(String entry) throws ConfigurationException {
        return exporter(component, entry);
    }

    public ServiceBuilder exporter(String component, String entry) throws ConfigurationException {
        service.setExporter(component, entry);
        return this;
    }

    public ServiceBuilder discoveryManagement() throws ConfigurationException, IOException {
        service.setDiscoveryManagement();
        return this;
    }

    public ServiceBuilder export(Remote remote) throws ExportException {
        service.setProxy(service.getExporter().export(remote));
        return this;
    }

    public Service build() {
        return service;
    }
}
