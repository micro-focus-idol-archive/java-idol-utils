/*
 * Copyright 2015 Hewlett-Packard Development Company, L.P.
 * Licensed under the MIT License (the "License"); you may not use this file except in compliance with the License.
 */

package com.hp.autonomy.idolutils;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.UUID;

import static org.junit.Assert.assertTrue;

public class GenerateIndexableXmlDocumentTest {
    private static final File TEST_DIR = new File("./target/test");

    @BeforeClass
    public static void init() throws IOException {
        FileUtils.forceMkdir(TEST_DIR);
    }

    @AfterClass
    public static void destroy() throws IOException {
        FileUtils.forceDelete(TEST_DIR);
    }

    @Test
    public void generateFile() throws IOException {
        final Collection<String> numericFields = new ArrayList<>(1000);
        final Random random = new Random();
        for (int i = 0; i < 1000; i++) {
            numericFields.add(String.valueOf(1451610061 + random.nextInt(1000)));
        }

        final Collection<SampleDoc> sampleDocs = new ArrayList<>(numericFields.size());
        int i = 0;
        for (final String numericField : numericFields) {
            sampleDocs.add(new SampleDoc(UUID.randomUUID().toString(), "Sample Date Title " + i++, "Sample Date Content", numericField));
        }

        @SuppressWarnings("TypeMayBeWeakened")
        final IdolXmlMarshaller marshaller = new IdolXmlMarshallerImpl();
        final File outputFile = new File(TEST_DIR, "sampleFile.xml");
        FileUtils.writeStringToFile(outputFile, marshaller.generateXmlDocument(sampleDocs, SampleDoc.class, StandardCharsets.UTF_8));
        assertTrue(outputFile.exists());
    }

    @SuppressWarnings({"unused", "WeakerAccess"})
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "sampleDoc", propOrder = {
    })
    @XmlRootElement(namespace = "", name = "DOCUMENT")
    public static class SampleDoc {
        @XmlElement(namespace = "", name = "DREREFERENCE")
        private String reference;
        @XmlElement(namespace = "", name = "DRETITLE")
        private String title;
        @XmlElement(namespace = "", name = "DRECONTENT")
        private String content;
        @XmlElement(namespace = "", name = "SOME_DATE")
        private String numericField;

        public SampleDoc() {
        }

        public SampleDoc(final String reference, final String title, final String content, final String numericField) {
            this.reference = reference;
            this.title = title;
            this.content = content;
            this.numericField = numericField;
        }

        public String getReference() {
            return reference;
        }

        public String getTitle() {
            return title;
        }

        public String getContent() {
            return content;
        }

        public String getNumericField() {
            return numericField;
        }
    }
}
