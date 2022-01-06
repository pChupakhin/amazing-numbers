package numbers;

public class Properties {

    public static final String[] AVAILABLE_PROPERTIES
            = {"even", "odd", "buzz", "duck", "jumping", "palindromic", "gapful", "spy", "square", "sunny", "happy", "sad"};
    public static final String[][] MUTUALLY_EXCLUSIVE_PROPERTIES = {{"even", "odd"},
                                                                    {"spy", "duck"},
                                                                    {"happy", "sad"},
                                                                    {"sunny", "square"}};

}
