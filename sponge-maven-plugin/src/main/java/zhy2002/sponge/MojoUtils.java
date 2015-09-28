package zhy2002.sponge;

import simpleshop.common.StringUtils;

import java.io.*;
import java.util.*;

/**
 * Utility classes for the mojos.
 */
public class MojoUtils {

    public  static String readContent(Class<?> mojoClass, String templateFileName) throws IOException {

        InputStream inputStream = null;
        BufferedReader reader = null;

        try {
            inputStream = mojoClass.getClassLoader().getResourceAsStream(templateFileName);
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder out = new StringBuilder();
            String newLine = System.getProperty("line.separator");
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
                out.append(newLine);
            }
            return out.toString();
        }finally {
            if(reader != null){
                reader.close();
            }
            if(inputStream != null){
                inputStream.close();
            }
        }
    }

    public static File[] getFilesInDirectory(String dir){
        File directory = new File(dir);
        return directory.listFiles();
    }

    public static List<File> getFilesUnderDirectory(String directory, String requiredExtension, String[] excludedSubDirectories) {
        ArrayList<File> result = new ArrayList<>();
        if(excludedSubDirectories == null)
            excludedSubDirectories = StringUtils.emptyArray();
        getFilesUnderDirectory(result, directory, requiredExtension, new HashSet<>(Arrays.asList(excludedSubDirectories)));
        return result;
    }


    public static void getFilesUnderDirectory(List<File> result, String directory, String requiredExtension, Set<String> excludedSubDirectories) {

        File[] files = getFilesInDirectory(directory);
        for(File file : files) {
            if(file.isDirectory()){

                if(!excludedSubDirectories.contains(file.getName())){
                    getFilesUnderDirectory(result, file.getAbsolutePath(), requiredExtension, excludedSubDirectories);
                }

            } else if(file.getName().endsWith(requiredExtension)) {
                result.add(file);
            }
        }
    }


}
