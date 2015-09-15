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
import net.jini.core.entry.Entry;
import net.jini.core.lookup.ServiceID;
import net.jini.discovery.DiscoveryManagement;
import net.jini.discovery.LookupDiscovery;
import net.jini.export.Exporter;
import net.jini.id.Uuid;
import net.jini.id.UuidFactory;
import net.jini.lease.LeaseRenewalManager;
import net.jini.lookup.JoinManager;
import org.apache.river.config.Config;

import java.io.IOException;
import java.rmi.Remote;

/**
 * @author Dennis Reedy
 */
public class Service {
    private Configuration configuration;
    private String[] groups;
    private Entry[] attributes;
    private DiscoveryManagement discoveryManagement;
    private Object proxy;
    private Exporter exporter;
    private JoinManager joinManager;

    public Entry[] getAttributes() {
        return attributes;
    }

    public void setAttributes(String component, String entry) throws ConfigurationException {
        this.attributes = configuration.getEntry(component, entry, Entry[].class);
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public DiscoveryManagement getDiscoveryManagement() {
        return discoveryManagement;
    }

    public void setDiscoveryManagement() throws IOException, ConfigurationException {
        this.discoveryManagement = new LookupDiscovery(groups, configuration);
    }

    public Exporter getExporter() {
        return exporter;
    }

    public void setExporter(String component, String entry) throws ConfigurationException {
        this.exporter = Config.getNonNullEntry(configuration, component, entry, Exporter.class);
    }

    public Object getProxy() {
        return proxy;
    }

    public void setProxy(Object proxy) {
        this.proxy = proxy;
    }

    public String[] getGroups() {
        return groups;
    }

    public void setGroups(String component, String entry) throws ConfigurationException {
        groups = configuration.getEntry(component, entry, String[].class);
    }

    public Service exportAndJoin(Remote remote) throws IOException, ConfigurationException {
        proxy = exporter.export(remote);
        Uuid myUuid = UuidFactory.generate();
        ServiceID myServiceId = new ServiceID(myUuid.getMostSignificantBits(), myUuid.getLeastSignificantBits());
        joinManager = new JoinManager(proxy, attributes, myServiceId, discoveryManagement, new LeaseRenewalManager(), configuration);
        return this;
    }

    public JoinManager getJoinManager() {
        return joinManager;
    }
}
