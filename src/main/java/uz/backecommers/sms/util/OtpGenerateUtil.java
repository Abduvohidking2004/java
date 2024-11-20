package uz.backecommers.sms.util;

import java.util.Random;
import java.util.stream.Collectors;

public class OtpGenerateUtil {
    public static String generate() {
        return new Random().ints(6, 0, 10)
                .mapToObj(String::valueOf).toList().stream().map(Object::toString).collect(Collectors.joining(""));
    }
}
