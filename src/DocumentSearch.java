import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Search a document to show how many times a phrase or word appears. Three different search
 * options, a regular string search, a regular expression search, or a search that indexes the
 * files and then searches the index.
 */
public class DocumentSearch extends JDialog {
    private JPanel contentPane;
    private JTextField textField1;
    private JComboBox comboBox1;
    private JButton searchButton;
    private JButton clearButton;
    private JTextArea textArea1;
    public static final List<String> FILE_LIST = Collections.unmodifiableList(
            new ArrayList<String>() {{
                add("french_armed_forces.txt");
                add("hitchhikers.txt");
                add("warp_drive.txt");
            }});


    public static long mTimeElapsed;

    public static String mSearchPhrase;

    public DocumentSearch() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(searchButton);

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call dispose() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        // call dispose() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    /**
     * Determines which search type was chosen by the user
     *
     * @return a SearchType to determine which search method to use
     */
    private SearchType getSearchType() {
        SearchType type;
        switch (comboBox1.getSelectedIndex()) {
            case 1:
                type = SearchType.STRING_SEARCH;
                break;
            case 2:
                type = SearchType.REGULAR_EXPRESSION_SEARCH;
                break;
            case 3:
                type = SearchType.INDEXED_SEARCH;
                break;
            default:
                type = SearchType.NONE;
                break;
        }
        return type;
    }

    /**
     * searches the file given for the text given and returns the number of occurrences
     * Uses a simple string match to return results
     *
     * @param textToSearch the text to search for
     * @param fileToSearch the file that will be searched
     *
     * @return the number of times the text is found in the file
     */
    private int stringMatchSearch(String textToSearch, File fileToSearch) throws IOException {
        int matches = 0;
        String stringLine;

        FileInputStream fstream = new FileInputStream(fileToSearch);
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        while ((stringLine = br.readLine()) != null) {
            String[] words = stringLine.split(" ");
            for (String text : words) {
                if (text.equalsIgnoreCase(textToSearch)) {
                    matches++;
                }
            }
        }

        return matches;
    }

    /**
     * searches the file given for the text given and returns the number of occurrences
     * Will pre-process the file and then use an index
     *
     * @param textToSearch the text to search for
     * @param fileToSearch the file that will be searched
     *
     * @return the number of times the text is found in the file
     */
    private int indexedSearch(String textToSearch, File fileToSearch) throws IOException{

        Map<String, Integer> countByWords = new HashMap<String, Integer>();
        Scanner s = new Scanner(fileToSearch);
        while (s.hasNext()) {
            String next = s.next();
            next = next.toUpperCase();
            if (countByWords.containsKey(next)) {
                countByWords.put(next, countByWords.get(next) + 1);
            } else {
                countByWords.put(next, 1);
            }
        }
        s.close();
        textToSearch = textToSearch.toUpperCase();
        if (countByWords.containsKey(textToSearch)) {
            return countByWords.get(textToSearch);
        } else {
            return 0;
        }
    }

    /**
     * searches the file given for the text given and returns the number of occurrences
     * Uses a text search with regular expressions to return results
     *
     * @param textToSearch the text to search for
     * @param fileToSearch the file that will be searched
     *
     * @return the number of times the text is found in the file
     */
    private int regExSearch(String textToSearch, File fileToSearch) {
        int matches = 0;

        return matches;
    }

    /**
     * called in onCancel to clear any entered information by user
     */
    private void clear() {
        textField1.setText("");
        comboBox1.setSelectedIndex(0);
    }

    /**
     * Prints the results to the text area of the panel
     *
     * @param matchResults a list of number of matches, one for each file that was searched
     */
    private void printResults(List<Integer> matchResults) {
        StringBuilder resultsText = new StringBuilder("Search Results: \n\n");

        for (int i = 0; i<matchResults.size(); i++) {
            String result = "  * " + FILE_LIST.get(i) + " - " + String.valueOf(matchResults.get(i)) +
                    "" + " " + "matches \n";
            resultsText.append(result);
        }

        long durationInMs = TimeUnit.MILLISECONDS.convert(mTimeElapsed, TimeUnit.NANOSECONDS);
        String timeElapsed = "\n Elapsed Time: " + String.valueOf(durationInMs) + " ms";

        resultsText.append(timeElapsed);

        textArea1.setText(resultsText.toString());

    }

    private void onOK() {
        List<Integer> results = new ArrayList<>();
        if (!textField1.getText().isEmpty()) {
            mSearchPhrase = textField1.getText();
        }

        //run timer and search at the same time
        mTimeElapsed = 0;
        long startTime = System.nanoTime();
        switch (getSearchType()) {
            case STRING_SEARCH:
                //perform string search on each file
                try{
                    results.add(stringMatchSearch(mSearchPhrase, new File(FILE_LIST.get(0))));
                    results.add(stringMatchSearch(mSearchPhrase, new File(FILE_LIST.get(1))));
                    results.add(stringMatchSearch(mSearchPhrase, new File(FILE_LIST.get(2))));
                }catch(IOException e){
                    e.printStackTrace();
                }
                break;
            case REGULAR_EXPRESSION_SEARCH:
                //perform regular expression search on each file
                break;
            case INDEXED_SEARCH:
                //perform indexed search on each file
                try{
                    results.add(indexedSearch(mSearchPhrase, new File(FILE_LIST.get(0))));
                    results.add(indexedSearch(mSearchPhrase, new File(FILE_LIST.get(1))));
                    results.add(indexedSearch(mSearchPhrase, new File(FILE_LIST.get(2))));
                }catch(IOException e){
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }

        mTimeElapsed = System.nanoTime() - startTime;
        //print results

        printResults(results);


    }

    private void onCancel() {
        clear();
    }

    public static void main(String[] args) {
        DocumentSearch dialog = new DocumentSearch();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

}
