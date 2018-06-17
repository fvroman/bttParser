import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;

class ElementToItemConverter {
     static Item convertElement(Element element) {
         Item item = new Item();
         String title = element.selectFirst("h1").text();
         item.setTitle(title);
         //Характеристики
         Elements featureTable = element.getElementsByClass("item_table");
         Elements tableRows = featureTable.select("tr");
         for (Element tableRow : tableRows) {
             String key = tableRow.selectFirst("th").text();
             String value = tableRow.selectFirst("td").text();
             item.addFeature(key, value);
         }
         String subcategory = element.select("a").text();
         item.setSubcategory(subcategory);

         //Подгружаем фото в соотв. папку
         //item.setImageLink();
        return item;
    }

    static void downloadImage(String url, String title, String subcategory) {
        Connection.Response resultImageResponse = Jsoup.connect(imageLocation).cookies(null)
                .ignoreContentType(true).execute();

        FileOutputStream out = (new FileOutputStream(new java.io.File(outputFolder + name)));
        try {
            out.write(resultImageResponse.bodyAsBytes());  // resultImageResponse.body() is where the image's contents are.
            out.close();
        } catch (IOException exception) {
            System.out.println("Can't download image" + url);
        }

    }
}
