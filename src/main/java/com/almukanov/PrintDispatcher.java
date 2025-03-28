package com.almukanov;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class PrintDispatcher {
    private final BlockingQueue<Document> printQueue = new LinkedBlockingQueue<>();
    private final List<Document> printedDocuments = Collections.synchronizedList(new ArrayList<>());
    private final AtomicInteger printedCount = new AtomicInteger(0);
    private final AtomicInteger remainingCount = new AtomicInteger(0);
    private final List<Integer> remainingDocuments = Collections.synchronizedList(new ArrayList<>());
    // Флаг для остановки диспетчера
    private volatile boolean running = true;
    private final Thread printingThread;

    public PrintDispatcher() {
        printingThread = new Thread(() -> {
            while (running || !printQueue.isEmpty()) {
                try {
                    Document doc = printQueue.poll(500, TimeUnit.MILLISECONDS);
                    if (doc != null) {
                        // Симуляция печати
                        System.out.println("Печать документа: " + doc);
                        Thread.sleep(doc.getType().getPrintingDuration());
                        printedDocuments.add(doc);
                        printedCount.incrementAndGet();
                        remainingDocuments.remove(Integer.valueOf(doc.getId()));
                        remainingCount.decrementAndGet();
                        System.out.println("Документ напечатан: " + doc);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            System.out.println("Диспетчер остановлен.");
        });
        printingThread.start();
    }

    public void acceptDocument(Document doc) {
        printQueue.offer(doc);
        remainingCount.incrementAndGet();
        remainingDocuments.add(doc.getId());
        System.out.println("Принят документ: " + doc);
    }

    // Отмена печати
    public boolean cancelDocument(int documentId) {
        for (Document doc : printQueue) {
            if (doc.getId() == documentId) {
                boolean removed = printQueue.remove(doc);
                if (removed) {
                    remainingCount.decrementAndGet();
                    remainingDocuments.remove(Integer.valueOf(doc.getId()));
                    System.out.println("Отменена печать документа: " + doc);
                }
                return removed;
            }
        }
        System.out.println("Документ с id=" + documentId + " не найден или уже печатается.");
        return false;
    }

    // Остановка диспетчера
    public List<Document> stopDispatcher() {
        running = false;
        printingThread.interrupt();
        List<Document> notPrinted = new ArrayList<>(printQueue);
        printQueue.clear();
        return notPrinted;
    }

    // Сортировка
    public List<Document> getSortedPrintedDocuments(String sortBy) {
        List<Document> sorted = new ArrayList<>(printedDocuments);
        switch (sortBy.toLowerCase()) {
            case "order":
                break;
            case "type":
                sorted.sort(Comparator.comparing(d -> d.getType().getTypeName()));
                break;
            case "duration":
                sorted.sort(Comparator.comparingInt(d -> d.getType().getPrintingDuration()));
                break;
            case "papersize":
                sorted.sort(Comparator.comparing(d -> d.getType().getPaperSize()));
                break;
            default:
                System.out.println("Неизвестный критерий сортировки.");
        }
        return sorted;
    }

    // Расчет средней продолжительности печати
    public double getAveragePrintingDuration() {
        if (printedDocuments.isEmpty()) {
            return 0.0;
        }
        int totalDuration = printedDocuments.stream()
                .mapToInt(doc -> doc.getType().getPrintingDuration())
                .sum();
        return (double) totalDuration / printedDocuments.size();
    }

    // Статистика
    public void printStatistics() {
        System.out.println("Количество напечатанных документов: " + printedCount.get());
        System.out.println("Количество оставшихся документов: " + remainingCount.get());
        System.out.println("Средняя продолжительность печати: " + getAveragePrintingDuration() + " мс");
        System.out.println("Номера оставшихся документов: " + remainingDocuments);
    }
}
