package simpleshop.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class StringUtils {

    private StringUtils(){}

    //region general string util functions

    /**
     * Test if the value is null or empty.
     * @param value the value.
     * @return true if yes.
     */
    public static boolean isNullOrEmpty(String value){
        return value == null || value.length() == 0;
    }

    /**
     * Returns the part of master before the first separator. Returns the whole string if separator is not found.
     * @param master the string.
     * @param separator separator is not included in the return value.
     * @return the part of master before the first separator.
     */
    public static String subStrB4(String master, String  separator){
        if(master == null || master.length() == 0 || separator == null || separator.length() == 0)
            return master;

        int index = master.indexOf(separator);
        if(index == -1)
            return master;

        return master.substring(0, index);
    }

    /**
     * Replace the single quote character with the corresponding html entity.
     * @param src string that could contain single quote.
     * @return replaced string.
     */
    public static String htmlEncodeSingleQuote(String src){
        if(src == null)
            return null;

        return src.replaceAll("'", "&#39;");
    }

    /**
     * Get the substring after the last last until the first fist.
     * @param src source string.
     * @param last start capturing after the last last.
     * @param first stop capturing until the first first.
     * @return substring of src.
     */
    public static String subStrBetweenLastAndFirst(String src, String last, String first){
        int lastIndex = src.lastIndexOf(last);
        if(lastIndex < 0)
            return src;
        int start = lastIndex + last.length();
        int firstIndex = src.indexOf(first, start);
        if(firstIndex < 0)
            return src;
        return src.substring(start, firstIndex);
    }

    /**
     * Get property name from getter or setter name.
     * @param accessorName getter name or setter name.
     * @return property name.
     */
    public static String getPropertyName(String accessorName) {
        if (accessorName == null || accessorName.length() < 3)
            return null;

        int startIndex = 3;
        if(accessorName.startsWith("is") && Character.isUpperCase(accessorName.charAt(2)))
            startIndex = 2;

        return Character.toLowerCase(accessorName.charAt(startIndex)) + accessorName.substring(startIndex + 1);
    }

    public static String firstCharLower(String simpleName) {
        if (simpleName == null)
            return null;

        if (simpleName.length() < 1)
            return simpleName;

        return Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1);
    }

    public static String firstCharUpper(String simpleName) {
        if (simpleName == null)
            return null;

        if (simpleName.length() < 1)
            return simpleName;

        return Character.toUpperCase(simpleName.charAt(0)) + simpleName.substring(1);
    }

    private static final String[] EMPTY_STRING_ARRAY = {};

    public static String[] emptyArray(){
        return EMPTY_STRING_ARRAY;
    }

    public static boolean startWith(String name, String s) {
        return !(name == null || s == null) && name.startsWith(s);
    }

    public static boolean endWith(String name, String s) {
        return !(name == null || s == null) && name.endsWith(s);
    }

    //endregion


    ////////////////////////////////////////////////// quickshop specific string util functions //////////////////////////////////////////////////

    /**
     * Convert a friendly name to a camel name.
     * [convention]friendlyName - friendly name is capitalised words separated by a single space, e.g. Entity Portfolio Id.
     * [convention]camelName - camel name is a name in camel case, e.g. entityPortfolioId.
     * @param friendlyName friend name.
     * @return camel name.
     */
    public static String friendlyNameToCamelName(String friendlyName){

        if(isNullOrEmpty(friendlyName))
            return friendlyName;

        friendlyName = friendlyName.trim();
        if(friendlyName.length() == 0)
            return "";

        return friendlyName.substring(0,1).toLowerCase() + friendlyName.substring(1).replaceAll("\\s+", "");
    }

    /**
     * Convert camel name back to friendly name.
     * @param camelName camel name, e.f. firstDayOfMonth.
     * @return First Day Of Month.
     */
    public static String camelNameToFriendlyName(String camelName){
        if(isNullOrEmpty(camelName))
            return camelName;

        StringBuilder stringBuilder = new StringBuilder();

        for(int i=0; i<camelName.length();i++){
            char ch = camelName.charAt(i);
            if(i == 0){
                stringBuilder.append(Character.toUpperCase(ch));
                continue;
            }

            if(Character.isUpperCase(ch)){
                stringBuilder.append(' ');
            }
            stringBuilder.append(ch);
        }
        return stringBuilder.toString();
    }

    /**
     * Extract model name in friendly model name format from url.
     * @param url url of a view.
     * @return friendly model name of the view.
     */
    public static String friendlyModelNameFromUrl(String url){
        if(url == null)
            return null;

        int lastSlash = url.lastIndexOf('/');
        int lastDash = url.lastIndexOf('-');
        if(lastDash == -1)
            lastDash = url.length();

        String[] parts = url.substring(lastSlash + 1, lastDash).split("_");
        String result = "";
        for(String part : parts){
            if(part.length() == 0)
                continue;
            if(result.length() > 0)
                result += " ";
            result += part.substring(0, 1).toUpperCase() + part.substring(1);
        }
        return result;
    }

    /**
     * Generate a unique, css friendly html input id from the container id and field path.
     * @param parentId id of the naming container.
     * @param path the path to the field.
     * @return input id/name.
     */
    public static String getFieldId(String parentId, String path){
        return parentId + "-" + path.replaceAll("\\.","_");
    }

    /**
     * Extract view type from the request url.
     * @param url url of a SPA view.
     * [convention]spaView - a SPA view is a JSP page that renders the AngularJS template of a northwind model.The returned HTML fragment can also carry the model in JSON format.
     * [convention]viewType - Currently there are these view types for a northwind object - Details, Create, Update and List.
     * @return the view type.
     */
    public static String viewTypeFromUrl(String url){
        return subStrBetweenLastAndFirst(url, "-", ".");
    }

    /**
     * Get the full view name as is.
     * @param url view url.
     * @return view name.
     */
    public static String viewNameFromUrl(String url){
        return subStrBetweenLastAndFirst(url, "/", ".");
    }

    /**
     * Parse an integer. This is called from el.
     * @param id integer.
     * @return integer.
     */
    public static Integer parseId(String id){
        return new Integer(id);
    }

    /**
     * Convert a camel name to a url name, which is lower case underscore separated.
     * @param camelName camel name.
     * @return url name.
     */
    public static String camelNameToUrlName(String camelName){
        if(isNullOrEmpty(camelName))
            return camelName;

        StringBuilder stringBuilder = new StringBuilder();

        for(int i=0; i<camelName.length();i++){
            char ch = camelName.charAt(i);

            if(Character.isUpperCase(ch)){
                stringBuilder.append('_');
                stringBuilder.append(Character.toLowerCase(ch));
            } else {
                stringBuilder.append(ch);
            }
        }
        return stringBuilder.toString();
    }

    /**
     * Convert from friendly name to pascal name, which is concatenated capitalised words.
     * @param name
     * @return
     */
    public static String friendlyNameToPascalName(String name) {
        return name.replaceAll("\\s+", "");
    }

    public static String wrapLikeKeywords(String param) {
        if(param == null)
            param = "";

        if(!param.startsWith("%"))
            param = "%" + param;

        if(!param.endsWith("%"))
            param += "%";

        return param.replaceAll("-", "");
    }

}
