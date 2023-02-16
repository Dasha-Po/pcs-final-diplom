import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {

    private Map<String, List<PageEntry>> wordCount = new HashMap<>();

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        // прочтите тут все pdf и сохраните нужные данные,
        // тк во время поиска сервер не должен уже читать файлы

        File[] pdfFile = pdfsDir.listFiles();
        if (pdfFile != null) {
            for (File pdf : pdfFile) {
                // Чтобы создать объект пдф-документа, нужно указать объект File этого документа следующим классам:
                var doc = new PdfDocument(new PdfReader(pdf));
                int lengthPdf = doc.getNumberOfPages(); // Полистайте методы doc чтобы найти способ узнать количество * страниц в документе.
                for (int i = 1; i <= lengthPdf; i++) {
                    // Чтобы получить объект одной страницы документа, нужно вызвать doc.getPage(номерСтраницы).
                    PdfPage page = doc.getPage(i); // получили страницу
                    // Чтобы получить текст со страницы, используйте var text = PdfTextExtractor.getTextFromPage(page);.
                    var text = PdfTextExtractor.getTextFromPage(page); // получили текст со страницы
                    // Чтобы разбить текст на слова (а они в этих документах разделены могут быть не только пробелами),
                    // используйте var words = text.split("\\P{IsAlphabetic}+");.
                    var words = text.split("\\P{IsAlphabetic}+"); // получили массив слов

                    // для подсчета частоты слов:
                    Map<String, Integer> freqs = new HashMap<>(); // мапа, где ключом будет слово, а значением - частота
                    for (var word : words) { // перебираем слова
                        if (word.isEmpty()) {
                            continue;
                        }
                        word = word.toLowerCase();
                        freqs.put(word, freqs.getOrDefault(word, 0) + 1);
                    }
                    for (String word : freqs.keySet()) {
                        PageEntry wordPageEntry = new PageEntry(pdf.getName(), i, freqs.get(word));
                        if (wordCount.containsKey(word)) {
                            wordCount.get(word).add(wordPageEntry);
                        } else {
                            wordCount.put(word, new ArrayList<>());
                            wordCount.get(word).add(wordPageEntry);
                        }
                        Collections.sort(wordCount.get(word)); // сортируем по кол-ву повторений на странице
                    }
                }
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        // тут реализуйте поиск по слову
        String searchWord = word.toLowerCase();
        List<PageEntry> result = new ArrayList<>();

        if (wordCount.containsKey(searchWord)) {
            result = wordCount.get(searchWord);
        } else {
            System.out.println("Введенное слово не найдено в файлах");
        }

        return result;
    }
}
