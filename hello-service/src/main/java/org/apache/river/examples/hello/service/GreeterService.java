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
import org.apache.river.api.util.Startable;
import org.apache.river.examples.builder.Service;
import org.apache.river.examples.hello.api.Greeter;
import org.apache.river.examples.hello.api.GreeterAdmin;
import org.apache.river.start.LifeCycle;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.logging.Logger;

import static org.apache.river.examples.builder.ServiceBuilder.service;

/**
 *
 * Implementation of the Greeter interface, suitable for starting under the
 * ServiceStarter framework.
 * 
 * Note: This code is part of a comprehensive tutorial - Be sure to have a look
 * at the 'README.md' file in the root of the parent 'river-examples' project
 * for instructions on how to build and view the complete tutorial.
 * 
 */
public class GreeterService implements Greeter, Startable {
    private static final String GREETER = "org.apache.river.examples.hello.greeter";
    private final String[] args;
    private final LifeCycle lifeCycle;
    private Service service;
    private GreeterAdmin greeterAdmin;
    private static final Logger logger = Logger.getLogger(GreeterService.class.getName());

    public GreeterService(final String[] args, final LifeCycle lifeCycle) throws ConfigurationException, IOException {
        this.args = args;
        this.lifeCycle = lifeCycle;
    }

    @Override public void start() throws Exception {
        logger.info("Hello service starting...");
        Configuration config = ConfigurationProvider.getInstance(args);

        /*
         * Use a builder pattern to assemble thr service's attributes, groups,
         * exporter and discovery management. Then export the service and join the network
         */
        service = service(config).usingComponent(GREETER)
                      .attributes()
                      .groups()
                      .exporter()
                      .discoveryManagement()
                      .build().exportAndJoin(this);

        /* Create the Greeter administration object */
        greeterAdmin = new GreeterAdmin((Greeter)service.getProxy());

        /* Register the administration object as an MBean */
        service.registerMBean(greeterAdmin);

        logger.info("Hello service has been started successfully.");
    }

    public String sayHello(String name) {
        return "Hi there " + name;
    }

    @Override public void destroy() {
        service.getJoinManager().terminate();
        service.getExporter().unexport(true);
        service.unregisterMBean(greeterAdmin);
        lifeCycle.unregister(this);
    }

    @Override public Object getAdmin() throws RemoteException {
        return greeterAdmin;
    }
}
