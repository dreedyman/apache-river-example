<!--
 Licensed to the Apache Software Foundation (ASF) under one
 or more contributor license agreements.  See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership. The ASF licenses this file
 to you under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License. You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

-->

Apache River Example - README
=====================

This project contains an example that shows how to use Apache River.

## Building the example

The project uses Gradle to manage the dependencies and build the examples.
There is no need to download and build the main River distribution or tools;
the River artifacts are deployed to Maven Central, Gradle will automatically
download the binary artifacts as needed for the examples build.

To build the examples, simply run:

    cd apache-river-example
    gradlew build

This will build the project and also run the integrated tests.

## The Hello Service Example

In a nutshell, a River service project should be built as multi-module project, that reflects the basic
architectural elements of a distributed service. Given the service name of hello, the service project
is composed of the following modules:

* hello-api
The hello-api module contains all the classes (interfaces and other classes) that is needed to communicate with the service

* hello-service
The hello-service module, depends on the hello-api module, and provides the backend service implementation.


## Service Browser

This is a utility that allows you to browse the services that are operating in your workgroup, or ‘djinn’. The  Service Browser
can be started by changing into the river-services/browser, and running gradlew browser



