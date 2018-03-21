package nor.example.leica

import griffon.javafx.JavaFXGriffonApplication

public class Launcher {

    //todo remove this line in prod
    static {
        System.setProperty("griffon.full.stacktrace", "true")
        System.setProperty("logging.level", "debug")
    }

    public static void main(String[] args) throws Exception {
        JavaFXGriffonApplication.main(args)
    }
}