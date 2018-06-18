public class bttToComfortCategoryMapper {
    public static String mapCategory(String category) {

        category = category.replaceAll("/", "_");
        if (category.contains("плееры")) {
            return "DVD-плееры";
        } else if (category.contains("Аксессуары")) {
            return "Аксессуары";
        } else {
            return category;
        }
    }
}
