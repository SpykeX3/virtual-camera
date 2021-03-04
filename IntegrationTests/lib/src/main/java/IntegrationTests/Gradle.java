package IntegrationTests;

public class Gradle {
    private static Boolean isWin;

    public static String gradle() {
        return isWindows() ? "gradlew.bat" : "gradlew";
    }

    private static boolean isWindows() {
        if (isWin == null) {
            isWin = System.getProperty("os.name").startsWith("Windows");
        }
        return isWin;
    }
}
