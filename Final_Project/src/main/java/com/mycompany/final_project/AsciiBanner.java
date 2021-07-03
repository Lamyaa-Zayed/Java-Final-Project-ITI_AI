package com.mycompany.final_project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;

class AsciiBanner {

    private static final Logger logger = LoggerFactory.getLogger(AsciiBanner.class);

    private final InputStream file;

    private AsciiBanner(final InputStream file) {

        this.file = file;
    }

    static Optional<AsciiBanner> asciiBanner(final String fileName) {

        final InputStream file = AsciiBanner.class.getClassLoader().getResourceAsStream(fileName);

        if (file != null)
            return Optional.of(new AsciiBanner(file));
        else
            return Optional.empty();
    }

    void print() {

        try {

            final BufferedReader br = new BufferedReader(new InputStreamReader(file));

            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            br.close();

        } catch (final Exception ex) {

            logger.warn("Could not display application ascii banner.", ex);
        }
    }
}
