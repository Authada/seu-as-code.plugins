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
package de.qaware.seu.as.code.plugins.credentials.win;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.gradle.api.Project;
import org.gradle.api.initialization.Settings;
import org.gradle.api.invocation.Gradle;

import de.qaware.seu.as.code.plugins.credentials.Credentials;
import de.qaware.seu.as.code.plugins.credentials.CredentialsException;
import de.qaware.seu.as.code.plugins.credentials.CredentialsExtension;
import de.qaware.seu.as.code.plugins.credentials.CredentialsStorage;
import de.qaware.seu.as.code.plugins.credentials.Encryptor;

/**
 * Stores the credentials in a property file.
 *
 * @author phxql
 */
public class PropertyCredentialsStorage implements CredentialsStorage {

    private static final String DEFAULT_FILENAME = "secure-credentials.properties";

    private final Properties properties;
    private final File credentialsFile;
    private final Encryptor encryptor;

    /**
     * Initialize a new property credential storage for the given project.
     *
     * @param project   the current Gradle project used to locate the file
     * @param extension the credentials configuration extension
     */
    public PropertyCredentialsStorage(Project project, CredentialsExtension extension) {
        this(project.getGradle(), extension, new DPAPIEncryptor());
    }

    public PropertyCredentialsStorage(Settings settings, CredentialsExtension extension) {
        this(settings.getGradle(), extension, new DPAPIEncryptor());
    }

    /**
     * Creates a new instance for the given Gradle project, credentials extension and encryptor.
     *
     * @param gradle    a Gradle root object
     * @param extension the credentials configuration extension
     * @param encryptor an encryptor
     */
    PropertyCredentialsStorage(Gradle gradle, CredentialsExtension extension, Encryptor encryptor) {
        this(credentialsFile(gradle, extension), encryptor);
    }

    /**
     * Creates a new instance for a given property file and encryptor.
     *
     * @param credentialsFile Properties file.
     * @param encryptor       Encryptor.
     */
    PropertyCredentialsStorage(File credentialsFile, Encryptor encryptor) {
        this.credentialsFile = credentialsFile;
        this.encryptor = encryptor;

        properties = new Properties();
        if (credentialsFile.exists()) {
            loadProperties(credentialsFile);
        }
    }

    private static File credentialsFile(Gradle gradle, CredentialsExtension extension) {
        String propertyFile = extension.getPropertyFile();
        return (propertyFile != null) ? new File(propertyFile) :
                new File(gradle.getGradleUserHomeDir(), DEFAULT_FILENAME);
    }

    /**
     * Loads the properties from the given file.
     *
     * @param file the file to load from
     */
    private void loadProperties(File file) {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            throw new CredentialsException("Error loading secure credential properties.", e);
        }
    }

    private void save() {
        if (properties.isEmpty()) {
            FileUtils.deleteQuietly(credentialsFile);
        } else {
            try (FileOutputStream fileOutputStream = new FileOutputStream(credentialsFile)) {
                properties.store(fileOutputStream, "SEU-as-code secure credentials");
            } catch (IOException e) {
                throw new CredentialsException("Error storing secure credential properties.", e);
            }
        }
    }

    @Override
    public Credentials findCredentials(String service) {
        String encodedValue = properties.getProperty(service);
        if (encodedValue == null) {
            return null;
        }

        byte[] encryptedValue = Base64.decodeBase64(encodedValue);
        byte[] decryptedValue = encryptor.decrypt(encryptedValue);

        return Credentials.fromSecret(new String(decryptedValue, UTF_8));
    }

    @Override
    public void setCredentials(String service, String username, char[] password) {
        setCredentials(service, new Credentials(username, new String(password)));
    }

    @Override
    public void setCredentials(String service, Credentials credentials) {
        String secret = credentials.toSecret();
        byte[] encryptedValue = encryptor.encrypt(secret.getBytes(UTF_8));
        String encodedValue = Base64.encodeBase64String(encryptedValue);

        // this will set or update the credentials
        properties.setProperty(service, encodedValue);
        save();
    }

    @Override
    public void clearCredentials(String service) {
        properties.remove(service);
        save();
    }
}
