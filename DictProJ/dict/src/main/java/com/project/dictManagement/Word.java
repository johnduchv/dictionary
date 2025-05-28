package com.project.dictManagement;

public class Word {
    private String word;
    private String html;
    private int id;


    public Word() {
        word = "";
        html = "";
        id = 0;
    }

    public Word(String word) {
        this.word = normalizeWord(word);
        this.id = DictionaryManagement.getWordId(this);
    }

    public Word(String word, int id) {
        this.word = normalizeWord(word);
        this.id = id;
    }

    public Word(String word, String html, int id) {
        this.word = normalizeWord(word);
        this.html = html;
        this.id = id;
    }

    public Word(Word w) {
        this.word = w.word;
        this.html = w.html;
        this.id = w.id;
    }

    public static String normalizeWord(String word) {
        StringBuilder ret = new StringBuilder();
        for(int i = 0; i < word.length(); ++i) {
            int cur = (int)word.charAt(i);
            if( (cur >= (int)'a' && cur <= (int)'z') || (cur >= (int)'A' && cur <= (int)'Z')
                    || (cur == 45) || (cur == 32) || (cur == 39)  || (cur == 46)){
                ret.append(word.charAt(i));
            }
        }
        String ret1 = ret.toString();
        return ret1.toLowerCase();
    }

    public void copyWord(Word w) {
        this.word = w.word;
        this.html = w.html;
        this.id = w.id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = normalizeWord(word);
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
