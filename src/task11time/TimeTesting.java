package task11time;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Demo of Date/Time API handling of DST (daylight safe time) with the help of ZonedDateTime.
 *
 * @author Dmitry Matrizaev
 * @since 1.0
 */
public class TimeTesting {

    public static void main(String[] args) {
        LocalDateTime beforeDST = LocalDateTime.of(2020, 3, 29, 1, 55);
        assert (beforeDST.toString().equals("2020-03-29T01:55"));

        ZoneId frenchZoneId = ZoneId.of("Europe/Paris"); //DST changes on 29.03.2020 at 02:00
        ZonedDateTime zonedBeforeDST = beforeDST.atZone(frenchZoneId);
        assert (zonedBeforeDST.toString().equals("2020-03-29T01:55+01:00[Europe/Paris]"));

        ZonedDateTime zonedAfterDST = zonedBeforeDST.plusMinutes(10); // adding 10 minutes
        assert (zonedAfterDST.toString().equals("2020-03-29T03:05+02:00[Europe/Paris]")); // DST has changed

        long deltaInMinutes = ChronoUnit.MINUTES.between(zonedBeforeDST, zonedAfterDST); // delta is still 10 minutes
        assert (deltaInMinutes == 10);
    }

}
