package com.example;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // Create the main frame
        JFrame jFrame = new JFrame();
        jFrame.setSize(500, 525);
        jFrame.setTitle("Lyrics Chooser");
        jFrame.setLayout(null);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create JComboBox and initialize songs
        JComboBox<Song> comboBox = new JComboBox<>();
        Map<String, String> lyricsMap = readLyricsFromFile("lyrics.txt"); // Read lyrics from the file
        for (String title : lyricsMap.keySet()) {
            comboBox.addItem(new Song(title, lyricsMap.get(title))); // Add songs to combo box
        }

        // Set the default selection to the first item in the combo box
        if (comboBox.getItemCount() > 0) {
            comboBox.setSelectedIndex(0);  // Set the first song as selected
        }

        comboBox.setBounds(150, 50, 200, 30);
        jFrame.add(comboBox);

        // Create a JTextArea to display the lyrics
        JTextArea lyricsTextArea = new JTextArea();
        lyricsTextArea.setBounds(50, 150, 400, 200);
        lyricsTextArea.setLineWrap(false);
        lyricsTextArea.setWrapStyleWord(false);
        lyricsTextArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(lyricsTextArea);
        scrollPane.setBounds(50, 150, 400, 200);

        jFrame.add(scrollPane);

        // Add ActionListener to JComboBox to update lyrics when selection changes
        comboBox.addActionListener(e -> {
            Song selectedSong = (Song) comboBox.getSelectedItem();
            if (selectedSong != null) {
                lyricsTextArea.setText(selectedSong.getLyrics());  // Set the lyrics in JTextArea
            }
        });

        // Display the default song's lyrics upon startup
        lyricsTextArea.setText(((Song) comboBox.getSelectedItem()).getLyrics());

        // Font size panel with radio buttons
        JPanel fontSizePanel = new JPanel();
        fontSizePanel.setLayout(new GridLayout(3, 1)); // 3 rows, 1 column
        fontSizePanel.setBorder(new TitledBorder("Font Size")); // Set titled border

        ButtonGroup fontSizeGroup = new ButtonGroup();
        JRadioButton smallFont = new JRadioButton("Small");
        smallFont.addActionListener(e -> lyricsTextArea.setFont(new Font("Serif", Font.PLAIN, 12))); // Change to small font
        JRadioButton mediumFont = new JRadioButton("Medium", true); // Default selected
        mediumFont.addActionListener(e -> lyricsTextArea.setFont(new Font("Serif", Font.PLAIN, 16))); // Change to medium font
        JRadioButton largeFont = new JRadioButton("Large");
        largeFont.addActionListener(e -> lyricsTextArea.setFont(new Font("Serif", Font.PLAIN, 20))); // Change to large font

        fontSizeGroup.add(smallFont);
        fontSizeGroup.add(mediumFont);
        fontSizeGroup.add(largeFont);

        fontSizePanel.add(smallFont);
        fontSizePanel.add(mediumFont);
        fontSizePanel.add(largeFont);
        fontSizePanel.setBounds(50, 370, 100, 100);
        jFrame.add(fontSizePanel);

        // Font style panel with checkboxes
        JPanel fontStylePanel = new JPanel();
        fontStylePanel.setLayout(new GridLayout(3,1)); // Vertical layout
        fontStylePanel.setBorder(new TitledBorder("Font Style")); // Set titled border

        JCheckBox normalCheckBox = new JCheckBox("Normal");
        JCheckBox boldCheckBox = new JCheckBox("Bold");
        JCheckBox italicCheckBox = new JCheckBox("Italic");

       // Updates the state of font styles
        ActionListener fontStyleListener = e -> updateFontStyle(lyricsTextArea, normalCheckBox.isSelected(), boldCheckBox.isSelected(), italicCheckBox.isSelected());

        normalCheckBox.addActionListener(fontStyleListener);
        boldCheckBox.addActionListener(fontStyleListener);
        italicCheckBox.addActionListener(fontStyleListener);

        fontStylePanel.add(normalCheckBox);
        fontStylePanel.add(boldCheckBox);
        fontStylePanel.add(italicCheckBox);
        fontStylePanel.setBounds(300, 370, 100, 100);
        jFrame.add(fontStylePanel);

        // Make the frame visible
        jFrame.setVisible(true);
    }

    // Read lyrics from the lyrics.txt in resources
    private static Map<String, String> readLyricsFromFile(String fileName) {
        Map<String, String> lyricsMap = new HashMap<>();
        try {

            InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(fileName);
            if (inputStream == null) {
                throw new FileNotFoundException("File " + fileName + " not found in resources.");
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            StringBuilder lyrics = new StringBuilder();
            String currentTitle = null;

            while ((line = br.readLine()) != null) {
                if (line.contains(":")) {
                    // If there's an existing title, save the lyrics
                    if (currentTitle != null) {
                        lyricsMap.put(currentTitle, lyrics.toString().trim());
                    }

                    // Split the title and lyrics
                    String[] parts = line.split(":", 2);
                    currentTitle = parts[0].trim();
                    lyrics = new StringBuilder(parts[1].trim() + "\n"); // Start the lyrics with the content after the title
                } else {
                    // Append the current line of lyrics with a newline character
                    lyrics.append(line).append("\n");
                }
            }

            // Save the last song if any
            if (currentTitle != null) {
                lyricsMap.put(currentTitle, lyrics.toString().trim());
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lyricsMap;
    }



    // Update font style based on checkbox selections
    private static void updateFontStyle(JTextArea textArea, boolean isNormal, boolean isBold, boolean isItalic) {
        int fontStyle = Font.PLAIN;
        if (isBold) {
            fontStyle |= Font.BOLD; // Add bold style if checked
        }
        if (isItalic) {
            fontStyle |= Font.ITALIC; // Add italic style if checked
        }
        textArea.setFont(textArea.getFont().deriveFont(fontStyle)); // Update the font style
    }
}
