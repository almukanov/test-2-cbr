package com.almukanov;

public enum DocumentType {
    REPORT("Отчет", 2000, "A4"),
    INVOICE("Счет", 1500, "A5"),
    LETTER("Письмо", 1000, "A4"),
    MANUAL("Инструкция", 2500, "A3"),
    MEMO("Записка", 500, "A6");

    private final String typeName;
    private final int printingDuration;
    private final String paperSize;

    DocumentType(String typeName, int printingDuration, String paperSize) {
        this.typeName = typeName;
        this.printingDuration = printingDuration;
        this.paperSize = paperSize;
    }

    public String getTypeName() {
        return typeName;
    }

    public int getPrintingDuration() {
        return printingDuration;
    }

    public String getPaperSize() {
        return paperSize;
    }
}
