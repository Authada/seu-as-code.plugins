/*
 *    Copyright (C) 2015 QAware GmbH
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

//plugins {
//    id 'java-gradle-plugin'
//}
//dependencies {
//    compile 'net.java.dev.jna:jna:4.2.0'
//    compile 'net.java.dev.jna:jna-platform:4.2.0'
//
//    compile 'org.apache.commons:commons-lang3:3.4'
//    compile 'commons-codec:commons-codec:1.10'
//    compile 'commons-io:commons-io:2.4'
//}
//
//sourceCompatibility = JavaVersion.VERSION_1_8
//targetCompatibility = JavaVersion.VERSION_1_8
//
//idea {
//    module {
//        inheritOutputDirs = false
//        outputDir = file("$buildDir/classes/java/main")
//        testOutputDir = file("$buildDir/classes/java/test")
//        downloadJavadoc = true
//        downloadSources = true
//    }
//}
//
//task copyTestResources(type: Copy) {
//    from "${projectDir}/src/test/resources"
//    into "${buildDir}/classes/test"
//}
//processTestResources.dependsOn copyTestResources
//
//task javadocJar(type: Jar, dependsOn: javadoc) {
//    classifier = 'javadoc'
//    from javadoc.destinationDir
//}
//
//artifacts {
//    archives sourcesJar
//    archives javadocJar
//}
//
//def pomConfig = {
//
//    inceptionYear '2015'
//
//    scm {
//        connection "scm:git:${project.scmUrl}"
//        developerConnection "scm:git:${project.scmUrl}"
//        url project.websiteUrl
//    }
//
//    issueManagement {
//        system 'GitHub'
//        url project.issueTrackerUrl
//    }
//
//    licenses {
//        license([:]) {
//            name 'The Apache Software License, Version 2.0'
//            url 'http://www.apache.org/licenses/LICENSE-2.0.txt'
//            distribution 'repo'
//        }
//    }
//
//    organisation {
//        name 'QAware GmbH'
//        url 'https://www.qaware.de'
//    }
//
//    developers {
//        developer {
//            id 'lreimer'
//            name 'Mario-Leander Reimer'
//            email 'mario-leander.reimer@qaware.de'
//            organization 'QAware GmbH'
//            organizationUrl 'https://www.qaware.de'
//            roles { role 'Developer' }
//        }
//        developer {
//            id 'phxql'
//            name 'Moritz Kammerer'
//            email 'moritz.kammerer@qaware.de'
//            organization 'QAware GmbH'
//            organizationUrl 'https://www.qaware.de'
//            roles { role 'Developer' }
//        }
//        developer {
//            id 'clboettcher'
//            name 'Claudius Boettcher'
//            email 'claudius.boettcher@qaware.de'
//            organization 'QAware GmbH'
//            organizationUrl 'https://www.qaware.de'
//            roles { role 'Developer' }
//        }
//    }
//}
//
//publishing {
//    repositories {
//        // set the properties via -P to publish to your company repo
//        maven {
//            url = project.hasProperty('nexusUrl') ? project.nexusUrl : ''
//            authentication {
//                basic(BasicAuthentication)
//            }
//            credentials {
//                username = project.hasProperty('nexusUsername') ? project.nexusUsername : ''
//                password = project.hasProperty('nexusPassword') ? project.nexusPassword : ''
//            }
//        }
//    }
//}
//
//gradlePlugin {
//    plugins {
//        credentialsPlugin {
//            id = 'de.qaware.seu.as.code.credentials'
//            displayName = project.displayName
//            description = project.description
//            implementationClass = 'de.qaware.seu.as.code.plugins.credentials.SeuacCredentialsPlugin'
//        }
//    }
//}
