/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.river.examples.hello.service;

import net.jini.config.Configuration;
import net.jini.config.ConfigurationException;
import net.jini.config.ConfigurationProvider;
import net.jini.core.entry.Entry;
import net.jini.core.lookup.ServiceItem;
import net.jini.core.lookup.ServiceTemplate;
import net.jini.discovery.LookupDiscovery;
import net.jini.lookup.ServiceDiscoveryManager;
import net.jini.security.ProxyPreparer;
import org.apache.river.examples.hello.api.Greeter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Tests the GreeterService
 */
public class GreeterServiceTest {
    ProxyPreparer preparer;
    ServiceDiscoveryManager sdm;
    static final String MODULE = GreeterServiceTest.class.getPackage().getName();

    @BeforeClass
    public static void setup() {
        if(System.getSecurityManager()==null)
            System.setSecurityManager(new SecurityManager());
    }

    @Before
    public void setupDiscovery() throws ConfigurationException, IOException {
        Configuration config = ConfigurationProvider.getInstance(new String[]{System.getProperty("client.config")});
        /* From the config, get the groups to discover */
        String[] groups = config.getEntry(MODULE, "groups", String[].class);
        sdm = new ServiceDiscoveryManager(new LookupDiscovery(groups, config), null);
        /* We'll also need a proxy preparer. */
        preparer = config.getEntry(MODULE, "greeterPreparer", ProxyPreparer.class);
    }

    @Test
    public void testGreeterService() throws IOException, InterruptedException {
        ServiceTemplate template = new ServiceTemplate(null,
                                                       new Class[] { Greeter.class },
                                                       new Entry[0]);
        ServiceItem[] serviceItems = sdm.lookup(template, 1, 5, null, TimeUnit.SECONDS.toMillis(5));
        Assert.assertTrue(serviceItems.length>0);

        /* Pick a service item */
        ServiceItem chosen = serviceItems[0];

        /* Prepare the proxy. */
        Greeter greeter = (Greeter)preparer.prepareProxy(chosen.service);

        /* Make the call */
        String name = "Itsa me, Mario";
        String expectedResult = String .format("Hi there %s", name);
        String message = greeter.sayHello(name);
        Assert.assertTrue(expectedResult.equals(message));
    }
}
