package org.project.locks;

import org.project.storage.models.Document;

public class OptimisticLock {
    private Document document;
    private int version;

    public OptimisticLock(Document document) {
        this.document = document;
        this.version = 1;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document, int expectedVersion) {
        if (this.version != expectedVersion) {
            System.err.println("Data has been modified by another transaction.");
            return;
        }
        this.document = document;
        this.version++;
    }

    public int getVersion() {
        return version;
    }
}
