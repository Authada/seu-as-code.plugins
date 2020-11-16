import org.gradle.api.JavaVersion.VERSION_1_8

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

plugins {
    groovy
    java
    `java-gradle-plugin`
    `maven-publish`
}

java {
    sourceCompatibility = VERSION_1_8
    targetCompatibility = VERSION_1_8
}

val jnaVersion = "5.6.0"
dependencies {
    implementation(gradleApi())

    implementation("net.java.dev.jna:jna:$jnaVersion")
    implementation("net.java.dev.jna:jna-platform:$jnaVersion")
    implementation("org.apache.commons:commons-lang3:3.11")
    implementation("commons-codec:commons-codec:1.15")
    implementation("commons-io:commons-io:2.8.0")

    testImplementation(localGroovy())
    testImplementation(gradleTestKit())

    testImplementation("org.spockframework:spock-core:1.3-groovy-2.5")
    testImplementation("org.hamcrest:hamcrest-library:2.2")
    testRuntimeOnly("cglib:cglib-nodep:3.3.0")

//        testRuntime 'org.objenesis:objenesis:2.1'
//        testCompile 'com.athaydes:spock-reports:1.2.5'
}

tasks {

    val sourceJarTask = register("sourceJar", Jar::class) {
        archiveClassifier.set("sources")
        project.the<SourceSetContainer>().findByName("main")?.run {
            from(allSource)
        }
    }
    withType(GenerateMavenPom::class.java) {
        mustRunAfter(sourceJarTask)
    }

    val copyTestResourcesTasks = register<Copy>("copyTestResources") {
        from(layout.projectDirectory.dir("src/test/resources"))
        into(layout.buildDirectory.dir("classes/test"))
    }
    named("processTestResources").configure {
        dependsOn(copyTestResourcesTasks)
    }
}

idea {
    module {
        inheritOutputDirs = false
        outputDir = file("$buildDir/classes/java/main")
        testOutputDir = file("$buildDir/classes/java/test")
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}


val nexusUrl: String by project
// set the properties via -P to publish to your company repo
val nexusUsername: String by project
val nexusPassword: String by project
publishing {
    repositories {
        maven {
            url = uri(nexusUrl)
            authentication {
                create<BasicAuthentication>("basic")
            }
            credentials {
                username = nexusUsername
                password = nexusPassword
            }
        }
    }
}

val displayNameValue: String by project
val descriptionValue: String by project
gradlePlugin {
    plugins {
        register("credentialsPlugin") {
            id = "de.qaware.seu.as.code.credentials"
            displayName = displayNameValue
            description = descriptionValue
            implementationClass = "de.qaware.seu.as.code.plugins.credentials.SeuacCredentialsPlugin"
        }
    }
}
