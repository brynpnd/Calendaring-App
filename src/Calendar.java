import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Calendar {

	public final String TIMEZONE = "TZID=Pacific/Honolulu:";
	
	private Scanner scan;
	
	public static void main(String args[]) {
		
		Calendar c = new Calendar();

	}
	
	public Calendar() {
		
		scan = new Scanner(System.in);
		
		boolean inputValid = false;
		
		while(inputValid == false) {
			System.out.print("Would you like to (c)reate a new .ics file or (f)ind free time between them? ");
			String input = scan.next();
			
			switch(input) {
				case "c": createICSFile(); inputValid = true; break; // create .ics file
				case "f": findFreeTime(); inputValid = true; break; // find free time
				default: System.out.println("Invalid entry.");
			}
		}
		
	}
	
	public void createICSFile() {
		
		String filename, version, classification, location, summary, priority,
		DTSTART, DTEND, timeZone;
		
		classification = new String("");
		priority = new String("");
		
		System.out.print("Enter the file name to be saved (no spaces): ");

		filename = scan.next();
		version = "VERSION:2.0";
		
		boolean inputValid = false;
		while(inputValid == false) {
			System.out.print("Enter a classification - P(u)blic or P(r)ivate: ");
			String a = scan.next();
			
			switch(a) {
				case "u": classification = "CLASS:PUBLIC"; inputValid = true; break;
				case "r": classification = "CLASS:PRIVATE"; inputValid = true; break;
				default: System.out.println("Invalid entry.");
			
			}
		}
		
		inputValid = false;
		while(inputValid == false) {
			System.out.print("Enter a priority number from 1 to 9: ");
			int b = scan.nextInt();
			scan.nextLine();
			
			if(b < 10 && b > 0) {
				priority = "PRIORITY:" + String.valueOf(b);
				inputValid = true;
			}
			else
				System.out.println("Invalid entry.");
		}
		
		System.out.print("Enter a location: ");
		String c = scan.nextLine();
		location = "LOCATION:" + c;
		
		System.out.print("Enter a summary: ");
		String d = scan.nextLine();
		summary = "SUMMARY:" + d;
		
		System.out.print("Enter a start date as YYYYMMDD: ");
		String startDate = scan.next();
		
		System.out.print("Enter an end date as YYYYMMDD: ");
		String endDate = scan.next();
		
		System.out.print("Enter a start time as HHMMSS (24-hour clock): ");
		String startTime = scan.next();
		
		System.out.print("Enter an end time as HHMMSS (24-hour clock): ");
		String endTime = scan.next();

		DTSTART = "DTSTART;" + TIMEZONE + startDate + "T" + startTime;
		DTEND = "DTEND;" + TIMEZONE + endDate + "T" + endTime;

		try {
			PrintWriter out = new PrintWriter(filename + ".ics");	
			out.println("BEGIN:VCALENDAR");
			out.println(version);
			out.println("BEGIN:VEVENT");
			out.println(classification);
			out.println(priority);
			out.println(location);
			out.println(summary);
			out.println(DTSTART);
			out.println(DTEND);
			out.println("END:VEVENT");
			out.println("END:VCALENDAR");
			out.close();
		} catch(Exception e) {
			System.out.println("PrintWriter error");
			System.exit(1);
		}
		
		System.out.println("Complete!");
		
		
	}
	
	public void findFreeTime() {
		
		List<File> files = new ArrayList<File>();

		boolean inputValid = false;
		while(inputValid == false) {
			System.out.println("Enter the pathname of the .ics files you would like to examine, separated by commas (no spaces):");
			String input = scan.next();
			
			// Parse strings by commas
			List<String> fileNames = Arrays.asList(input.split(","));
			File[] f = new File[fileNames.size()];
			
			inputValid = true;			
			// Check if files exist
			for(int i = 0; i < fileNames.size(); i++) {
				
				f[i] = new File(fileNames.get(i));
				
				if(f[i].isFile() == false) {
					inputValid = false;
					System.out.println("File " + fileNames.get(i) + " does not exist!");
				}

			}
			
			if(inputValid == true) {
				for(int i = 0; i < f.length; i++) {
					files.add(f[i]);
				}
			}
			
			// Still have to check .ics files for correctness			

		}
		
		/*** Grab dates and times of all .ics files ***/
		List<String> startDateStrings = new ArrayList<String>();
		List<String> startTimeStrings = new ArrayList<String>();
		List<String> endDateStrings = new ArrayList<String>();
		List<String> endTimeStrings = new ArrayList<String>();
		List<String> fileStrings = new ArrayList<String>();
		
		// Convert file content to String
		for(int i = 0; i < files.size(); i++) {
			File f = files.get(i);
			FileInputStream fis;
			try {
				fis = new FileInputStream(f);
				byte[] data = new byte[(int) f.length()];
				try {
					fis.read(data);
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				try {
					fileStrings.add(new String(data, "UTF-8"));
					System.out.println(fileStrings.get(i));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}			
		}
		
		// Find and separate date and time Strings
		for(int i = 0; i < fileStrings.size(); i++) {
			String currString = fileStrings.get(i);
			
			int index = currString.indexOf(TIMEZONE);
			int start = index;
			while(!Character.isDigit(currString.charAt(start))) 
				start++;

			int end = start;
			while(currString.charAt(end) != '\n')
				end++;
			
			end--;

			startDateStrings.add(currString.substring(start, start+8));
			startTimeStrings.add(currString.substring(start+9,end));
		}
				
		// Find dates and times between .ics files
		
		// Create new .ics files with date and times between .ics files
			

		
	}
		

	
}
