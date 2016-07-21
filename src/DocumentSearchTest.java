import junit.framework.TestCase;
import org.junit.Assert;

import java.io.*;

/**
 * Unit Test for Document Search
 */
public class DocumentSearchTest extends TestCase {

    public void testStringMatchSearch() throws Exception {
        int searchCount;
        DocumentSearch ds = new DocumentSearch();
        //test that word searches do not depend on letter cases
        searchCount = ds.stringMatchSearch("This", new File("test_1.txt"));
        Assert.assertEquals(48, searchCount);
        searchCount = ds.stringMatchSearch("this", new File("test_1.txt"));
        Assert.assertEquals(48, searchCount);

        //test that phrase searches do not depend on letter cases
        searchCount = ds.stringMatchSearch("multiple Lines of", new File("test_1.txt"));
        Assert.assertEquals(48, searchCount);
        searchCount = ds.stringMatchSearch("MULTIPLE lines OF", new File("test_1.txt"));
        Assert.assertEquals(48, searchCount);

        //same word multiple times on the same line
        searchCount = ds.stringMatchSearch("arsenal", new File("test_2.txt"));
        Assert.assertEquals(28, searchCount);

    }

    public void testIndexedSearch() throws Exception {
        int searchCount;
        DocumentSearch ds = new DocumentSearch();
        //test that word searches do not depend on letter cases
        searchCount = ds.indexedSearch("This", new File("test_1.txt"));
        Assert.assertEquals(48, searchCount);
        searchCount = ds.indexedSearch("this", new File("test_1.txt"));
        Assert.assertEquals(48, searchCount);

        //same word multiple times on the same line
        searchCount = ds.indexedSearch("arsenal", new File("test_2.txt"));
        Assert.assertEquals(28, searchCount);

    }

    public void testSearchTimes() throws Exception {
        long stringSearchTime;
        long indexedSearchTime;
        DocumentSearch ds1 = new DocumentSearch();
        DocumentSearch ds2 = new DocumentSearch();

        long startTime1 = System.nanoTime();
        ds1.stringMatchSearch("the", new File("test_3.txt"));
        stringSearchTime = System.nanoTime() - startTime1;


        long startTime2 = System.nanoTime();
        ds2.indexedSearch("the", new File("test_3.txt"));
        indexedSearchTime = System.nanoTime() - startTime2;


        Assert.assertTrue(stringSearchTime < indexedSearchTime);
    }

    public void testStress() throws Exception {
        long stringSearchTime;
        long indexedSearchTime;
        String stringLine;
        String[] searchWords;
        DocumentSearch ds = new DocumentSearch();

        FileInputStream fstream = new FileInputStream("test_3.txt");
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        long startTime1 = System.nanoTime();
        while ((stringLine = br.readLine()) != null) {
            searchWords = stringLine.split(" ");
            for (String word: searchWords) {
                 ds.stringMatchSearch(word, new File("test_3.txt"));
            }
        }
        stringSearchTime = System.nanoTime() - startTime1;

        long startTime2 = System.nanoTime();
        while ((stringLine = br.readLine()) != null) {
            searchWords = stringLine.split(" ");
            for (String word: searchWords) {
                ds.indexedSearch(word, new File("test_3.txt"));
            }
        }
        indexedSearchTime = System.nanoTime() - startTime2;

        Assert.assertTrue(stringSearchTime < indexedSearchTime);
    }
}