import java.util.List;

public class Item {

    private String title;
    private String ImageLink;
    private String subcategory;
    private List<String> features;
    private String getImageLink;

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

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }

    public String getGetImageLink() {
        return getImageLink;
    }

    public void setGetImageLink(String getImageLink) {
        this.getImageLink = getImageLink;
    }
}
