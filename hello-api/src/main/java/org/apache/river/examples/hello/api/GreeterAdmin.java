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
package org.apache.river.examples.hello.api;

import org.apache.river.admin.DestroyAdmin;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Dennis Reedy
 */
public class GreeterAdmin implements GreeterAdminMBean, DestroyAdmin, Serializable {
    static final long serialVersionUID = 1l;
    private final Greeter greeter;
    private static final Logger logger = Logger.getLogger(GreeterAdmin.class.getName());

    public GreeterAdmin(Greeter greeter) {
        this.greeter = greeter;
    }

    @Override public void destroy() {
        try {
            greeter.destroy();
        } catch (RemoteException e) {
            logger.log(Level.SEVERE, "problem trying to destroy Greeter", e);
        }
    }

    public ObjectName getObjectName() throws MalformedObjectNameException {
        String domain = "org.apache.river.examples";
        String projectId;
        if(System.getProperty("project.id")!=null)
            projectId = String.format(",projectId=%s", System.getProperty("project.id"));
        else
            projectId="";
        String objectName = String.format("%s:name=Greeter%s", domain, projectId);
        return ObjectName.getInstance(objectName);
    }
}
