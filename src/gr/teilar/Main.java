package gr.teilar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Main {
    //Total Songs 3257
    static final int TotalSongs = 3257;
    //Remaining time for completion
    static double rTime = 0;
    static double elapsedMin = 0;
    //add completion percentage example: 10% and approximate remaining time for completion
    //completion percentage is (DoneSongs/TotalSongs)*100
    //for approximate remaining time, find (DoneSongs/minute)/TotalSongs, I could use a thread?
    static int songNum = 0;
    //Exceptions Counter
    static int exCount = 0;
    public static void main(String[] args) {
        long tStart = System.currentTimeMillis();
        Document doc;
        try {
            doc = Jsoup.connect("http://www.notaarsivleri.com/eskisite/Turk_Sanat_Muzigi.html").get();
        } catch (IOException e) {
            System.out.println(exCount++);
            System.out.println("Couldn't make Doc");
            e.printStackTrace();
            return;
        }
        Elements elements = doc.select("div.container-fluid")
                            .select("div.col-md-2")
                            .select("a[href]");
        List<String> UrlList = elements.eachAttr("href");
        Map<String, String> makams = new HashMap<>(UrlList.size());
        int i = 0;
        for (Element element: elements.select("button")) {
            makams.put(element.html().toLowerCase(Locale.forLanguageTag("tr-TR")), UrlList.get(i));
            i++;
        }
        MyThread R = new MyThread("rTimeFinder", 5);
        R.start();
        //MyThread R2 = new MyThread("rTimeFinder", 5);
        //R2.start();
        i = 1;
        double percentage = 0;
        for(Map.Entry<String, String> entry: makams.entrySet()) {
            elapsedMin = (((System.currentTimeMillis() - tStart)/1000.0)/60);
            System.out.println("Makam: " + i + "/" + makams.size() + "  SongNum: " + songNum +  "  Elapsed minutes: "
                    + String.format("%.2f", elapsedMin)
                    + "  Exceptions: " + exCount + "  Completion: " + String.format("%.2f", percentage) + "%"
                    + "  Remaining time: " + String.format("%.2f", R.getrTime()));
            MakamSongs(entry.getValue());
            percentage = (((double)songNum/TotalSongs)*100);
            i++;
        }
        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        double elapsedSeconds = tDelta / 1000.0;
        System.out.println("Elapsed minutes: " + String.format("%.2f", elapsedSeconds/60));
        System.out.println("Exceptions Count:" + exCount);
    }

    private static void MakamSongs(String songsURL) {
        Document doc;
        try {
            doc = Jsoup.connect(songsURL).get();
        } catch (IOException e) {
            System.out.println(exCount++);
            System.out.println("Couldn't make Doc");
            e.printStackTrace();
            return;
        }
        Elements tablo = doc.select(".detay_icon")
                    .select("img");
        Elements elements = new Elements();
        for (Element element: tablo) {
            elements.add(element.parent());
        }
        List<String> UrlList = elements.next()
                .select("a[href]")
                .eachAttr("href");
        //songNum += UrlList.size();
        //System.out.println(UrlList.size());
        Song song;
        for (String url:
             UrlList) {
            //System.out.println(url);
            song = new Song("http://www.notaarsivleri.com" + url);
            songNum++;
            //System.out.println(song);
            song.SaveAudio();
        }
    }
}