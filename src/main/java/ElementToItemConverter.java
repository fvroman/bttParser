import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

class ElementToItemConverter {
     static Item convertElement(Element element) {
         Item item = new Item();
         String title = element.selectFirst("h1").text();
         item.setTitle(title);
         //Характеристики
         Elements featureTable = element.getElementsByClass("item_table");
         if (featureTable != null) {
             Elements tableRows = featureTable.select("tr");
             for (Element tableRow : tableRows) {
                 String key = tableRow.selectFirst("th").text();
                 String value = tableRow.selectFirst("td").text();
                 item.addFeature(key, value);
             }
         }

         String subcategory = element.select("a").text();
         item.setSubcategory(bttToComfortCategoryMapper.mapCategory(subcategory));


         int price = Integer.parseInt(element.getElementsByClass("price price-item").first().text().replaceAll("\\D+",""));
         item.setPrice(price);

         String imageLink = downloadImage(element.selectFirst("img").absUrl("src"), title, subcategory);
         //Подгружаем фото в соотв. папку
         item.setImageLink(imageLink);
        return item;
    }

    static String downloadImage(String url, String title, String subcategory) {
        try {
         String outputDirectory = "C:\\ComfortImages\\" +bttToComfortCategoryMapper.mapCategory(subcategory);
            String outputPath = outputDirectory + "\\" + title + ".jpg";
            System.out.println("downloading Image " + url + " to " + outputPath);
        Connection.Response resultImageResponse = Jsoup.connect(url).cookies(new HashMap<String, String>())
                .ignoreContentType(true).execute();
        FileOutputStream out = null;
        try {
            File directory = new File(outputDirectory);
            if (!directory.exists()) {
                directory.mkdir();
            }
            out = (new FileOutputStream(new java.io.File(outputPath)));
        } catch (FileNotFoundException e) {
            System.out.println("path not found");
        }

            out.write(resultImageResponse.bodyAsBytes());  // resultImageResponse.body() is where the image's contents are.
            out.close();
            return bttToComfortCategoryMapper.mapCategory(subcategory)+"/"+title+".jpg";

        } catch (IOException exception) {
            System.out.println("Can't download image" + url);
            return null;
        }

    }
}
