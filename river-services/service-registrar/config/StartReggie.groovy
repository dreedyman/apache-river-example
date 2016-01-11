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
 * Configuration to start the Service Registrar service
 */
@Component("org.apache.river.start")
class StartReggie {
    

    ServiceDescriptor[] getServiceDescriptors() {
        String policy = System.getProperty("java.security.policy")
        String riverVersion = System.getProperty("river.version")

        String projectDir = System.getProperty("project.dir")
        String configPath = "${projectDir}/config"
        String localRepo = "${System.getProperty("user.home")}/.m2/repository"
        String reggieDl = "org/apache/river/reggie-dl/${riverVersion}/reggie-dl-${riverVersion}.jar"
        String jskDl = "net/jini/jsk-dl/${riverVersion}/jsk-dl-${riverVersion}.jar"
        String port="8090"

        def descriptors = []
        def reggieConfigArg = ["${configPath}/ReggieConfig.groovy"]
        def codebase = "http://${InetAddress.localHost.hostAddress}:$port"
        def classServerArgs = ["-port", port,
                               "-dir", "${localRepo}",
                               "-verbose"]

        descriptors << new NonActivatableServiceDescriptor("",
                                                           policy,
                                                           "${localRepo}/org/apache/river/tools/${riverVersion}/tools-${riverVersion}.jar",
                                                           "org.apache.river.tool.ClassServer",
                                                           classServerArgs as String[])

        descriptors << new NonActivatableServiceDescriptor("$codebase/$reggieDl $codebase/$jskDl",
                                                           policy,
                                                           "${localRepo}/org/apache/river/reggie/${riverVersion}/reggie-${riverVersion}.jar:${localRepo}/net/jini/jsk-lib/${riverVersion}/jsk-lib-${riverVersion}.jar",
                                                           "org.apache.river.reggie.TransientRegistrarImpl",
                                                           reggieConfigArg as String[])

        return descriptors as ServiceDescriptor[]
    }
}
