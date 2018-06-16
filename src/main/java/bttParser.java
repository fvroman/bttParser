import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.print.Doc;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class bttParser {
    private Document document;

    bttParser() {
        try {
            document = Jsoup.connect("http://www.24btt.ru/").get();
            System.out.println("Connected to 24btt.ru ");

        } catch (IOException exception) {
            System.out.println("Couldn't connect to website");
        }
    }


    private Element getCategoryElement() {
        return document.getElementById("catalog_menu");
    }

    public Elements getSubcategories() {
        Element categoryElement = getCategoryElement();
        Elements elements = categoryElement.getElementsByClass("child_sub4");
        elements.addAll(categoryElement.getElementsByClass("drop_list__item"));
        return elements;
    }

    public List<String> getSubcategoryLinks(Elements elements) {
        List<String> links = new ArrayList<String>();
        for (Element element : elements) {
          Element link =  element.select("a").first();
            String absoluteLink = link.attr("abs:href");
            links.add(absoluteLink);
        }
        return links;
    }


    public Elements parseSubcategory(String subcategoryLink) {
        Document subcategory;
        Elements items = new Elements();
        try {
            subcategory = Jsoup.connect(subcategoryLink).get();
            System.out.println("connected to " + subcategoryLink);
        } catch (IOException exception) {
            System.out.println("couldn't access link" + subcategoryLink);
            return null;
        }
        //парсим айтемы в подкатегории
        Elements itemsLight = subcategory.getElementsByClass("catalog_item");
        for (Element itemLight: itemsLight) {

            Element refContainer = itemLight.selectFirst("h3");
            String itemLink = refContainer.select("a").first().attr("abs:href");
            Element image = itemLight.selectFirst("img");
            Element item = new Element("item");
            Document itemFull;

            try {
                itemFull = Jsoup.connect(itemLink).get();
                System.out.println("Parsing :" + refContainer.text());
                Element title = itemFull.select("h1").last();
                Element price = itemFull.getElementsByClass("price price-item").first();
                Element features = itemFull.getElementsByClass("item_table").first();
                Element category = itemFull.getElementsByClass("catalog_item__category").first();

                item.appendChild(title);
                item.appendChild(features);
                item.appendChild(price);
                item.appendChild(category);
                item.appendChild(image);

                items.add(item);

            } catch (IOException exception) {
                System.out.println("Can't parse" + refContainer.ownText());
            }
        }
        return items;
    }
}
