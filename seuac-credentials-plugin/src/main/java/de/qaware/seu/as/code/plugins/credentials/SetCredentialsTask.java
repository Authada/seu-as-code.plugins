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
package de.qaware.seu.as.code.plugins.credentials;

import org.gradle.api.tasks.TaskAction;

/**
 * Task to set the credentials for a given service.
 * <p>
 * Invoke with 'gradle setCredentials --service [name of service]' or alternatively with
 * 'gradle setCredentials --service [name of service] --username [the username]'
 * <p/>
 * The task will query the username and password on the console to input the value of the credentials.
 *
 * @author phxql
 */
public class SetCredentialsTask extends AbstractCredentialsTask {

    /**
     * Constructor initializing the tasks meta data.
     */
    public SetCredentialsTask() {
        this.setDescription("Sets the credentials.");
    }

    /**
     * Is executed from gradle when running the 'setStorage' task.
     */
    @TaskAction
    public void onAction() {
        try (final SystemConsole console = new SystemConsole()) {
            setConsole(console);
            getConsole().format("%n");
            getStorage().setCredentials(getService(), getUsername(), getPassword());
        }
    }
}
