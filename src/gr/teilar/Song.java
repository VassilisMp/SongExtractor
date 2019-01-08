package gr.teilar;

import com.mpatric.mp3agic.*;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Song {
    private String title;
    private String makam;
    private String composer;
    private String lyricist;
    private String form;
    private String rythm;
    private String audioUrl;

    Song(String url) {
        Document doc;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            System.out.println(Main.exCount++);
            System.out.println("Couldn't make Doc");
            e.printStackTrace();
            return;
        }
        //System.out.println("#########");
        //In case something of the following .equals("_"); must be null
        Element ic_tablo = doc.select("div.ic_tablo").first();
        //Form retrieve
        this.form = ElementText(ic_tablo, "Formu");
        if(this.form != null && this.form.equals("İlahi")) {
            return;
        }
        //Title retrieve
        this.title = ElementText(ic_tablo, "Eser Adı");
        //Makam retrieve
        this.makam = ElementText(ic_tablo, "Makamı");
        //Composer retrieve
        this.composer = ElementText(ic_tablo, "Bestekarı");
        //Lyricist retrieve
        this.lyricist = ElementText(ic_tablo, "Söz Yazarı");

        //Rythm retrieve
        this.rythm = ElementText(ic_tablo, "Usulü");
        //Song URL retrieve
        try {
            this.audioUrl = doc
                    .select("audio")
                    .first()
                    .attributes()
                    .get("src");
        }
        catch (NullPointerException ex) {
            Main.exCount++;
            //System.out.println("No audio");
        }
        //System.out.println("#########");
    }

    //returns element's inner html where previous element contains turField in inner html
    // field name in Turkish, field name in English
    private static String ElementText(Element ic_tablo, String turField) {
        String text;
        try {
            text = ic_tablo
                    .getElementsContainingOwnText(turField)
                    .first()
                    .nextElementSibling()
                    .html();
        }
        catch (NullPointerException ex){
            Main.exCount++;
            return null;
            //System.out.println(enField + ": " + "null");
        }
        return text;
    }
    void SaveAudio() {
        if (this.audioUrl == null) {
            System.out.println("Doesn't have audio");
            return;
        }
        String fileName;
        if (this.audioUrl.contains(".mp3")) {
            //System.out.println(matcher.group(0));
            fileName = title + ".mp3";
            if (fileName.contains("pesrev") || fileName.contains("semai"))
                fileName = makam + " " + fileName + " - " + composer;
        } else {
            System.out.println("File isn't MP3\n exiting..");
            return;
        }
        URL audioUrl;
        try {
            audioUrl = new URL(this.audioUrl);
        } catch (MalformedURLException e) {
            Main.exCount++;
            System.out.println("Wrong URL\n Exiting...");
            e.printStackTrace();
            return;
        }
        // to be used only when saving with Tags
        //File file = new File(("noID3" + fileName));
        try {
            title = title.replaceAll("[\"<>:/\\\\|?*]", " ");
        }catch (Exception e) {
            e.printStackTrace();
        }
        String wholeName = "songs" + "/" + makam + "/" + form + "/" + title + " - " + makam + " - " + composer + " - " + lyricist + " - " + form + " - " + rythm + ".mp3";
        File file = new File(wholeName);
        if (file.exists() && (file.length()/1000) > 200)
            return;
        //************
        //System.out.println("Downloading file..");
        try {
            FileUtils.copyURLToFile(audioUrl, file);

            //***********
            //System.out.println(FileUtils.byteCountToDisplaySize(FileUtils.sizeOf(file)));
            if ( (file.length()/1000) < 200) {
                System.out.println("Tiny file");
                file.delete();
                return;
            }

            //***********
            //System.out.println("Done");
        } catch (IOException e) {
            Main.exCount++;
            System.out.println("Couldn't copy URLToFile\n Exiting...");
            e.printStackTrace();
            return;
        }

        /*Mp3File mp3file;
        try {
            mp3file = new Mp3File("noID3" + fileName);
        } catch (UnsupportedTagException | InvalidDataException | IOException e) {
            Main.exCount++;
            System.out.println("Error trying to make Mp3File Object with name" + "noID3" + fileName + "\n Exiting...");
            e.printStackTrace();
            return;
        }
        if (mp3file.hasId3v1Tag()) {
            mp3file.removeId3v1Tag();
        }
        if (mp3file.hasId3v2Tag()) {
            mp3file.removeId3v2Tag();
        }
        if (mp3file.hasCustomTag()) {
            mp3file.removeCustomTag();
        }
        ID3v2 id3v2Tag;
        if (mp3file.hasId3v2Tag()) {
            id3v2Tag = mp3file.getId3v2Tag();
            System.out.println("has tag");
        } else {
            // mp3 does not have an ID3v2 tag, let's create one..
            id3v2Tag = new ID3v24Tag();
            mp3file.setId3v2Tag(id3v2Tag);
        }
        id3v2Tag.setTitle(title);
        id3v2Tag.setGenreDescription(form);
        id3v2Tag.setComposer(composer);
        //((ID3v24Tag) id3v2Tag).setRecordingTime("54");
        id3v2Tag.setComment("lyricist: " + lyricist + "\nrythm: " + rythm + "\nmakam: " + makam);
        //System.out.println("Title: " + id3v2Tag.getTitle());
        try {
            mp3file.save(fileName);
            System.out.println("noID3 file deleted: " + file.delete());
        } catch (NotSupportedException | IOException e) {
            Main.exCount++;
            System.out.println("Couldn't save MP3 file with Tags\n Exiting...");
            e.printStackTrace();
        }*/

    }

    @Override
    public String toString() {
        return "Song{\n" +
                "title=    '" + title + "'\n" +
                "makam=    '" + makam + "'\n" +
                "composer= '" + composer + "'\n" +
                "lyricist= '" + lyricist + "'\n" +
                "form=     '" + form + "'\n" +
                "rythm=    '" + rythm + "'\n}";
    }

    /*
    Mp3File mp3file;
        try {
            mp3file = new Mp3File("noID3" + fileName);
        } catch (UnsupportedTagException | InvalidDataException | IOException e) {
            System.out.println("Error trying to make Mp3File Object with name" + "noID3" + fileName + "\n Exiting...");
            e.printStackTrace();
            return;
        }
        ID3v2 id3v2Tag;
        if (mp3file.hasId3v2Tag()) {
            id3v2Tag = mp3file.getId3v2Tag();
        } else {
            // mp3 does not have an ID3v2 tag, let's create one..
            id3v2Tag = new ID3v24Tag();
            mp3file.setId3v2Tag(id3v2Tag);
        }
        //id3v2Tag.setTrack("5");
        //id3v2Tag.setArtist("An Artist");
        id3v2Tag.setTitle(title);
        //id3v2Tag.setAlbum("The Album");
        //id3v2Tag.setYear("2001");
        //id3v2Tag.setGenre(12);
        //id3v2Tag.setComment("Some comment");
        //id3v2Tag.setLyrics("Some lyrics");
        id3v2Tag.setComposer(composer);
        ((ID3v24Tag) id3v2Tag).setRecordingTime("54");
        //id3v2Tag.setPublisher("A Publisher");
        id3v2Tag.setOriginalArtist("Another Artist");
        id3v2Tag.setAlbumArtist("An Artist");
        id3v2Tag.setCopyright("Copyright");
        id3v2Tag.setUrl("http://foobar");
        id3v2Tag.setEncoder("The Encoder");
        //System.out.println("Title: " + id3v2Tag.getTitle());
        try {
            mp3file.save(fileName);
            System.out.println("noID3 file deleted: " + file.delete());
        } catch (NotSupportedException | IOException e) {
            System.out.println("Couldn't save MP3 file with Tags\n Exiting...");
            e.printStackTrace();
        }
     */
}
