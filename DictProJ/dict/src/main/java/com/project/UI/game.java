package com.project.UI;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
//import com.project.dictManagement.DictionaryCommandline;
//import org.objectweb.asm.tree.InsnList;

import javax.swing.*;
import javax.swing.border.TitledBorder;
//import javax.swing.event.DocumentEvent;
//import javax.swing.event.DocumentListener;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class game extends JDialog {
    private JPanel contentPane;

    private JButton buttonCancel;
    private JButton buttonMissWord;

    private JPanel panelQuestion;
    private JLabel labelQuestion;
    private JLabel labelTheQuestion;

    private JPanel panelAnswer;
    private JLabel labelAnswer;
    private JEditorPane editorPaneAnswer;

    private JPanel missword;
    private ArrayList wordList;
    private ArrayList<String> englishWordList;
    private ArrayList<String> vietnameseWordList;
    private final Set<String> missedWords;
    private int currentWordIndex;
    private JComboBox comboBoxLevelVoc;
    private JLabel labelLevel;

    private missWord dialogMissWord;
    private String selectedLevel;
    private Random randomIndex;

    public game() {
        contentPane = new JPanel(); // Initialize the contentPane
        $$$setupUI$$$();
        setContentPane(contentPane);
        setModal(true);

        randomIndex = new Random();
        missWord dialogMissWord = new missWord();
        dialogMissWord.pack();
        missedWords = new HashSet<>();

        buttonMissWord.addActionListener(e -> {
            dialogMissWord.updateMissedWords(missedWords);
            dialogMissWord.setLocationRelativeTo(null);
            dialogMissWord.setVisible(true);
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Clear the content of textPaneMiss when the main program is closed
                missedWords.clear();
                dialogMissWord.clearMissedWords();
            }
        });

        comboBoxLevelVoc.addActionListener(e -> onLevelSelectionChange()); // Thêm ActionListener cho comboBoxLevelVoc

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        editorPaneAnswer.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume();
                    checkAnswer();
                }
            }
        });

        loadWordsFromFile(); // Load words from the file
        setNextWord(); // Set the initial word

        setTitle("Minigame");
    }

    private void onLevelSelectionChange() {
        selectedLevel = (String) comboBoxLevelVoc.getSelectedItem();
        currentWordIndex = 0; // Đặt lại chỉ số từ vựng khi thay đổi cấp độ
        if (selectedLevel != null) {
            setNextWord();
        }
    }

    private void loadWordsFromFile() {
        wordList = new ArrayList<>();
        englishWordList = new ArrayList<>();
        vietnameseWordList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("dict/Data/game.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("-");
                if (parts.length == 3) {
                    wordList.add(line);
                    englishWordList.add(parts[1].trim());
                    vietnameseWordList.add(parts[2].trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Kiểm tra nếu có mức độ từ vựng đã chọn
        if (selectedLevel != null) {
            setNextWord();
        }
    }


    private void setNextWord() {
        boolean foundWord = false;
        while (currentWordIndex < wordList.size()) {
            String wordLine = (String) wordList.get(currentWordIndex);
            String[] parts = wordLine.split("-");
            if (parts.length == 3 && parts[0].equals(selectedLevel)) {
                labelTheQuestion.setText(parts[2].trim());
                foundWord = true;
                //currentWordIndex++; // Tăng chỉ số để lấy từ tiếp theo khi gặp từ thoả mãn
                break;
            }
            currentWordIndex++;
        }

        if (!foundWord) {
            labelTheQuestion.setText("No more words for the selected level");
            // Nếu không tìm thấy từ nào thỏa mãn, có thể thiết lập lại currentWordIndex để bắt đầu lại từ đầu
            currentWordIndex = -1;
        }
    }

    private void checkAnswer() {
        if (currentWordIndex >= 0 && currentWordIndex < englishWordList.size()) {
            String userInput = editorPaneAnswer.getText();
            String englishWord = englishWordList.get(currentWordIndex); // Lấy từ tiếng Anh
            String vietnameseWord = vietnameseWordList.get(currentWordIndex); // Lấy nghĩa tiếng Việt

            if (userInput.trim().equalsIgnoreCase(englishWord.trim())) {
                JOptionPane.showMessageDialog(game.this, "Đúng!");
                //currentWordIndex += 50;
                currentWordIndex = randomIndex.nextInt(10) + currentWordIndex;
                // Bỏ qua phần tăng chỉ số currentWordIndex vì đã được thực hiện trong setNextWord()
            } else {
                JOptionPane.showMessageDialog(game.this, "Sai! Từ tiếng Anh đúng là: " + englishWord);
                // Thêm từ tiếng Anh cần điền ở bên cạnh chữ "sai"
                editorPaneAnswer.setText(englishWord);
                // Lưu từ sai vào file Miss.txt
                String missedWord = comboBoxLevelVoc.getSelectedItem() + "-" + englishWord + "-" + vietnameseWord;
                if (missedWords.add(missedWord)) { // Kiểm tra nếu từ chưa có trong set
                    saveMissedWordToFile(missedWord);
                }
            }
            setNextWord();
            editorPaneAnswer.setText("");
        } else {
            System.out.println("Error: currentWordIndex is less than 0 or greater than or equal to englishWordList size");
        }
    }

    private void saveMissedWordToFile(String missedWord) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Data/Miss.txt", true))) {
            writer.write(missedWord);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        game dialog = new game();
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

        System.exit(0);
    }


    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(3, 1, new Insets(10, 10, 10, 10), -1, -1));
        contentPane.setBackground(new Color(-11711155));
        contentPane.setPreferredSize(new Dimension(640, 480));
        panel1.add(contentPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        contentPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-11711155)), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel3.setAutoscrolls(false);
        panel3.setBackground(new Color(-12559704));
        panel2.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-1)), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        buttonMissWord = new JButton();
        buttonMissWord.setBackground(new Color(-9805144));
        Font buttonMissWordFont = this.$$$getFont$$$("Consolas", -1, 18, buttonMissWord.getFont());
        if (buttonMissWordFont != null) buttonMissWord.setFont(buttonMissWordFont);
        buttonMissWord.setForeground(new Color(-4397831));
        buttonMissWord.setHideActionText(false);
        buttonMissWord.setText("Missword");
        panel3.add(buttonMissWord, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setBackground(new Color(-9805144));
        Font buttonCancelFont = this.$$$getFont$$$("Consolas", -1, 18, buttonCancel.getFont());
        if (buttonCancelFont != null) buttonCancel.setFont(buttonCancelFont);
        buttonCancel.setForeground(new Color(-4397831));
        buttonCancel.setText("Cancel");
        panel3.add(buttonCancel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel4.setBackground(new Color(-12559704));
        contentPane.add(panel4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel4.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-1)), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        panelQuestion = new JPanel();
        panelQuestion.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        panelQuestion.setBackground(new Color(-12559704));
        panel4.add(panelQuestion, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panelQuestion.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-12559704)), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, new Color(-1)));
        labelLevel = new JLabel();
        Font labelLevelFont = this.$$$getFont$$$("Consolas", -1, 18, labelLevel.getFont());
        if (labelLevelFont != null) labelLevel.setFont(labelLevelFont);
        labelLevel.setForeground(new Color(-4397831));
        labelLevel.setText("         Lựa chọn cấp độ từ vựng:            ");
        panelQuestion.add(labelLevel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labelQuestion = new JLabel();
        Font labelQuestionFont = this.$$$getFont$$$("Consolas", -1, 18, labelQuestion.getFont());
        if (labelQuestionFont != null) labelQuestion.setFont(labelQuestionFont);
        labelQuestion.setForeground(new Color(-4397831));
        labelQuestion.setText("Điền nghĩa của từ  tiếng Việt vào ô \"Enter the answer\"");
        panelQuestion.add(labelQuestion, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labelTheQuestion = new JLabel();
        Font labelTheQuestionFont = this.$$$getFont$$$("Consolas", -1, 18, labelTheQuestion.getFont());
        if (labelTheQuestionFont != null) labelTheQuestion.setFont(labelTheQuestionFont);
        labelTheQuestion.setForeground(new Color(-4397831));
        labelTheQuestion.setText("Label");
        panelQuestion.add(labelTheQuestion, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        comboBoxLevelVoc = new JComboBox();
        comboBoxLevelVoc.setAutoscrolls(false);
        comboBoxLevelVoc.setBackground(new Color(-11711155));
        Font comboBoxLevelVocFont = this.$$$getFont$$$("Consolas", -1, 16, comboBoxLevelVoc.getFont());
        if (comboBoxLevelVocFont != null) comboBoxLevelVoc.setFont(comboBoxLevelVocFont);
        comboBoxLevelVoc.setForeground(new Color(-4397831));
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("B1");
        defaultComboBoxModel1.addElement("B2");
        defaultComboBoxModel1.addElement("C1");
        comboBoxLevelVoc.setModel(defaultComboBoxModel1);
        panelQuestion.add(comboBoxLevelVoc, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panelAnswer = new JPanel();
        panelAnswer.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelAnswer.setBackground(new Color(-12559704));
        contentPane.add(panelAnswer, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panelAnswer.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-1)), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        labelAnswer = new JLabel();
        labelAnswer.setBackground(new Color(-4397831));
        Font labelAnswerFont = this.$$$getFont$$$("Consolas", -1, 18, labelAnswer.getFont());
        if (labelAnswerFont != null) labelAnswer.setFont(labelAnswerFont);
        labelAnswer.setForeground(new Color(-4397831));
        labelAnswer.setText("Enter the answer:");
        panelAnswer.add(labelAnswer, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        editorPaneAnswer = new JEditorPane();
        editorPaneAnswer.setAutoscrolls(true);
        editorPaneAnswer.setBackground(new Color(-11711155));
        editorPaneAnswer.setCaretColor(new Color(-21419));
        Font editorPaneAnswerFont = this.$$$getFont$$$("Consolas", -1, 18, editorPaneAnswer.getFont());
        if (editorPaneAnswerFont != null) editorPaneAnswer.setFont(editorPaneAnswerFont);
        editorPaneAnswer.setForeground(new Color(-4397831));
        panelAnswer.add(editorPaneAnswer, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}