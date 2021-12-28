package src.numbers;

import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Response{
    
    private static final int REQUESTED_NUMBERS_MINIMUM_AMOUNT = 1;
    
    private static final String PROGRAM_TERMINATE_RESPONSE = "\nGoodbye!\n";
    private static final  String INSTRUCTIONS = String.join("\n",
            "Supported requests:",
            " - enter a natural number to know its properties;",
            " - enter two natural numbers to obtain the properties of the list:",
            "  * the first parameter represents a starting number;",
            "  * the second parameter shows how many consecutive numbers are to be processed;",
            " - two natural numbers and a property to search for;",
            " - two natural numbers and two properties to search for;",
            " - a property preceded by minus must not be present in numbers;",
            " - separate the parameters with one space;",
            " - enter 0 to exit.");

    private final String response;

    public Response(final Request request) {
        response = getResponse(request);
    }

    private String getResponse(final Request request) {
        if(request.forProgramTerminate()) {
            return PROGRAM_TERMINATE_RESPONSE;
        }
        if(request.forInstructionsPrint()) {
            return INSTRUCTIONS;
        }

        final RequestErrorDetector detector = new RequestErrorDetector(request);
        if(!detector.isValidRequest()) {
            return detector.getErrorMessage();
        }

        final NumberWithProperties[] numbersWithProperties = getNumbersWithProperties(request);
        final String unformattedResponse = Stream.of(numbersWithProperties)
                .map(nwp -> String.format(Locale.US, "%,d:", nwp.getNumber())
                        + String.join(", ", nwp.getProperties()))
                .collect(Collectors.joining("\n"));

        return formatter(unformattedResponse, request.isChainedRequest());
    }

    private NumberWithProperties[] getNumbersWithProperties(final Request request) {
        final long startNumber = Long.parseLong(request.getStartNumberSection());
        final long numberAmount = request.isChainedRequest()
                ? Long.parseLong(request.getNumbersAmountSection()) : REQUESTED_NUMBERS_MINIMUM_AMOUNT;

        final String[] numbersProperties = request.getNumbersPropertiesSection().split(" ");
        final String[] assertedProperties = Stream.of(numbersProperties)
                .filter(np -> !np.contains("-"))
                .toArray(String[]::new);
        final String[] negatedProperties = Stream.of(numbersProperties)
                .filter(np -> np.contains("-"))
                .map(np -> np.substring(1))
                .toArray(String[]::new);

        return Stream.iterate(startNumber, i -> i + 1)
                .map(NumberWithProperties::new)
                .filter(nwp -> Stream.of(assertedProperties).allMatch(ap -> String.join("", nwp.getProperties()).contains(ap))
                        && Stream.of(negatedProperties).noneMatch(np -> String.join("", nwp.getProperties()).contains(np)))
                .limit(numberAmount)
                .toArray(NumberWithProperties[]::new);
    }

    private String formatter(final String properties, final boolean isChainedRequest) {
        return  isChainedRequest
                ? properties.replaceAll("(.+):", "\t$1 is ")
                : "Properties of " + properties.substring(0, properties.indexOf(':'))
                + Stream.of(Properties.AVAILABLE_PROPERTIES)
                .map(ap -> " ".repeat(12 - ap.length()) + ap + ": " + properties.contains(ap))
                .collect(Collectors.joining("\n", "\n", ""));
    }

    @Override
    public String toString() {
        return response.hashCode() == PROGRAM_TERMINATE_RESPONSE.hashCode()
                && response.equals(PROGRAM_TERMINATE_RESPONSE)
                ? PROGRAM_TERMINATE_RESPONSE : '\n' + response + "\n\nEnter a request: ";
    }

}
