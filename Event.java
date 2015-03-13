import java.io.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.util.TimeZone;



public class Event {

	public static void main(String[] args)
	{
		GregorianCalendar gCal = new GregorianCalendar();
		SimpleDateFormat timeFormat = new SimpleDateFormat

("HHmmss");
		SimpleDateFormat dateFormat = new SimpleDateFormat

("yyyyMMdd");
		TimeZone localTimeZone = gCal.getTimeZone();
		   
		
		final String VERSION = "VERSION:2.0";
		
		String start_date;
		String end_date;
		String start_time;
		String end_time;
		String classification;
	    String priority;
		String location;
		String Summary;
		    

		Scanner in = new Scanner(System.in);
		System.out.println("Create A Calendar Event");
		System.out.println("Enter the name of your event:");
		Summary = in.nextLine();
		System.out.println("Enter event start date in 

'yyyyMMdd' format:");
		start_date = in.nextLine();
		System.out.println("Enter time this event starts in 

'HHmmSS' format ");
		start_time = in.nextLine();
		System.out.println("Enter the date the event ends in 

'yyyyMMdd' format:");
		end_date = in.nextLine();
		System.out.println("Enter time this event ends in 

'HHmmSS' format");
		end_time = in.nextLine();
		System.out.println("Where is the location of this 

event?:");
		location = in.nextLine();
		System.out.println("Is this event public, private, or 

confidential?:");
		classification = in.nextLine();
		classification = classification.toUpperCase();
		System.out.println(classification);
		System.out.println("Enter the priority level of this 

event on a scale of (1-10):");
		priority = in.nextLine();
		in.close();
		
		BufferedWriter os;
		try 
		{
			
			File file = new File("Event.ics");
			os = new BufferedWriter(new FileWriter

(file));
			os.write("BEGIN:VCALENDAR" );
			os.newLine();
	        os.write(VERSION);           
	        os.newLine();
	        os.write("BEGIN:VTIMEZONE");
	        os.newLine();
	        os.write("TZID:" + localTimeZone.getID());
	        os.newLine();
	        os.write("BEGIN:STANDARD");
	        os.newLine();
	        os.write("DTSTART:" + "19470608T020000");
	        os.newLine();
	        os.write("END:STANDARD");
	        os.newLine();
	        os.write("END:VTIMEZONE");
	        os.newLine();
	        os.write("BEGIN:VEVENT");
	        os.newLine();
	        os.write("CREATED:" + dateFormat.format(gCal.getTime

()) +
                "T" + timeFormat.format(gCal.getTime()) + "Z");
	        os.newLine();
	        os.write("CLASS:"+ classification);
	        os.newLine();
	        os.write("PRIORITY:"+priority);
	        os.newLine();
	        os.write("SUMMARY:"+Summary);
	        os.newLine();
	        os.write("DTSTART;TZID=" + localTimeZone.getID() + 

":" + start_date + "T" + start_time);
	        os.newLine();
	        os.write("DTEND;TZID=" + localTimeZone.getID() + ":" 

+ end_date +"T" + end_time);
	        os.newLine();
	        os.write("LOCATION:"+location);
	        os.newLine();
	        os.write("END:VEVENT");
	        os.newLine();
	        os.write("END:VCALENDAR");
	             
	        os.close();

		}
		catch (IOException io)
		{
			System.out.println("File Cannot Be Created");
		}
		

	}
}
