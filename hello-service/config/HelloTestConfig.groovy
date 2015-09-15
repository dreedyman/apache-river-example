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




import net.jini.config.Component
import net.jini.constraint.BasicMethodConstraints
import net.jini.core.constraint.InvocationConstraints
import net.jini.export.Exporter
import net.jini.jeri.BasicILFactory
import net.jini.jeri.BasicJeriExporter
import net.jini.jeri.tcp.TcpServerEndpoint
import net.jini.security.BasicProxyPreparer
/**
 * Configuration for the Hello service test client
 */
@Component("org.apache.river.examples.hello.service")
class HelloTestConfig {

    Exporter exporter = new BasicJeriExporter(TcpServerEndpoint.getInstance(0),
                                              new BasicILFactory())

    def getGroups() {
        def groups = [System.getProperty("user.name")]
        return groups as String[]
    }

    def getProxyPreparer() {
        InvocationConstraints serviceInvocationConstraints = InvocationConstraints.EMPTY;
        return new BasicProxyPreparer(false,
                                      new BasicMethodConstraints(serviceInvocationConstraints),
                                      null)
    }

    def getGreeterPreparer() {
        return getProxyPreparer()
    }

}

