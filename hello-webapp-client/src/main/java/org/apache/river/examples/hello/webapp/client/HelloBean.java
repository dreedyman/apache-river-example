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

package org.apache.river.examples.hello.webapp.client;

import java.util.Scanner;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import net.jini.config.Configuration;
import net.jini.core.entry.Entry;
import net.jini.core.lookup.ServiceItem;
import net.jini.core.lookup.ServiceTemplate;
import net.jini.lookup.ServiceDiscoveryManager;
import net.jini.security.ProxyPreparer;
import org.apache.river.examples.hello.api.Greeter;


/**
 * A Managed Bean that uses a Jini Service.
 * 
 * Note: This code is part of a comprehensive tutorial - Be sure to have a look
 * at the 'README.md' file in the root of the parent 'river-examples' project
 * for instructions on how to build and view the complete tutorial.
 * 
 */
@Named
@RequestScoped
public class HelloBean {

    private static final Logger log = Logger.getLogger(HelloBean.class.getName());
    private static final String MODULE=HelloBean.class.getPackage().getName();

    String name;
    String message="Default";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Inject
    private Configuration config;
   
    public void updateMessage() {
        log.info("Running updateMessage()...");
        log.info("Config is " + config);
        try {
            // From the config, get the ServiceDiscoveryManager
            ServiceDiscoveryManager sdm=
                    (ServiceDiscoveryManager) 
                    config.getEntry(MODULE, "sdm", ServiceDiscoveryManager.class);
            // We'll also need a proxy preparer.
            ProxyPreparer preparer=(ProxyPreparer) config.getEntry(MODULE, 
                    "greeterPreparer", ProxyPreparer.class);
            // Query the sdm for Greeter services.
            ServiceTemplate template=new ServiceTemplate(
                    null,
                    new Class[] { Greeter.class },
                    new Entry[0]
            );
            ServiceItem[] serviceItems=sdm.lookup(template, 5, null);
            if (serviceItems.length==0) {
                message="We didn't find any greeter services.";
                return;
            }
            // Pick a service item
            ServiceItem chosen=serviceItems[0];
            // Prepare the proxy.
            Greeter greeter=(Greeter) preparer.prepareProxy(chosen.service);
            // Make the call
            message=greeter.sayHello(name);
        } catch (Throwable ex) {
            message=ex.getMessage();
            ex.printStackTrace();
        }    
    }
}
