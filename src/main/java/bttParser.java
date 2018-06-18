import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.print.Doc;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class bttParser {
    private Document document;

    //todo РБТ или Ситилинк, компьютеры + телефоны
    private static final List neededCategories = Arrays.asList("Телевизоры", "DVD-плееры", "Домашние кинотеатры",
            "Музыкальные центры", "Магнитолы", "Холодильники" ,"Кухонные плиты", "Посудомоечные машины" ,"Вытяжки", "Микроволновые печи",
            "Мультиварки" ,"Встраиевамая техника", "Стиральные машины", "Пылесосы", "Кондиционеры", "Обогреватели", "Водонагреватели",
            "Швейные машины", ",Системные блоки", "Ноутбуки" , "Мониторы Принтеры Сканеры" ,"Комплекты клавиатура / мышь", "Смартфоны",
            "Планшеты" , "Фото и видео", "Гаджеты", "Электронные книги", "Зернодробилки", "Инкубаторы", "Насосы", "Сепараторы", "Маслобойки");
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
        //убираем что не нужно
        Elements toRemove = new Elements();
        for (Element element : elements) {
            String title = element.selectFirst("a").text().replaceAll("[0-9]", "").trim();
            if (!neededCategories.contains(bttToComfortCategoryMapper.mapCategory(title))){
                toRemove.add(element);
            }
        }
        elements.removeAll(toRemove);

        return elements;

    }

    public List<String> getSubcategoryLinks(Elements elements) {
        List<String> links = new ArrayList<String>();
        for (Element element : elements) {
            Element link = element.select("a").first();
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
        Elements itemsLight = subcategory.select("section").select("article");
        for (int i = 0; i < itemsLight.size(); i += 2) {
            Element itemLight = itemsLight.get(i);

            Element refContainer = itemLight.selectFirst("h3");
            String itemLink = refContainer.selectFirst("a").attr("abs:href");
            Element image = itemLight.selectFirst("img");
            Element item = new Element("item");
            Document itemFull;

            try {
                itemFull = Jsoup.connect(itemLink).get();
                System.out.println("Parsing :" + refContainer.text());
                //уцененное нафиг
                if (refContainer.text().contains("Уценка")) {
                    continue;
                }
                Element title = itemFull.select("h1").last();
                Element price = itemFull.getElementsByClass("price price-item").first();
                Element features = itemFull.getElementsByClass("item_table").first();
                Element category = itemFull.getElementsByClass("catalog_item__category").first();

                item.appendChild(title);
                if (features != null) {
                    item.appendChild(features);
                }
                item.appendChild(price);
                item.appendChild(category);
                item.appendChild(image);

                items.add(item);

            } catch (IOException exception) {
                System.out.println("Can't parse" + refContainer.text());
            }
        }
        //по всем страницам
        Element paginator = subcategory.getElementsByClass("paginator paginator-underline").first();
        if (paginator != null) {
            String nextPageLink;
            try {
                nextPageLink =
                        paginator.getElementsByClass("paginator__next").first().selectFirst("a").attr("abs:href");
                         items.addAll(parseSubcategory(nextPageLink));

            } catch (NullPointerException e) {
                System.out.println("Category's been parsed");
            }
        }
        return items;
    }
}
