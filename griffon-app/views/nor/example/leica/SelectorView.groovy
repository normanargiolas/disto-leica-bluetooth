package nor.example.leica

/**
 * Created by NormaN on 21/03/18.
 *
 **/


enum SelectorView {
    CONNECT_BTN('connectBtn'),
    COMMAND_BTN('commandBtn'),
    OUTPUT_TXTAREA('outputTxtarea')

    SelectorView(String value) {
        this.value = value
    }
    private final String value

    String getValue() {
        value
    }

    String getSelector() {
        '#' + value
    }

}