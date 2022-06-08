package de.planqk.s1.artifacttemplates.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.nio.file.Files;

public class FileHandler {

    protected static File downloadFile(final String url) {
        try {
            final URI dockerImageURI = new URI(url);

            final String[] pathSplit = dockerImageURI.getRawPath().split("/");
            final String fileName = pathSplit[pathSplit.length - 1];

            final File tempDir = Files.createTempDirectory("planqk-service-").toFile();
            final File tempFile = new File(tempDir, fileName);

            downloadFile(dockerImageURI, tempFile);
            return tempFile;
        } catch (final URISyntaxException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected static void downloadFile(URI dockerImageURI, File tempFile) throws IOException {
        final URLConnection connection = dockerImageURI.toURL().openConnection();
        connection.setRequestProperty("Accept", "application/octet-stream");

        try (final InputStream input = connection.getInputStream()) {
            final byte[] buffer = new byte[4096];
            int n;

            try (final OutputStream output = new FileOutputStream(tempFile)) {
                while ((n = input.read(buffer)) != -1) {
                    output.write(buffer, 0, n);
                }
            }
        }
    }
}
