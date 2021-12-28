package src.numbers;

import java.util.function.Supplier;
import java.util.stream.IntStream;

class NumberWithProperties {

    private final long number;
    private final String[] properties;

    public NumberWithProperties(final long number) {
        this.number = number;
        properties = getNumberProperties();
    }

    private String[] getNumberProperties() {
        final var buffer = new StringBuilder(isEven() ? "even" : "odd")
                .append(isBuzz() ? " buzz" : "")
                .append(isDuck() ? " duck" : "")
                .append(isGapful() ? " gapful" : "")
                .append(isPalindromic() ? " palindromic" : "")
                .append(isSpy() ? " spy" : "")
                .append(isSquare() ? " square" : "")
                .append(isSunny() ? " sunny" : "")
                .append(isJumping()? " jumping" : "")
                .append(isHappy() ? " happy" : " sad");
        return buffer.toString().split(" ");
    }

    private boolean isEven(){
        return number % 2 == 0;
    }

    private boolean isBuzz() {
        return number % 7 == 0 || number % 10 == 7;
    }

    private boolean isDuck() {
        return String.valueOf(number).contains("0");
    }

    private boolean isGapful() {
        return number / 100 != 0
                && number % (number / (long)Math.pow(10, (int)Math.log10(number)) * 10 + number % 10) == 0;
    }

    private boolean isPalindromic() {
        final StringBuilder tmp = new StringBuilder(String.valueOf(number));
        return tmp.toString().hashCode() == tmp.reverse().toString().hashCode()
                && tmp.toString().equals(tmp.reverse().toString());
    }

    private boolean isSpy() {
        final Supplier<IntStream> numberDigitsStreamSupplier
                = () -> String.valueOf(number).chars().map(Character::getNumericValue);
        return numberDigitsStreamSupplier.get().sum()
                == numberDigitsStreamSupplier.get().reduce((res, dgd) -> res * dgd).getAsInt();
    }

    private boolean isSquare() {
        return Math.pow((long)Math.sqrt(number), 2) == number;
    }

    private boolean isSunny() {
        return Math.pow((long)Math.sqrt(number + 1), 2) == number + 1;
    }

    private boolean isJumping() {
        return number / 10 == 0 || number % 10
                == String.valueOf(number).chars()
                .map(Character::getNumericValue).
                reduce(11, (res, dgd) -> res = res == -1 ? res : Math.abs(res - dgd) == 1 || res == 11 ? dgd : -1);
    }

    private boolean isHappy() {
        final int HAPPY_NUMBER_RESULT = 1, SAD_NUMBER_RESULT = 4;
        long res = number;
        long tmp;
        while (res != HAPPY_NUMBER_RESULT && res != SAD_NUMBER_RESULT) {
            tmp = res;
            res = 0;
            while (tmp != 0) {
                res += Math.pow(tmp % 10, 2);
                tmp /= 10;
            }
        }
        return res == HAPPY_NUMBER_RESULT;
    }

    public long getNumber() {
        return number;
    }

    public String[] getProperties() {
        return properties;
    }

}
