/*
 * (C) Copyright 2006-2011 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     Nuxeo - initial API and implementation
 *
 */
package org.nuxeo.ecm.core.convert.api;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.apache.commons.collections.CollectionUtils;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.blobholder.BlobHolder;
import org.nuxeo.ecm.core.api.blobholder.DocumentBlobHolder;
import org.nuxeo.ecm.core.blob.ManagedBlob;

/**
 * Base exception raised by the {@link ConversionService}.
 *
 * @author tiry
 */
public class ConversionException extends NuxeoException {

    private static final long serialVersionUID = 1L;

    public ConversionException() {
        super();
    }

    public ConversionException(String message) {
        super(message);
    }

    public ConversionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConversionException(Throwable cause) {
        super(cause);
    }

    public ConversionException(String message, BlobHolder blobHolder) {
        super(message);
        addInfos(blobHolder);
    }

    public ConversionException(String message, Throwable cause, BlobHolder blobHolder) {
        super(message, cause);
        addInfos(blobHolder);

    }

    public ConversionException(Throwable cause, BlobHolder blobHolder) {
        super(cause);
        addInfos(blobHolder);
    }

    public ConversionException(String message, Throwable cause, Blob blob) {
        super(message, cause);
        addInfos(Collections.singleton(blob));
    }

    public ConversionException(String message, Blob blob) {
        super(message);
        addInfos(Collections.singleton(blob));
    }

    protected void addInfos(BlobHolder blobHolder) {
        if (blobHolder instanceof DocumentBlobHolder) {
            DocumentBlobHolder documentBlobHolder = (DocumentBlobHolder) blobHolder;
            addInfo(String.format("Document Id = %s", documentBlobHolder.getDocument().getId()));
        }

        Collection<Blob> blobs = new HashSet<>();
        if (blobHolder.getBlob() != null) {
            blobs.add(blobHolder.getBlob());
        }

        if (CollectionUtils.isNotEmpty(blobHolder.getBlobs())) {
            blobs.addAll(blobHolder.getBlobs());
        }

        addInfos(blobs);
    }

    protected void addInfos(Collection<Blob> blobs) {
        blobs.stream().filter(b -> b instanceof ManagedBlob).map(ManagedBlob.class::cast).forEach(mb -> {
            addInfo(String.format("Blob Key : %s", mb.getKey()));
            addInfo(String.format("Blob Provider : %s", mb.getProviderId()));
        });

    }

}
