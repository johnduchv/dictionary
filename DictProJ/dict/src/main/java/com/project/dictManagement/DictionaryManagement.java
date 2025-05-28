package com.project.dictManagement;

import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;


public class DictionaryManagement {
    /**
     * Connect to a sample database
     */
    public static void connect() {
        try {
            // db parameters
            String url = "jdbc:sqlite:dict_hh.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public static boolean internetConnection() {
        boolean flag = true;
        try {
            String url = "www.google.com";
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to Internet has been established.");
        } catch (Exception e) {
            flag = false;
            System.out.println(e.getMessage());
        }
        return flag;
    }

    /**
     * Thử lại.
     */
    public static ArrayList<Pair<Integer, String>> stringSimilarWords(Word w) {
        ArrayList<Pair<Integer, String>> res = new ArrayList<>();
        myTrie.reset();
        String word = w.getWord();
        int lastPosOnTrie = myTrie.traverseNonInsert(word);
        if (lastPosOnTrie == 0 && word.length() > 0) {
            return res;
        }
        ArrayList<Integer> ListId = new ArrayList<>();
        myTrie.findSimilar(ListId, lastPosOnTrie);
        Pair<Integer, String> toAdd = new Pair<>();
        System.out.println("Similar word to " + word + ":");
        for (Integer re : ListId) {
            toAdd.setKey(re);
            toAdd.setValue(DictionaryManagement.selectWordWithId(re));
            res.add(toAdd);
            System.out.println(DictionaryManagement.selectWordWithId(re));
        }
        return res;
    }

    public static ArrayList<String> stringSimilarWord(String s) {
        ArrayList<String> res = new ArrayList<>();
        myTrie.reset();
        int lastPosOnTrie = myTrie.traverseNonInsert(s);
        if (lastPosOnTrie == 0 && s.length()>0) {
            return res;
        }
        ArrayList<Integer> ListId = new ArrayList<>();
        myTrie.findSimilar(ListId, lastPosOnTrie);
        for (Integer re : ListId) {
            String temp = DictionaryManagement.selectWordWithId(re);
            res.add(temp);
        }
        return res;
    }


    public static void buildTrie() {
        String sql = "SELECT id, word FROM av";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                myTrie.addWord(Word.normalizeWord(rs.getString("word")), rs.getInt("id"));
                maxWordId += 1;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void selectAll() {
        String sql = "SELECT id, word FROM av";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("id") + "\t" +
                        rs.getString("word"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public static void updateWord(Word w) {
        String sql = "UPDATE av SET html = ? "
                + "WHERE id = ?";
        if (w.getId() == 0) return;
        try (PreparedStatement preStatement = conn.prepareStatement(sql)) {
            preStatement.setString(1, w.getHtml());
            preStatement.setInt(2, w.getId());
            // update
            preStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void deleteWord(Word w) throws nonExistException {
        if (w.getId() == 0 || Objects.equals(w.getWord(), "")) {
            throw new nonExistException("Word does not exist");
        }
        String sql = "DELETE FROM av WHERE id = ?";
        // Delete on trie
        myTrie.deleteWord(w.getWord());
        try (PreparedStatement preStatement = conn.prepareStatement(sql)) {
            preStatement.setInt(1, w.getId());
            preStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static String selectWordWithId(int id) {
        String sql = "SELECT word FROM av WHERE id = ?";
        if (id == 0) return "";
        try (PreparedStatement preStatement = conn.prepareStatement(sql)) {
            preStatement.setInt(1, id);
            try (ResultSet rs = preStatement.executeQuery()) {
                return Word.normalizeWord(rs.getString("word"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    public static String selectWordHtmlWithId(int id) {
        String sql = "SELECT html FROM av WHERE id = ?";
        if (id == 0) return "";
        try (PreparedStatement preStatement = conn.prepareStatement(sql)) {
            preStatement.setInt(1, id);
            try (ResultSet rs = preStatement.executeQuery()) {
                return rs.getString("html");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    public static Pair<String, String> selectWordAndHtmlWithId(int id) {
        String sql = "SELECT word,html FROM av WHERE id = ?";
        Pair<String, String> ret = new Pair<>();
        if (id == 0) return ret;
        try (PreparedStatement preStatement = conn.prepareStatement(sql)) {
            preStatement.setInt(1, id);
            try (ResultSet rs = preStatement.executeQuery()) {
                ret.setKey(Word.normalizeWord(rs.getString("word")));
                ret.setValue(rs.getString("html"));
                return ret;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return ret;
    }

    public static void insertWordToTable(Word w) throws existException {

        if (getWordId(w) > 0) {
            throw new existException(w.getWord() + " already exists");
        }

        String sql = "INSERT INTO av(id,word,html) VALUES(?,?,?)";
        ++maxWordId;
        myTrie.addWord(w.getWord(), w.getId());
        try (PreparedStatement preStatement = conn.prepareStatement(sql)) {
            preStatement.setInt(1, w.getId());
            preStatement.setString(2, w.getWord());
            preStatement.setString(3, w.getHtml());
            preStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void sortDatabase() {
        String s1 = "CREATE TABLE av_ordered (id INTEGER PRIMARY KEY, word TEXT, html TEXT, description TEXT, pronounce TEXT)";
        String s2 = "INSERT INTO av_ordered (word, html, description, pronounce) SELECT word, html, description, pronounce FROM av ORDER BY word";
        String s3 = "DROP TABLE av";
        String s4 = "ALTER TABLE av_ordered RENAME TO av";
        String s5 = "VACUUM";
        try (PreparedStatement preStatement1 = conn.prepareStatement(s1)) {
            preStatement1.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        try (PreparedStatement preStatement2 = conn.prepareStatement(s2)) {
            preStatement2.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        try (PreparedStatement preStatement3 = conn.prepareStatement(s3)) {
            preStatement3.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        try (PreparedStatement preStatement4 = conn.prepareStatement(s4)) {
            preStatement4.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        try (PreparedStatement preStatement5 = conn.prepareStatement(s5)) {
            preStatement5.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static int getWordId(Word w) {
        return myTrie.findWordId(w.getWord());
    }

    public static int getMaxWordId() {
        return maxWordId;
    }

    public static Connection conn = null;
    public static final Trie myTrie = new Trie();
    /**
     * Sẽ sửa lại nếu có sort word khi tắt chương trình.
     */
    private static int maxWordId = 0;

    public static void main(String[] args) {
    }
}