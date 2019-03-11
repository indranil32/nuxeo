package org.nuxeo.ecm.platform.convert.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collections;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.mockito.Mockito;
import org.nuxeo.ecm.core.api.blobholder.BlobHolder;
import org.nuxeo.ecm.core.api.blobholder.DocumentBlobHolder;
import org.nuxeo.ecm.core.api.blobholder.SimpleBlobHolder;
import org.nuxeo.ecm.core.blob.SimpleManagedBlob;
import org.nuxeo.ecm.core.convert.api.ConversionException;

/**
 * @since 11.1
 */
public class ConversionServiceTest extends BaseConverterTest {

    @Test
    public void shouldFailWithoutAdditionalInfosWhenMimeTypeIsNotSupportedByConverter() throws IOException {
        String converterName = cs.getConverterName("image/jpeg", "application/pdf");
        BlobHolder blobHolder = getBlobFromPath("test-docs/" + "hello.xls", "application/vnd.ms-excel");

        try {
            cs.convert(converterName, blobHolder, Collections.emptyMap());
        } catch (ConversionException e) {
            assertNotNull(e.getInfos());
            assertTrue(CollectionUtils.isEmpty(e.getInfos()));
        }

    }

    @Test
    public void shouldFailWithAdditionalInfosWhenMimeTypeIsNotSupportedByConverter() throws IOException {
        String converterName = cs.getConverterName("image/jpeg", "application/pdf");
        DocumentBlobHolder documentBlobHolder = getDocumentBlob("test-docs/" + "hello.xls", "application/vnd.ms-excel");

        try {
            cs.convert(converterName, documentBlobHolder, Collections.emptyMap());
        } catch (ConversionException e) {
            assertTrue(e.getInfos().size() == 1);
            assertTrue(e.getInfos().get(0).contains(documentBlobHolder.getDocument().getId()));

        }

    }

    @Test
    public void shouldFailWithAdditionalInfosWhenReadingDocumentBlobContent() throws IOException {
        SimpleManagedBlob mock = Mockito.mock(SimpleManagedBlob.class);

        when(mock.getEncoding()).thenReturn("UTF-8");
        when(mock.getKey()).thenReturn("anyKey:anyKey");
        when(mock.getProviderId()).thenReturn("anyProvider:anyProvider");
        when(mock.getMimeType()).thenReturn("text/html");
        when(mock.getStream()).thenThrow(IOException.class);

        DocumentBlobHolder documentBlobHolder = createDocumentBlob(mock);

        try {
            cs.convert("html2text", documentBlobHolder, Collections.emptyMap());
        } catch (ConversionException e) {
            assertTrue(e.getInfos().size() == 3);
            assertTrue(e.getInfos().get(0).contains("anyProvider:anyProvider"));
            assertTrue(e.getInfos().get(1).contains("anyKey:anyKey"));
            assertTrue(e.getInfos().get(2).contains(documentBlobHolder.getDocument().getId()));

        }

    }

    @Test
    public void shouldFailWithAdditionalInfosWhenReadingDBlobContent() throws IOException {
        SimpleManagedBlob mock = Mockito.mock(SimpleManagedBlob.class);

        when(mock.getEncoding()).thenReturn("UTF-8");
        when(mock.getKey()).thenReturn("anyKey:anyKey");
        when(mock.getProviderId()).thenReturn("anyProvider:anyProvider");
        when(mock.getMimeType()).thenReturn("text/html");
        when(mock.getDigest()).thenReturn("anyDigest");
        when(mock.getStream()).thenThrow(IOException.class);

        try {
            cs.convert("html2text", new SimpleBlobHolder(mock), Collections.emptyMap());
        } catch (ConversionException e) {
            assertTrue(e.getInfos().size() == 2);
            assertTrue(e.getInfos().get(0).contains("anyProvider:anyProvider"));
            assertTrue(e.getInfos().get(1).contains("anyKey:anyKey"));

        }

    }

}
