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
package de.qaware.seu.as.code.plugins.credentials

import org.gradle.testfixtures.ProjectBuilder
import org.junit.Ignore
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

import static org.hamcrest.Matchers.notNullValue
import static spock.util.matcher.HamcrestSupport.expect

/**
 * Basic test specification for the {@link SeuacCredentialsPlugin}.
 *
 * @author lreimer
 */
@Ignore
class SeuacCredentialsPluginSpec extends Specification {

    @Rule
    public final TemporaryFolder testProjectDir = new TemporaryFolder()
    private File settingsFile
    private File buildFile

    def setup() {
        settingsFile = testProjectDir.newFile("settings.gradle")
        buildFile = testProjectDir.newFile("build.gradle")
    }

    def "Apply the plugin"() {
        given:
        settingsFile << "rootProject.name = 'apply-test'"
        buildFile << """
            plugins {
                id 'de.qaware.seu.as.code.credentials'
            }
        """

        when: "the plugin is applied"
        def project = ProjectBuilder.builder()
                .withProjectDir(testProjectDir.root)
                .build()

        then: "we expect to find the tasks and the extension to be configured"
        SetCredentialsTask setCredentialsTask = (SetCredentialsTask) project.tasks.findByName('setCredentials')
        ClearCredentialsTask clearCredentialsTask = (ClearCredentialsTask) project.tasks.findByName('clearCredentials')
        DisplayCredentialsTask displayCredentialsTask = (DisplayCredentialsTask) project.tasks.findByName('displayCredentials')

        def extension = project.getExtensions().getExtraProperties().get('credentials')

        expect setCredentialsTask, notNullValue()
        expect setCredentialsTask.getConsole(), notNullValue()
        expect setCredentialsTask.getStorage(), notNullValue()

        expect clearCredentialsTask, notNullValue()
        expect clearCredentialsTask.getConsole(), notNullValue()
        expect clearCredentialsTask.getStorage(), notNullValue()

        expect displayCredentialsTask, notNullValue()
        expect displayCredentialsTask.getConsole(), notNullValue()
        expect displayCredentialsTask.getStorage(), notNullValue()

        expect extension, notNullValue()
    }

}
