import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class SqlProducer {
    public static String produceSqlQuery(Item item) {
        String query = "INSERT INTO PRODUCTS (PRODUCT_NAME, PRICE, SUBCATEGORY, IMAGE_LINK) VALUES ( " + "'"+item.getTitle() + "', " +  item.getPrice()
                + ", " + "'"+ item.getSubcategory() + "', " +  "'" +item.getImageLink() + "'); \n";

        for (Map.Entry<String, String> entry : item.getFeatures().entrySet()) {
           query +="INSERT INTO FEATURES (TITLE,VAL,PRODUCT_ID) VALUES ("
                   + "'" +entry.getKey() +"', " + "'"+ entry.getValue() + "', " + "(SELECT MAX(PRODUCT_ID) FROM PRODUCTS));\n";
        }

        return query;
    }

    public static void writeSqlToFile(String query, String category) {

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("C:\\ComfortScripts\\"+category+".sql", true),  "utf-8"))) {
            writer.write(query);
        } catch (IOException exception) {
            System.out.println("Couldn't write sql to file");
        }
    }
}
