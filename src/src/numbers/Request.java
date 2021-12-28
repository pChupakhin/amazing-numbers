package src.numbers;

import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Request {

    public static final String INSTRUCTIONS_PRINT_REQUEST = "";
    public static final String PROGRAM_TERMINATE_REQUEST = "0";

    final String startNumberSection;
    final String numbersAmountSection;
    final String numbersPropertiesSection;

    public Request(final String userInput) {
        final Scanner scanner = new Scanner(userInput).useDelimiter(" ");
        startNumberSection = scanner.hasNext() ? scanner.next() : "";
        numbersAmountSection = scanner.hasNext() ? scanner.next() : "";
        numbersPropertiesSection = scanner.hasNext()
                ? removeDuplicatedProperties(scanner.nextLine().substring(1).toLowerCase()) : "";
        scanner.close();
    }

    public boolean isChainedRequest() {
        return !numbersAmountSection.isEmpty();
    }

    public boolean forProgramTerminate() {
        return !isChainedRequest() &&
                startNumberSection.hashCode() == PROGRAM_TERMINATE_REQUEST.hashCode()
                && startNumberSection.equals(PROGRAM_TERMINATE_REQUEST);
    }

    public boolean forInstructionsPrint() {
        return startNumberSection.hashCode() == INSTRUCTIONS_PRINT_REQUEST.hashCode()
                && startNumberSection.equals(INSTRUCTIONS_PRINT_REQUEST);
    }

    public String getStartNumberSection () {
        return startNumberSection;
    }

    public String getNumbersAmountSection () {
        return numbersAmountSection;
    }

    public String getNumbersPropertiesSection () {
        return numbersPropertiesSection;
    }

    private String removeDuplicatedProperties(final String propertiesTemporaryContainer) {
        return Stream.of(propertiesTemporaryContainer.split(" "))
                .distinct()
                .collect(Collectors.joining(" "));
    }

}
