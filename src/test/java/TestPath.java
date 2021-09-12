public class TestPath {
    public static String realName(String filePath) {
        String tempFileName = filePath.substring(filePath.lastIndexOf("\\") + 1);
        String filestage_FileName = tempFileName.substring(tempFileName.indexOf("_") + 1);
        return filestage_FileName.substring(filestage_FileName.indexOf("_") + 1);
    }
}
