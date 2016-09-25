package jaxp.sax;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Shubo on 9/25/2016.
 */
public class POIreaderTest {
    private File file = new File("C://fdu.xlsx");

    @Test
    public void parseExcelData() throws Exception {

        POIreader poiReader = new POIreader();
        InputStream inputStream = new FileInputStream(file);
        List<ArrayList<String>> lists = poiReader.parseExcelData(inputStream);
        System.out.println(lists.size());
    }

}