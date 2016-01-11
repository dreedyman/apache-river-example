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
import org.apache.river.start.NonActivatableServiceDescriptor
import org.apache.river.start.ServiceDescriptor

/**
 * Configuration to start the Hello service
 */
@Component("org.apache.river.start")
class StartHelloService {
    

    ServiceDescriptor[] getServiceDescriptors() {
        String policy = System.getProperty("java.security.policy")

        String projectDir = System.getProperty("project.dir")
        String configPath = "${projectDir}/config"
        String version = System.getProperty("project.version")
        String riverVersion = System.getProperty("river.version")
        String apiPath = "org/apache/river/examples/hello-api/${version}/hello-api-${version}.jar"
        String jskDl = "net/jini/jsk-dl/${riverVersion}/jsk-dl-${riverVersion}.jar"
        String port="8090"

        def descriptors = []
        def configArg = ["${configPath}/HelloServiceConfig.groovy"]
        def codebase = "http://${InetAddress.localHost.hostAddress}:$port"

        descriptors << new NonActivatableServiceDescriptor("$codebase/$apiPath $codebase/$jskDl",
                                                           policy,
                                                           "${System.getProperty("project.classpath")}",
                                                           "org.apache.river.examples.hello.service.GreeterService",
                                                           configArg as String[])
        return descriptors as ServiceDescriptor[]
    }
}