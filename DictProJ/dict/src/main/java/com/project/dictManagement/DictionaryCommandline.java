package com.project.dictManagement;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import com.darkprograms.speech.translator.GoogleTranslate;

import com.darkprograms.speech.synthesiser.SynthesiserV2;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;


/** Nếu thao tác với Dictionary thì thao tác thông qua DictionaryCommandline. */
public class DictionaryCommandline {

    /** Find similar word to provided word
     * @param word the word to find similar
     * @return an array of pair that contains word's id and word's name
     */
    public static ArrayList<Pair<Integer,String> > makeSuggestion(String word) {
        Word w = new Word(word);
        return DictionaryManagement.stringSimilarWords(w);
    }

    /** Get word content from database with id
     * @param id word's id
     * @return a pair with word and its content(html)
     */
    public static Pair<String,String> getExactWord(int id) {
        return DictionaryManagement.selectWordAndHtmlWithId(id);
    }

    /** Get word content from database with string word
     * @param word string word
     * @return a pair with word and its content(html)
     */
    public static Pair<String,String> getExactWord(String word) {
        Word w = new Word(word);
        return DictionaryManagement.selectWordAndHtmlWithId(w.getId());
    }
    // Cần check xem từ có tồn tại hay không
    /** Insert new word to database
     * @param word word to be inserted
     * @param pronounce word's pronunciation
     * @param description word's description
     */
    public static boolean insertWord(String word,String pronounce, String description) {
        String html = toHtml(word,pronounce,description);
        Word w = new Word(word,html,DictionaryManagement.getMaxWordId()+1);
        try{
            DictionaryManagement.insertWordToTable(w);
        }
        catch(existException e) {
            return false;
        }
        return true;
    }

    /** Delete word from database
     * @param id word's id
     */
    public static boolean deleteWord(int id) {
        Word w = new Word(DictionaryManagement.selectWordWithId(id),id);
        try{
            DictionaryManagement.deleteWord(w);
        }
        catch(nonExistException e) {
            return false;
        }
        return true;
    }

    public static boolean checkConnection() {
        try {
            URL url = new URL("https://www.google.com");
            URLConnection connection = url.openConnection();
            connection.connect();
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public static String sentenceTranslator(String sentence){
        if(checkConnection()){
            if(sentence.isEmpty()) {
                return "";
            }
            String ret = "";
            try{
                ret = GoogleTranslate.translate("vi",sentence);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return ret;
        }
        return "";
    }

    public static void speak(String sentence) {
        Thread thread = new Thread(() -> {
            try {

                //Create a JLayer instance
                AdvancedPlayer player = new AdvancedPlayer(synthesizer.getMP3Data(sentence));
                player.play();

            } catch (IOException | JavaLayerException e) {

                e.printStackTrace();

            }
        });
        thread.setDaemon(false);

        thread.start();

    }

    public static void close() {
        DictionaryManagement.sortDatabase();
        System.out.println("Closed completely!");
    }

    public static String toHtml(String word, String pronounce,String description){
        String wordHtml = "<h1>" + word + "</h1>";
        String pronounceHtml = "<h3><i>" + pronounce + "</i></h3>";
        String desHtml = description;
        return wordHtml + pronounceHtml + desHtml;
    }
    static SynthesiserV2 synthesizer = new SynthesiserV2("AIzaSyBOti4mM-6x9WDnZIjIeyEU21OpBXqWBgw");
}
