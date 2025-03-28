package com.almukanov;

import java.util.concurrent.atomic.AtomicInteger;

public class Document {
    private static final AtomicInteger counter = new AtomicInteger(0);
    private final int id;
    private final DocumentType type;

    public Document(DocumentType type) {
        this.id = counter.incrementAndGet();
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public DocumentType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Document{id=" + id +
                ", type=" + type.getTypeName() +
                ", duration=" + type.getPrintingDuration() +
                ", paperSize=" + type.getPaperSize() +
                '}';
    }
}
