package com.project.dictManagement;

import java.util.ArrayList;

public class Trie {
    public Trie() {
        trie = new node[10000000];
        trie[0] = new node();
        curNode = new ArrayList<Integer>();
    }

    public int getValidIntChar(char x) {
        if( ((int)x < (int)'a' || (int)x > (int)'z') && ((int)x != 45 ) && ((int)x != 32 ) && ((int)x != 39 )
                && ((int)x != 46)) return -1;
        int nextChar = (x - 'a');
        if((int)x == 45) {
            nextChar = 26;
        }
        if((int)x == 32) {
            nextChar = 27;
        }
        if((int)x == 39) {
            nextChar = 28;
        }
        if((int)x == 46) {
            nextChar = 29;
        }
        return nextChar;
    }

    /** Trả lại vị trí của word trên cây Trie. */
    public int traverse(String word) {
        int pos = 0;
        int nextChar = 0;
        for(int i = 0; i < word.length(); ++i) {
            nextChar = getValidIntChar(word.charAt(i));
            if(nextChar < 0) continue;
            if(trie[pos].getNext()[nextChar] == 0) {
                ++this.curMax;
                trie[curMax] = new node();
                trie[pos].setNext(nextChar, this.curMax);
            }
            pos = trie[pos].getNext()[nextChar];

        }
        return pos;
    }

    public int traverseNonInsert(String word) {
        int pos = 0;
        int nextChar = 0;
        for(int i = 0; i < word.length(); ++i) {
            nextChar = getValidIntChar(word.charAt(i));
            if(nextChar < 0) continue;
            if(trie[pos].getNext()[nextChar] == 0) {
                return 0;
            }
            pos = trie[pos].getNext()[nextChar];
        }

        return pos;
    }

    /** Thêm một từ mới vào cây Trie. */
    public void addWord(String word,int wordId) {
        int pos = traverse(word);
        if(trie[pos].getId() == 0) {
            trie[pos].setId(wordId);
        }

    }

    /** Tìm Word Id. */
    public int findWordId(String word) {
        int pos = traverseNonInsert(word);
        if(pos == 0) return 0;
        if(word.charAt(0) == 'a') {
            System.out.println(trie[pos].getId());
        }
        return trie[pos].getId();
    }

    /** Xóa từ khỏi cây Trie. */
    public void deleteWord(String word) {
        int pos = traverseNonInsert(word);
        trie[pos].setId(0);
    }

    /** Di chuyển vị trí hiện tại khi từ ta đang có được thêm chữ cái mới. */
    public void traverseNextChar(char nextChar) {
        int intNextChar = getValidIntChar(nextChar);
        if(intNextChar < 0) return;
        curNode.add(trie[getLastCurNode()].getNext()[intNextChar]);
    }

    /** Di chuyển vị trí hiện tại khi từ ta đang có xóa đi chữ cái cuối. */
    public void traverseBack() {
        curNode.remove(getCurNodeSize() - 1);
    }

    /** Xóa hết. */
    public void reset() {
        curNode.clear();
    }
    /** Tìm tối đa 7 từ tương tự với từ hiện tại đang có. */
    public void findSimilar(ArrayList<Integer> resList,int curFindNode) {
        if(trie[curFindNode].getId() != 0) {
            resList.add(trie[curFindNode].getId());
        }
        if (resList.size() == 7) {
            return;
        }
        for(int i = 0;i <= 28;++i) {
            if(trie[curFindNode].getNext()[i] != 0) {
                findSimilar(resList, trie[curFindNode].getNext()[i]);
            }
            if (resList.size() == 7) {
                return;
            }
        }
    }

    public ArrayList<Integer> getCurNode() {
        return this.curNode;
    }

    public int getLastCurNode() {
        if(getCurNodeSize() >= 1)
            return this.curNode.get(getCurNodeSize() - 1);
        else return 0;
    }

    public int getCurNodeSize() {
        return this.curNode.size();
    }
    private node[] trie = null;
    private int curMax = 0;
    private ArrayList<Integer> curNode = null;

    class node {

        public node() {
            next = new int[30];
        }
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int[] getNext() {
            return next;
        }

        public void setNext(int index, int newId) {
            this.next[index] = newId;
        }

        private int id;
        private int[] next;
    }
}
