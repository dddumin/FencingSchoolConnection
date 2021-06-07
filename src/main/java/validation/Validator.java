package validation;

public class Validator {
    public static boolean validateTime(String time){
        if (time.length() != 5)
            return false;
        String[] split = time.split(":");
        if (split.length != 2)
            return false;
        try {
            int hour = Integer.parseInt(split[0]);
            int minute = Integer.parseInt(split[1]);
            if (hour < 0 || hour > 24 || minute < 0 || minute > 59)
                return false;
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
