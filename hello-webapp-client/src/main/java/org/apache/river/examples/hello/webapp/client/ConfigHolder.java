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

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.inject.Produces;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import net.jini.config.Configuration;
import net.jini.config.ConfigurationFile;
import net.jini.lookup.ServiceDiscoveryManager;

/**
 * This class acts as a ServletContextListener to load the configuration on 
 * startup of the application, and then acts as a CDI resource provider to allow
 * automatic injection of the Configuration to any other CDI components.
 * 
 * Note: This code is part of a comprehensive tutorial - Be sure to have a look
 * at the 'README.md' file in the root of the parent 'river-examples' project
 * for instructions on how to build and view the complete tutorial.
 * 
 * 
 */
@WebListener
public class ConfigHolder implements ServletContextListener {

    private static Logger log = Logger.getLogger(ConfigHolder.class.getName());
    
    private static Configuration config;
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext sc = sce.getServletContext();
        log.info("Got the servlet context in contextInitialized(...):" + sc);
        log.info("This is " + this);
        try {
            // Load the configuration
            log.info("servletContext=" + sc);
            Object configResourceUrl
                    = sc.getResource("/WEB-INF/client.config");
            String configResource = configResourceUrl.toString();

            log.info("Attempting to open config at "
                    + configResource);
            config
                    = new ConfigurationFile(new String[]{configResource});
            log.info("Got configuration.");
            /* Get the ServiceDiscoveryManager.  This call effectively causes discovery to 
            begin at application startup. 
            */
            config.getEntry(this.getClass().getPackage().getName(), 
                    "sdm", ServiceDiscoveryManager.class);
        } catch (Throwable t) {
            log.log(Level.SEVERE, "Got a throwable on startup", t);
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

    @Produces Configuration getConfig() {
        return config;
    }
}
