/*
 * Copyright (c) 2022 Bondo Pangaji
 *
 * This software is released under the MIT License.
 * https://opensource.org/licenses/MIT
 */

package io.bonan.notes.util;

/**
 * @author Bondo Pangaji
 */
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Bondo Pangaji
 */
public class DateUtil {
    // Method to generate custom date
    public static String dateNow() { // example format: Sat, 23 July 2022 at 9:19:05 AM{
        Date now = new Date();

        Format dateFormat = new SimpleDateFormat("dd, MMMM yyyy", Locale.ENGLISH);
        Format timeFormat = new SimpleDateFormat("hh:mm:ss a", Locale.ENGLISH);

        String date = dateFormat.format(now);
        String time = timeFormat.format(now);

        return date + " at " + time;
    }
}
