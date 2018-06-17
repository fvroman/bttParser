import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        bttParser parser = new bttParser();
        Elements subcategories = parser.getSubcategories();
        List<String> subcategoryLinks = parser.getSubcategoryLinks(subcategories);
        Elements parsedElements = new Elements();
        for (String link : subcategoryLinks.subList(0,1)) {
           Elements parsedSubcategory = parser.parseSubcategory(link);
            parsedElements.addAll(parsedSubcategory);
        }

        for (Element parsedElement : parsedElements) {
           // System.out.println(parsedElement);
            Item item = ElementToItemConverter.convertElement(parsedElement);

            //System.out.println(item);
            //System.out.println(SqlProducer.produceSqlQuery(item));
            String query = SqlProducer.produceSqlQuery(item);
            SqlProducer.writeSqlToFile(query, item.getSubcategory());

        }
      //  System.out.println(parsedElements.size());

    }

    public void downloadImage() {

    }
}
