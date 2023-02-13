import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PageEntry implements Comparable<PageEntry> {
    private final String pdfName;
    private final int page;
    private final int count;

    public PageEntry(String pdfName, int page, int count) {
        this.pdfName = pdfName;
        this.page = page;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    // списки ответов для каждого слова должны быть отсортированы в порядке уменьшения поля count.
    // Для этого предлагается классу PageEntry сразу реализовывать интерфейс Comparable
    @Override
    public int compareTo(PageEntry o) {
        return Integer.compare(o.getCount(), this.getCount());
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        return gson.toJson(this);
//        return "PageEntry{ pdf = " + pdfName + ", page = "+ page +", count = "+ count + "}";
    }
}
