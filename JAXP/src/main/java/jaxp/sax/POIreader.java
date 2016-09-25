package jaxp.sax;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shubo on 9/25/2016.
 */
public class POIreader {

    /*
   * This method parses uploaded excel file which is too large for XSSFWorkbook to process.
   * We deal it with SAX parser
   * */
    protected List<ArrayList<String>> parseExcelData(InputStream in) throws Exception {
        OPCPackage pkg = OPCPackage.open(in);
        XSSFReader r = new XSSFReader(pkg);
        SharedStringsTable sst = r.getSharedStringsTable();
        XMLReader parser = fetchSheetParser(sst);
        XSSFReader.SheetIterator sheets = (XSSFReader.SheetIterator) r.getSheetsData();

        while (sheets.hasNext()) {
            InputStream sheet = sheets.next();
            InputSource sheetSource = new InputSource(sheet);
            parser.parse(sheetSource);
            sheet.close();
            break; // Only need to process one sheet.
        }
        return SheetHandler.getRawDatas();
    }

    private XMLReader fetchSheetParser(SharedStringsTable sst) throws SAXException {
        XMLReader parser =
                XMLReaderFactory.createXMLReader();
        ContentHandler handler = new SheetHandler(sst);
        parser.setContentHandler(handler);
        return parser;
    }

    private static class SheetHandler extends DefaultHandler {

        private SharedStringsTable sst;
        private String lastContents;
        private boolean nextIsString;
        private boolean nextIsInlineString;
        private boolean nextIsNull;
        private ArrayList<String> rowData = new ArrayList<>();
        private static List<ArrayList<String>> rawDatas = new ArrayList<ArrayList<String>>();

        private int rowNo = 0;

        private SheetHandler(SharedStringsTable sst) {
            this.sst = sst;
            rawDatas = new ArrayList<ArrayList<String>>();
        }

        public static List<ArrayList<String>> getRawDatas() {
            return rawDatas;
        }

        @Override
        public void startElement(String uri, String localName, String name,
                                 Attributes attributes) throws SAXException {

            if (name.equals("c")) {
                String cellType = attributes.getValue("t");
                if (cellType == null) {
                    nextIsNull = true;
                }
                if (cellType != null && cellType.equals("s")) {
                    nextIsString = true;
                } else {
                    nextIsString = false;
                }

                if (cellType != null && cellType.equals("inlineStr")) {
                    nextIsInlineString = true;
                } else {
                    nextIsInlineString = false;
                }
            }
            // Clear contents cache
            lastContents = "";
        }

        @Override
        public void endElement(String uri, String localName, String name)
                throws SAXException {

            if (nextIsString) {
                int idx = Integer.parseInt(lastContents);
                lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
                nextIsString = false;
            }

            if (nextIsInlineString) {
                rowData.add(lastContents);
                nextIsInlineString = false;
            }

            if (name.equals("v") && !nextIsNull) {
                rowData.add(lastContents);
            }

            if (nextIsNull) {
                //lastContents = null;
                rowData.add(lastContents);
                nextIsNull = false;
            }

            if (name.equals("row") && (null != rowData.get(0)) && (!"".equals(rowData.get(0)))) {
                if (rowNo > 0)
                    rawDatas.add(rowData);
                rowData = new ArrayList<>();
                rowNo++;
            }
        }

        @Override
        public void characters(char[] ch, int start, int length)
                throws SAXException {
            lastContents += new String(ch, start, length);
        }
    }

}
