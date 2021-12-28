package src.numbers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class RequestErrorDetector {

    private static final String NATURAL_NUMBER_REGEX = "[1-9]\\d*";
    private static final String ACCEPTABLE_PROPERTIES_REGEX = Stream.of(Properties.AVAILABLE_PROPERTIES)
            .collect(Collectors.joining(")|(?:", "(?:(?> |^)-?(?>(?:", ")))*"));

    private static final String[] ERROR_MESSAGES  = new String[] {
            "The %s parameter should be a natural number%s.",
            "The property [%s] is wrong.\nAvailable properties: " + Arrays.toString(Properties.AVAILABLE_PROPERTIES).toUpperCase(),
            "The properties [%s] are wrong.\nAvailable properties: " + Arrays.toString(Properties.AVAILABLE_PROPERTIES).toUpperCase(),
            "The request contains mutually exclusive properties: [%s]\nThere are no numbers with these properties."};

    private final String errorMessage;

    public RequestErrorDetector(final Request request) {
        final String startNumber = request.getStartNumberSection();
        final String numbersAmount = request.getNumbersAmountSection();
        final String numberProperties = request.getNumbersPropertiesSection();

        if (!startNumber.matches(NATURAL_NUMBER_REGEX)) {
            errorMessage = String.format(ERROR_MESSAGES[errorMessageIndex.PARAMETER], "first", " or zero");
        } else if (!numbersAmount.isEmpty() && !numbersAmount.matches(NATURAL_NUMBER_REGEX)) {
            errorMessage = String.format(ERROR_MESSAGES[errorMessageIndex.PARAMETER], "second", "");
        } else if (numberProperties.isEmpty()) {
            errorMessage = "";
        } else if (!numberProperties.matches(ACCEPTABLE_PROPERTIES_REGEX)) {
            final String[] incorrectProperties = getIncorrectProperties(numberProperties);
            final boolean hasOneWrongProperty = incorrectProperties.length == 1;
            final int currentErrorMessageIndex = hasOneWrongProperty
                    ? errorMessageIndex.PROPERTY : errorMessageIndex.PROPERTIES;

            errorMessage= String.format(ERROR_MESSAGES[currentErrorMessageIndex], String.join(" ", incorrectProperties));
        } else {
            final var propertiesList = List.of(numberProperties.split(" "));
            final String requestedMutuallyExclusiveProperties = getMutuallyExclusiveProperties(propertiesList);

            errorMessage = requestedMutuallyExclusiveProperties.isEmpty()
                    ? "" : String.format(ERROR_MESSAGES[errorMessageIndex.MUTUALLY_EXCLUSIVE_PROPERTIES],
                    requestedMutuallyExclusiveProperties);
        }
    }

    private String[] getIncorrectProperties(final String numberProperties) {
        return Stream.of(numberProperties.split(" "))
                .filter(np -> Stream.of(Properties.AVAILABLE_PROPERTIES).noneMatch(ap -> np.matches("-?" + ap)))
                .toArray(String[]::new);
    }

    private String getMutuallyExclusiveProperties(List<String> numberProperties) {
        final var assertedProperties= numberProperties.stream().filter(np -> !np.startsWith("-")).toList();
        final var negatedProperties = numberProperties.stream().filter(np -> np.startsWith("-")).toList();

        final String polarisedProperties = negatedProperties.stream()
                .filter(np -> String.join("", assertedProperties).contains(np.replaceFirst("-", "")))
                .map(np -> np.replaceFirst("-", "") + " & " + np)
                .collect(Collectors.joining(", "));
        final String assertedMutuallyExclusiveProperties = Stream.of(Properties.MUTUALLY_EXCLUSIVE_PROPERTIES)
                .filter(mep -> assertedProperties.contains(mep[0]) && assertedProperties.contains(mep[1]))
                .map(mep -> mep[0] + " & " + mep[1])
                .collect(Collectors.joining(", "));
        final String negatedMutuallyExclusiveProperties = Stream.of(Properties.MUTUALLY_EXCLUSIVE_PROPERTIES)
                .filter(mep -> negatedProperties.contains('-' + mep[0]) && negatedProperties.contains('-' + mep[1]))
                .map(mep -> '-' + mep[0] + " & -"  + mep[1])
                .collect(Collectors.joining(", "));

        return (polarisedProperties + ", "
                + assertedMutuallyExclusiveProperties + ", "
                + negatedMutuallyExclusiveProperties)
                .replaceFirst("(?:, )*$", "").replaceFirst("^(?:, )*", "");
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isValidRequest() {
        return errorMessage.isEmpty();
    }

    private static class errorMessageIndex {
        private final static int PARAMETER = 0, PROPERTY = 1, PROPERTIES= 2, MUTUALLY_EXCLUSIVE_PROPERTIES = 3;
    }

}
