import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.zone.ZoneRules;
import java.util.*;

/**
 * Created by Cyrus on 3/11/2015.
 */
public class Calendar {

    static String filename;
    static String VERSION = "2.0";
    static String CLASS = "PUBLIC";
    static String LOCATION;
    static String PRIORITY = "0";
    static String SUMMARY;
    static String DTSTART;
    static String DTEND;
    static String TIMEZONE;

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException{
        PrintWriter writer;
        Scanner input = new Scanner(System.in);
        
        

        System.out.println("Enter Filename: ");
        filename = input.nextLine();
        File output = new File(filename + ".ics");
        
        StringBuffer prompt = new StringBuffer();
        prompt.append("1: Set Summary\n");
        prompt.append("2: Time\n");
        prompt.append("3: Set Location\n");
        prompt.append("4: Set Priority\n");
        prompt.append("5: Set Classification\n");
        prompt.append("6: Save Calendar Event\n");
        prompt.append("7: Delete Calendar Event\n");

 /*       while (true) {
            System.out.println(prompt.toString());

            //input.nextLine();

            break;
        }*/

        writer = new PrintWriter(output, "UTF-8");
        writer.write("BEGIN: VCALENDAR\n");
        writer.write("VERSION:" + VERSION + "\n");
        writer.write("BEGIN: VEVENT\n");

        System.out.println("Enter CLASSIFICATION\n1: Public\n2: Private\n3: Confidential\nAnything else will default to Public");
        CLASS = input.nextLine();
        int temp;

        try {
            temp = Integer.parseInt(CLASS);
        } catch (NumberFormatException e) {
            temp = 1;
        }

        switch (temp) {
            case 1: CLASS = "PUBLIC";
                    break;
            case 2: CLASS = "PRIVATE";
                    break;
            case 3: CLASS = "CONFIDENTIAL";
                    break;
            default: CLASS = "PUBLIC";
        }
        writer.write("CLASS:" + CLASS + "\n");

        System.out.println("Enter Location: ");
        LOCATION = input.nextLine();
        writer.write("LOCATION:" + LOCATION + "\n");

        System.out.println("Enter Priority: ");

        setDTSTART(writer);
        setDTEND(writer);
        setTIMEZONE(writer);

        writer.write("DTSTART;TZID=" + TIMEZONE + ":" + DTSTART + "\n");
        writer.write("DTEND;TZID=" + TIMEZONE + ":" + DTEND + "\n");
        writer.write("END:VEVENT\n");
        writer.write("END:VCALENDAR\n");
        writer.close();


    }
    static void setTime(Writer writer) {
        setDTSTART(writer);
        setDTEND(writer);
        setTIMEZONE(writer);
    }

    static void setDTSTART(Writer writer) {
        Scanner input = new Scanner(System.in);
        String date;
        String time;

        System.out.println("Enter Start Date: ");
        date = input.nextLine();
        System.out.println("Enter Start Time: ");
        time = input.nextLine();

        DTSTART = date + "T" + time;
    }
    static void setDTEND(Writer writer) {
        Scanner input = new Scanner(System.in);
        String date;
        String time;

        System.out.println("Enter End Date: ");
        date = input.nextLine();
        System.out.println("Enter End Time: ");
        time = input.nextLine();
        DTEND = date + "T" + time;

    }
    static void setTIMEZONE(Writer writer) {
        Scanner input = new Scanner(System.in);
        Set<String> allZones = ZoneId.getAvailableZoneIds();
        List<String> zoneList = new ArrayList<String>(allZones);

        System.out.println("Enter Time Zone: ");
        TIMEZONE = input.nextLine();
        while (!zoneList.contains(TIMEZONE)) {
            System.out.println("Timezone wrong. Try again: ");
            TIMEZONE = input.nextLine();
        }
    }
}
