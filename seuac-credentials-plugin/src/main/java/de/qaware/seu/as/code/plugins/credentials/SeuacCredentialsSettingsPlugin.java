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

import org.gradle.api.Plugin;
import org.gradle.api.initialization.Settings;

/**
 * SEU-as-code Credentials plugin.
 * <p/>
 * Provides the 'credentials' object to get credentials and the 'setStorage' task to set credentials.
 *
 * @author phxql
 */
public class SeuacCredentialsSettingsPlugin implements Plugin<Settings> {

    @Override
    public void apply(final Settings settings) {
        CredentialsStorageFactory factory = new CredentialsStorageFactory.SettingsCredentialsStorageFactory(settings);

        // register the credentials configuration with the project
        CredentialsExtension extension = new CredentialsExtension();
        settings.getExtensions().add(CredentialsExtension.NAME, extension);

        CredentialsProperty property = new CredentialsProperty(factory);
        settings.getExtensions().getExtraProperties().set(CredentialsProperty.NAME, property);
    }

}
