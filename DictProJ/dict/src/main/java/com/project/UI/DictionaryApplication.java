package com.project.UI;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.project.dictManagement.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.*;
import java.util.Locale;
import java.util.Objects;


public class DictionaryApplication extends JFrame {

    /**
     * panel chính và nhãn ghi tên app
     */
    private JPanel appPanel;
    private JLabel appLabel;

    private JPanel transWordPanel;
    private JButton transWordButton;
    private JLabel transWordLabel;

    private JPanel addWordPanel;
    private JLabel addWordLabel;
    private JButton addWordButton;


    private JPanel delWordPanel;
    private JLabel delWordLabel;
    private JButton delWordButton;


    private JPanel transSentencePanel;
    private JLabel transSentenceLabel;
    private JButton transSentenceButton;

    private JButton gameButton;

    private JTextField transWordText;
    private JButton editWordButton;
    private JList<String> suggestionTransWordList;
    private JPanel editWordPanel;
    private JEditorPane transSentenceEditorPane;

    /**
     * Input from Different Area.
     */
    public static String addStr;
    public static String addPro;
    public static String addDef;
    public static String delStr;
    public static String editStr;
    public static String editPronounce = "";
    public static String editDefinition = "";
    public static String transStr;
    public static String sentence;

    /**
     * Run.
     *
     * @param title String
     */
    public DictionaryApplication(String title) {
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(appPanel);
        this.pack();

        DictionaryManagement.connect();
        DictionaryManagement.buildTrie();

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                DictionaryCommandline.close();
                dispose();
            }
        });

        addWordButton.addActionListener(e -> {
            addWord dialog = new addWord();
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        });

        delWordButton.addActionListener(e -> {
            delWord dialog = new delWord();
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        });

        gameButton.addActionListener(e -> {
            game dialog = new game();
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        });

        transWordButton.addActionListener(e -> {
            transStr = transWordText.getText();
            Word w = new Word(transStr);
            if (w.getId() == 0 || Objects.equals(w.getWord(), "")) {
                JOptionPane.showMessageDialog(appPanel, "This word is non-existed in dictionary");
            } else {
                transWord dialog = new transWord();
                dialog.pack();
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);
            }

        });

        Border b = new LineBorder(new Color(152, 158, 161), 1);
        transSentenceEditorPane.setBorder(b);

        transSentenceButton.addActionListener(e -> {
            sentence = transSentenceEditorPane.getText();
            transSentence dialog = new transSentence();
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        });
        editWordButton.addActionListener(e -> {
            editWord dialog = new editWord();
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        });
        transWordText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                DefaultListModel<String> listModel = new DefaultListModel<>();
                String s = transWordText.getText();
                listModel.addAll(DictionaryManagement.stringSimilarWord(s));
                suggestionTransWordList.setModel(listModel);
            }
        });

        suggestionTransWordList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                transWordText.setText(suggestionTransWordList.getSelectedValue());
            }
        });
    }

    public static String getTransStr() {
        return transStr;
    }

    public static String getSentence() {
        return sentence;
    }

    public static void runApplication() {
        JFrame frame = new DictionaryApplication("Dictionary");
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        appPanel = new JPanel();
        appPanel.setLayout(new GridLayoutManager(5, 6, new Insets(0, 0, 0, 0), -1, -1));
        appPanel.setBackground(new Color(-11711155));
        appPanel.setForeground(new Color(-16777216));
        appPanel.setPreferredSize(new Dimension(1280, 720));
        transWordPanel = new JPanel();
        transWordPanel.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        transWordPanel.setBackground(new Color(-12559704));
        transWordPanel.setDoubleBuffered(true);
        transWordPanel.setFocusTraversalPolicyProvider(true);
        appPanel.add(transWordPanel, new GridConstraints(1, 1, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(1180, 59), null, 0, false));
        transWordPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-6236)), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        transWordLabel = new JLabel();
        Font transWordLabelFont = this.$$$getFont$$$("Consolas", -1, 18, transWordLabel.getFont());
        if (transWordLabelFont != null) transWordLabel.setFont(transWordLabelFont);
        transWordLabel.setForeground(new Color(-4397831));
        transWordLabel.setHorizontalAlignment(0);
        transWordLabel.setText("  Translate a word:   ");
        transWordPanel.add(transWordLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(120, 30), null, 0, false));
        transWordText = new JTextField();
        transWordText.setBackground(new Color(-11711155));
        transWordText.setCaretColor(new Color(-21419));
        transWordText.setDisabledTextColor(new Color(-16053));
        Font transWordTextFont = this.$$$getFont$$$("Consolas", Font.PLAIN, 20, transWordText.getFont());
        if (transWordTextFont != null) transWordText.setFont(transWordTextFont);
        transWordText.setForeground(new Color(-4397831));
        transWordText.setSelectedTextColor(new Color(-12559704));
        transWordText.setSelectionColor(new Color(-4397831));
        transWordText.setText("");
        transWordPanel.add(transWordText, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        transWordButton = new JButton();
        transWordButton.setAutoscrolls(false);
        transWordButton.setBackground(new Color(-9805144));
        transWordButton.setBorderPainted(true);
        transWordButton.setEnabled(true);
        Font transWordButtonFont = this.$$$getFont$$$("Consolas", -1, 18, transWordButton.getFont());
        if (transWordButtonFont != null) transWordButton.setFont(transWordButtonFont);
        transWordButton.setForeground(new Color(-4397831));
        transWordButton.setText("Translate");
        transWordButton.setVisible(true);
        transWordPanel.add(transWordButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        suggestionTransWordList = new JList();
        suggestionTransWordList.setBackground(new Color(-11711155));
        suggestionTransWordList.setEnabled(true);
        Font suggestionTransWordListFont = this.$$$getFont$$$("Consolas", -1, 18, suggestionTransWordList.getFont());
        if (suggestionTransWordListFont != null) suggestionTransWordList.setFont(suggestionTransWordListFont);
        suggestionTransWordList.setForeground(new Color(-4397831));
        suggestionTransWordList.setSelectionForeground(new Color(-12559704));
        suggestionTransWordList.setSelectionMode(1);
        transWordPanel.add(suggestionTransWordList, new GridConstraints(1, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        delWordPanel = new JPanel();
        delWordPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        delWordPanel.setBackground(new Color(-12559704));
        appPanel.add(delWordPanel, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(228, 59), null, 0, false));
        delWordPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-6236)), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        delWordLabel = new JLabel();
        Font delWordLabelFont = this.$$$getFont$$$("Consolas", -1, 18, delWordLabel.getFont());
        if (delWordLabelFont != null) delWordLabel.setFont(delWordLabelFont);
        delWordLabel.setForeground(new Color(-4397831));
        delWordLabel.setText("Delete an existing word here:");
        delWordPanel.add(delWordLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        delWordButton = new JButton();
        delWordButton.setBackground(new Color(-9805144));
        delWordButton.setBorderPainted(true);
        delWordButton.setFocusPainted(true);
        delWordButton.setFocusable(true);
        Font delWordButtonFont = this.$$$getFont$$$("Consolas", -1, 16, delWordButton.getFont());
        if (delWordButtonFont != null) delWordButton.setFont(delWordButtonFont);
        delWordButton.setForeground(new Color(-4397831));
        delWordButton.setOpaque(true);
        delWordButton.setRequestFocusEnabled(true);
        delWordButton.setText("Click here to delete a word!");
        delWordPanel.add(delWordButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addWordPanel = new JPanel();
        addWordPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        addWordPanel.setBackground(new Color(-12559704));
        appPanel.add(addWordPanel, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(228, 59), null, 0, false));
        addWordPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-6236)), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        addWordLabel = new JLabel();
        Font addWordLabelFont = this.$$$getFont$$$("Consolas", -1, 18, addWordLabel.getFont());
        if (addWordLabelFont != null) addWordLabel.setFont(addWordLabelFont);
        addWordLabel.setForeground(new Color(-4397831));
        addWordLabel.setHorizontalAlignment(10);
        addWordLabel.setHorizontalTextPosition(11);
        addWordLabel.setText("Add a new word here:");
        addWordLabel.setVerticalAlignment(0);
        addWordLabel.setVerticalTextPosition(0);
        addWordPanel.add(addWordLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addWordButton = new JButton();
        addWordButton.setBackground(new Color(-9805144));
        Font addWordButtonFont = this.$$$getFont$$$("Consolas", -1, 16, addWordButton.getFont());
        if (addWordButtonFont != null) addWordButton.setFont(addWordButtonFont);
        addWordButton.setForeground(new Color(-4397831));
        addWordButton.setText("Click here to add a new word!");
        addWordPanel.add(addWordButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        transSentencePanel = new JPanel();
        transSentencePanel.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        transSentencePanel.setBackground(new Color(-12559704));
        transSentencePanel.setForeground(new Color(-1));
        appPanel.add(transSentencePanel, new GridConstraints(2, 1, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        transSentencePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-6236)), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        transSentenceButton = new JButton();
        transSentenceButton.setBackground(new Color(-9805144));
        transSentenceButton.setEnabled(true);
        Font transSentenceButtonFont = this.$$$getFont$$$("Consolas", -1, 18, transSentenceButton.getFont());
        if (transSentenceButtonFont != null) transSentenceButton.setFont(transSentenceButtonFont);
        transSentenceButton.setForeground(new Color(-4397831));
        transSentenceButton.setInheritsPopupMenu(false);
        transSentenceButton.setText("Translate");
        transSentencePanel.add(transSentenceButton, new GridConstraints(0, 2, 2, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        transSentenceEditorPane = new JEditorPane();
        transSentenceEditorPane.setBackground(new Color(-11711155));
        transSentenceEditorPane.setCaretColor(new Color(-21419));
        transSentenceEditorPane.setDisabledTextColor(new Color(-1));
        Font transSentenceEditorPaneFont = this.$$$getFont$$$("Consolas", -1, 20, transSentenceEditorPane.getFont());
        if (transSentenceEditorPaneFont != null) transSentenceEditorPane.setFont(transSentenceEditorPaneFont);
        transSentenceEditorPane.setForeground(new Color(-4397831));
        transSentenceEditorPane.setSelectedTextColor(new Color(-12559704));
        transSentenceEditorPane.setSelectionColor(new Color(-4397831));
        transSentenceEditorPane.setText("");
        transSentencePanel.add(transSentenceEditorPane, new GridConstraints(0, 1, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        transSentenceLabel = new JLabel();
        transSentenceLabel.setBackground(new Color(-12559704));
        Font transSentenceLabelFont = this.$$$getFont$$$("Consolas", -1, 18, transSentenceLabel.getFont());
        if (transSentenceLabelFont != null) transSentenceLabel.setFont(transSentenceLabelFont);
        transSentenceLabel.setForeground(new Color(-4397831));
        transSentenceLabel.setOpaque(true);
        transSentenceLabel.setText(" Translate a sentence:");
        transSentencePanel.add(transSentenceLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(120, 30), null, 0, false));
        editWordPanel = new JPanel();
        editWordPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        editWordPanel.setBackground(new Color(-12559704));
        appPanel.add(editWordPanel, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(228, 59), null, 0, false));
        editWordPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-6236)), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label1 = new JLabel();
        label1.setBackground(new Color(-11711155));
        Font label1Font = this.$$$getFont$$$("Consolas", -1, 18, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setForeground(new Color(-4397831));
        label1.setText("Edit an existing word:");
        editWordPanel.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        editWordButton = new JButton();
        editWordButton.setBackground(new Color(-9805144));
        Font editWordButtonFont = this.$$$getFont$$$("Consolas", -1, 16, editWordButton.getFont());
        if (editWordButtonFont != null) editWordButton.setFont(editWordButtonFont);
        editWordButton.setForeground(new Color(-4397831));
        editWordButton.setText("Click here to edit a word!");
        editWordPanel.add(editWordButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        appLabel = new JLabel();
        Font appLabelFont = this.$$$getFont$$$("VNI-Vivi", -1, 36, appLabel.getFont());
        if (appLabelFont != null) appLabel.setFont(appLabelFont);
        appLabel.setForeground(new Color(-4397831));
        appLabel.setHorizontalAlignment(2);
        appLabel.setHorizontalTextPosition(2);
        appLabel.setText("Dictionary");
        appLabel.setVerticalAlignment(3);
        appLabel.setVerticalTextPosition(3);
        appPanel.add(appLabel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 120), null, 0, false));
        final Spacer spacer1 = new Spacer();
        appPanel.add(spacer1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, new Dimension(25, -1), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.setBackground(new Color(-12559704));
        appPanel.add(panel1, new GridConstraints(3, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(228, 59), null, 0, false));
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-6236)), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$("Consolas", -1, 18, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setForeground(new Color(-4397831));
        label2.setText("Open minigame:");
        panel1.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        gameButton = new JButton();
        gameButton.setBackground(new Color(-9805144));
        Font gameButtonFont = this.$$$getFont$$$(null, -1, 16, gameButton.getFont());
        if (gameButtonFont != null) gameButton.setFont(gameButtonFont);
        gameButton.setForeground(new Color(-4397831));
        gameButton.setText("Click here to open minigame!");
        panel1.add(gameButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        appPanel.add(spacer2, new GridConstraints(1, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, new Dimension(25, -1), null, 0, false));
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

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return appPanel;
    }

}
