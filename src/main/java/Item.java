import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Item {

    private String title;
    private String ImageLink;
    private String subcategory;
    private LinkedHashMap<String, String> features;

    Item() {
        features = new LinkedHashMap<String, String>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageLink() {
        return ImageLink;
    }

    public void setImageLink(String imageLink) {
        ImageLink = imageLink;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public LinkedHashMap<String, String> getFeatures() {
        return features;
    }

    public void setFeatures(LinkedHashMap<String, String> features) {
        this.features = features;
    }

    public void addFeature(String key, String value) {
        features.put(key, value);
    }

    @Override
    public String toString() {
        return "Item{" +
                "title='" + title + '\'' +
                ", ImageLink='" + ImageLink + '\'' +
                ", subcategory='" + subcategory + '\'' +
                ", features=" + features +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(title, item.title) &&
                Objects.equals(ImageLink, item.ImageLink) &&
                Objects.equals(subcategory, item.subcategory) &&
                Objects.equals(features, item.features);
    }

    @Override
    public int hashCode() {

        return Objects.hash(title, ImageLink, subcategory, features);
    }
}
