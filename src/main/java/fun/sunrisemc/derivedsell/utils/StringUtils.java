package fun.sunrisemc.derivedsell.utils;

public class StringUtils {

    public static int toInt(String numeral) {
        try {
            return Integer.parseInt(numeral);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static String titleCase(String str) {
        str = str.replace("_", " ");
        String[] words = str.split(" ");
        String titleCase = "";
        for (String word : words) {
            titleCase += word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase() + " ";
        }
        return titleCase.trim();
    }
}