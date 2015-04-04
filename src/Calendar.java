import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Calendar {

	public final String VERSION = "VERSION:2.0";
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
		
		String filename, classification, location, summary, priority,
		DTSTART, DTEND;
		
		classification = new String("");
		priority = new String("");
		
		System.out.print("Enter the file name to be saved (no spaces): ");

		filename = scan.next();
		
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
		
		printICSFile(filename, VERSION, classification, priority, location, summary, DTSTART, DTEND);
		
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
			
			start = end;
			
			while(!Character.isDigit(currString.charAt(start))) 
				start++;
			
			end = start;
			
			while(currString.charAt(end) != '\n')
				end++;
			
			end--;
			
			endDateStrings.add(currString.substring(start,start+8));
			endTimeStrings.add(currString.substring(start+9,end));
			
		}
				
		/*** Find dates and times between .ics files ***/
		List<Event> events = new ArrayList<Event>();
		
		for(int i = 0; i < startTimeStrings.size(); i++) {
			events.add(new Event(startDateStrings.get(i),endDateStrings.get(i),startTimeStrings.get(i),endTimeStrings.get(i)));
		}
	
		// Sort events by start date
		for(int i = 0; i < events.size(); i++) {
			
			Event currEvent = events.get(i);
			
			for(int j = i+1; j < events.size(); j++) {
				Event nextEvent = events.get(j);
				if(currEvent.getStartDate() > nextEvent.getStartDate()) {
					
					events.set(i, nextEvent);
					events.set(j, currEvent);
					
				}
					
			}	
		}
		
		// Sort events by start time
		for(int i = 0; i < events.size(); i++) {
			
			Event currEvent = events.get(i);
			
			int k = i;
			for(int j = i+1; j < events.size(); j++) {
				Event nextEvent = events.get(j);
				if(currEvent.getStartDate() == nextEvent.getStartDate()) {
					
					if(currEvent.getStartTime() > nextEvent.getStartTime()) {
						events.set(k, nextEvent);
						events.set(j, currEvent);
						k = j;
					}
					
				}
			}
			// if a swap was made
			if(k != i)
				i--;
		}

		System.out.println("Events");
		for(int i = 0; i < events.size(); i++) {
			System.out.println(String.valueOf(events.get(i).getStartDate()));
			System.out.println(String.valueOf(events.get(i).getStartTime()));
			System.out.println(String.valueOf(events.get(i).getEndDate()));
			System.out.println(String.valueOf(events.get(i).getEndTime()));
		}

		
		// Find gaps between dates and times
		List<Event> gaps = new ArrayList<Event>();
		
		for(int i = 0; i < events.size()-1; i++) {
			
			Event currEvent = events.get(i);
			Event nextEvent = events.get(i+1);
			
			if(currEvent.getEndDate() < nextEvent.getStartDate()) {
				gaps.add(new Event(currEvent.getEndDate(), nextEvent.getStartDate(), currEvent.getEndTime(), nextEvent.getStartTime()));
			}
			
			else if(currEvent.getEndDate() == nextEvent.getStartDate()) {
				
				if(currEvent.getEndTime() < nextEvent.getStartTime()) {
					
					gaps.add(new Event(currEvent.getEndDate(), nextEvent.getStartDate(), currEvent.getEndTime(), nextEvent.getStartTime()));
					
				}
			}
			
			else {
				System.out.println("Error in sorting events!");
				System.exit(1);
			}
			
		}
		
		System.out.println("Gaps");
		for(int i = 0; i < gaps.size(); i++) {
			System.out.println(String.valueOf(gaps.get(i).getStartDate()));
			System.out.println(String.valueOf(gaps.get(i).getStartTime()));
			System.out.println(String.valueOf(gaps.get(i).getEndDate()));
			System.out.println(String.valueOf(gaps.get(i).getEndTime()));
		}
		
		
		// Create new .ics files with date and times between .ics files
		for(int i = 0; i < gaps.size(); i++) {
			
			Event currGap = gaps.get(i);
			
			String DTSTART = "DTSTART;" + TIMEZONE + currGap.getStartDateString() + "T" + currGap.getStartTimeString();
			String DTEND = "DTEND;" + TIMEZONE + currGap.getEndDateString() + "T" + currGap.getEndTimeString();
			
			printICSFile("freetime_" + String.valueOf(i), VERSION, "CLASS:PUBLIC", "PRIORITY:1", "LOCATION:",
					"SUMMARY:Free Time", DTSTART, DTEND);
		}
		
		System.out.println("Complete!");
		
	}
	
	public void printICSFile(String filename, String version, String classification, String priority, String location, String summary, String DTSTART, String DTEND) {
		
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
		
	}
		
	public class Event {
		
		private int startTime;
		private int endTime;
		private int startDate;
		private int endDate;
		
		public Event(int startD, int endD, int startT, int endT) {
			
			startDate = startD;
			endDate = endD;
			startTime = startT;
			endTime = endT;
			
		}
		
		public Event(String startD, String endD, String startT, String endT) {
			
			startDate = Integer.parseInt(startD);
			endDate = Integer.parseInt(endD);
			startTime = Integer.parseInt(startT);
			endTime = Integer.parseInt(endT);
			
		}
		
		public int getStartDate() {
			return startDate;
		}
		
		public int getEndDate() {
			return endDate;
		}
		
		public int getStartTime() {
			return startTime;
		}
		
		public int getEndTime() {
			return endTime;
		}
		
		public String getStartDateString() {
			return String.valueOf(startDate);
		}
		
		public String getEndDateString() {
			return String.valueOf(endDate);
		}
		
		public String getStartTimeString() {
			if(startTime < 100000)
				return "0" + String.valueOf(startTime);
			else
				return String.valueOf(startTime);
		}
		
		public String getEndTimeString() {
			if(endTime < 100000)
				return "0" + String.valueOf(endTime);
			else
				return String.valueOf(endTime);
		}
	}

	
}
