import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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


        int price = Integer.parseInt(element.getElementsByClass("price price-item").first().text().replaceAll("\\D+", ""));

        item.setPrice(addPrice(price));

        String imageLink = downloadImage(element.selectFirst("img").absUrl("src"), item);
        //Подгружаем фото в соотв. папку
        item.setImageLink(imageLink);
        return item;
    }

    private static String downloadImage(String url, Item item) {
        try {
            String subcategory = item.getSubcategory();
            String title = item.getTitle();
            String outputDirectory = "C:\\ComfortImages\\" + bttToComfortCategoryMapper.mapCategory(subcategory);
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
            addToDescription(item, outputDirectory);
            return bttToComfortCategoryMapper.mapCategory(subcategory) + "/" + title + ".jpg";

        } catch (IOException exception) {
            System.out.println("Can't download image" + url);
            return null;
        }

    }

    private static void addToDescription(Item item, String categoryFolder) {
        try {
            final Path path = Paths.get(categoryFolder + "/description.txt");
            String keys = "";
            String values = "";
            for (Map.Entry<String, String> feature : item.getFeatures().entrySet()) {
                keys += feature.getKey() + "#";
                values += feature.getValue() + "#";
            }
            String text = item.getTitle() + "\t" + keys + "\t" + values + "\t" + "\"Images\\"+item.getTitle() + ".jpg\"" + "\t" + item.getPrice() + " руб.";
            if (Files.exists(path)) {
                Files.write(path, Arrays.asList(text), StandardCharsets.UTF_8,
                        StandardOpenOption.APPEND);
            } else {
                String header = "Наименование\tХарактеристика\tЗначение\t@Изображение\tЦена";
                Files.write(path, Arrays.asList(header, text), StandardCharsets.UTF_8,
                        StandardOpenOption.CREATE);
            }

        } catch (final IOException ioe) {
            System.out.println("can't write description");
        }

    }

    private static int addPrice(int price){
        int roundedPrice = 0;
        float floatPrice = (float) price;
        if (price < 5000) {
            roundedPrice = (int) Math.round(floatPrice + (floatPrice * 0.3));
            roundedPrice =  ((roundedPrice + 99) / 100 ) * 100;
        } else if (price > 5000 && price<=40000){
            roundedPrice = (int) Math.round(floatPrice + (floatPrice * 0.25));
            roundedPrice =  ((roundedPrice + 99) / 100 ) * 100;
        } else if (price > 40000 && price <=80000) {
            roundedPrice = (int) Math.round(floatPrice + (floatPrice * 0.2));
            roundedPrice =  ((roundedPrice + 99) / 100 ) * 100;
        } else if (price > 80000) {
            roundedPrice = (int) Math.round(floatPrice + (floatPrice * 0.15));
            roundedPrice =  ((roundedPrice + 99) / 100 ) * 100;
        }
        return roundedPrice;

    }


}



