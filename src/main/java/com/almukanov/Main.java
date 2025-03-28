package com.almukanov;

import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        PrintDispatcher dispatcher = new PrintDispatcher();

        dispatcher.acceptDocument(new Document(DocumentType.REPORT));
        dispatcher.acceptDocument(new Document(DocumentType.INVOICE));
        dispatcher.acceptDocument(new Document(DocumentType.LETTER));
        dispatcher.acceptDocument(new Document(DocumentType.MANUAL));
        dispatcher.acceptDocument(new Document(DocumentType.MEMO));

        // Оменяем документ с id = 3
        Thread.sleep(500); // небольшая задержка для имитации асинхронности
        dispatcher.cancelDocument(3);

        Thread.sleep(6000); // Даём время на печать нескольких документов

        // Получаем отсортированный список напечатанных документов по типу
        List<Document> sortedDocs = dispatcher.getSortedPrintedDocuments("type");
        System.out.println("Отсортированные напечатанные документы по типу:");
        sortedDocs.forEach(System.out::println);

        // Выводим статистику
        dispatcher.printStatistics();

        // Останавливаем диспетчер
        List<Document> notPrinted = dispatcher.stopDispatcher();
        notPrinted.forEach(System.out::println);
    }
}
