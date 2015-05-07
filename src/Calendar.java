import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class Calendar {

	public final String VERSION = "VERSION:2.0";
	public final String TIMEZONE = "TZID=Pacific/Honolulu:";
	
	private Scanner scan;
	private List<Event> gaps; 
	private List<Event> events;
	private List<Event> commonTimes;
	
	public static void main(String args[]) {
		
		Calendar c = new Calendar();

	}
	
	public Calendar() {
		
		scan = new Scanner(System.in);
		
		boolean inputValid = false;
		
		while(inputValid == false) {
			System.out.print("Would you like to (c)reate a new .ics file, (f)ind free time between them, or (d)iscover possible meeting times on a given day between two people? ");
			String input = scan.next();
			
			switch(input) {
				case "c": createICSFile(); inputValid = true; break; // create .ics file
				case "d": meetingTimes(); inputValid = true; break; // find meeting times
				case "f": findFreeTime(); inputValid = true; break; // find free time
				default: System.out.println("Invalid entry.");
			}
		}
		
	}
	
	public void meetingTimes() {
		
		String meetingDay = new String();
		List<String> fileStringsA = new ArrayList<String>();
		List<String> fileStringsB = new ArrayList<String>();

		// Get files for Person A
		boolean inputValid = false;
		while(inputValid == false) {
			System.out.println("Enter the pathname of the .ics files you would like to examine for Person A, separated by commas (no spaces):");
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
			
			// Get all .ics files with the summary Free Time
			meetingDay = getFileStartDate(fileToString(f[0]));
			
			if(inputValid == true) {
				for(int i = 0; i < f.length && inputValid == true; i++) {
					String fileString = fileToString(f[i]);
					
					// Must check if all .ics files are on same date
					String startDate = getFileStartDate(fileString);
					String endDate = getFileEndDate(fileString);
					if(!startDate.equals(endDate) || !startDate.equals(meetingDay)) {
						inputValid = false;
						System.out.println("All .ics files must be on the same day");
					}	
					
					else if(isFreeTime(fileString))
						fileStringsA.add(fileString);
				}
			}
		}
		
		// Get files for Person B
		inputValid = false;
		while(inputValid == false) {
			System.out.println("Enter the pathname of the .ics files you would like to examine for Person B, separated by commas (no spaces):");
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
			
			// Get all .ics files with the summary Free Time
			meetingDay = getFileStartDate(fileToString(f[0]));
			
			if(inputValid == true) {
				for(int i = 0; i < f.length; i++) {
					String fileString = fileToString(f[i]);
					
					// Must check if all .ics files are on same date
					String startDate = getFileStartDate(fileString);
					String endDate = getFileEndDate(fileString);
					if(!startDate.equals(endDate) || !startDate.equals(meetingDay)) {
						inputValid = false;
						System.out.println("All .ics files must be on the same day");
					}	
					
					else if(isFreeTime(fileString))
						fileStringsB.add(fileString);
				}
			}
		}
		
		/*** Find common times ***/
		ArrayList<String> startTimeStringsA = new ArrayList<String>();
		ArrayList<String> startTimeStringsB = new ArrayList<String>();
		ArrayList<String> endTimeStringsA = new ArrayList<String>();
		ArrayList<String> endTimeStringsB = new ArrayList<String>();
		
		// Find start and end time strings for person A		
		for(int i = 0; i < fileStringsA.size(); i++) {
			String currString = fileStringsA.get(i);
			
			int index = currString.indexOf(TIMEZONE);
			int start = index;
			while(!Character.isDigit(currString.charAt(start))) 
				start++;

			int end = start;
			while(currString.charAt(end) != '\n')
				end++;
			
			end--;

			startTimeStringsA.add(currString.substring(start+9,end));
			
			start = end;
			
			while(!Character.isDigit(currString.charAt(start))) 
				start++;
			
			end = start;
			
			while(currString.charAt(end) != '\n')
				end++;
			
			end--;
		
			endTimeStringsA.add(currString.substring(start+9,end));				
		}
		
		// Find start and end time strings for person B	
		for(int i = 0; i < fileStringsB.size(); i++) {
			String currString = fileStringsB.get(i);
			
			int index = currString.indexOf(TIMEZONE);
			int start = index;
			while(!Character.isDigit(currString.charAt(start))) 
				start++;

			int end = start;
			while(currString.charAt(end) != '\n')
				end++;
			
			end--;

			startTimeStringsB.add(currString.substring(start+9,end));
			
			start = end;
			
			while(!Character.isDigit(currString.charAt(start))) 
				start++;
			
			end = start;
			
			while(currString.charAt(end) != '\n')
				end++;
			
			end--;
		
			endTimeStringsB.add(currString.substring(start+9,end));				
		}
		
		// Sort events by start time for person A and B
		ArrayList<Event> freeTimesA = new ArrayList<Event>();
		ArrayList<Event> freeTimesB = new ArrayList<Event>();

		for(int i = 0; i < startTimeStringsA.size(); i++) {
			freeTimesA.add(new Event(meetingDay,meetingDay,startTimeStringsA.get(i),endTimeStringsA.get(i)));
		}
		for(int i = 0; i < startTimeStringsB.size(); i++) {
			freeTimesB.add(new Event(meetingDay,meetingDay,startTimeStringsB.get(i),endTimeStringsB.get(i)));
		}
		
		sortEventsStartTime(freeTimesA);
		sortEventsStartTime(freeTimesB);
		
		// Check for common times
		commonTimes = new ArrayList<Event>();
		
		for(int i = 0; i < freeTimesA.size(); i++) {
			
			Event freeTimeA = freeTimesA.get(i);
			int meetingDayNum = Integer.parseInt(meetingDay);
			int freeTimeStartA = freeTimeA.getStartTime();
			int freeTimeEndA = freeTimeA.getEndTime();
			Event e = new Event();
			
			for(int j = 0; j < freeTimesB.size(); j++) {
				
				Event freeTimeB = freeTimesB.get(j);
				int freeTimeStartB = freeTimeB.getStartTime();
				int freeTimeEndB = freeTimeB.getEndTime();
			
				if(freeTimeStartA >= freeTimeStartB && freeTimeEndB > freeTimeStartA) {
					if(freeTimeEndB > freeTimeEndA) {
						e = new Event(meetingDayNum,meetingDayNum,freeTimeStartA,freeTimeEndA);
					}				
					else if(freeTimeEndB <= freeTimeEndA) {
						e = new Event(meetingDayNum,meetingDayNum,freeTimeStartA,freeTimeEndB);
					}					
					
					commonTimes.add(e);
				}
				
				else if(freeTimeStartA < freeTimeStartB && freeTimeEndA > freeTimeStartB) {
					if(freeTimeEndA > freeTimeEndB) {
						e = new Event(meetingDayNum,meetingDayNum,freeTimeStartB,freeTimeEndB);
					}
					else if(freeTimeEndA <= freeTimeEndB) {
						e = new Event(meetingDayNum,meetingDayNum,freeTimeStartB,freeTimeEndA);
					}
					
					commonTimes.add(e);
				}
				
			}
			
		}
		
		// Output .ics files with common time
		for(int i = 0; i < commonTimes.size(); i++) {
			
			Event currCT = commonTimes.get(i);
			
			String DTSTART = "DTSTART;" + TIMEZONE + currCT.getStartDateString() + "T" + currCT.getStartTimeString();
			String DTEND = "DTEND;" + TIMEZONE + currCT.getEndDateString() + "T" + currCT.getEndTimeString();
			
			printICSFile("commontime_" + String.valueOf(i), VERSION, "CLASS:PUBLIC", "PRIORITY:1", "LOCATION:",
					"SUMMARY:POSSIBLE MEETING TIME", DTSTART, DTEND);
		}
		
		System.out.println("Complete!");
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
			fileStrings.add(fileToString(f));
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
		events = new ArrayList<Event>();
		
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
	
		// Find gaps between dates and times
		gaps = new ArrayList<Event>();
		
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
	
	public List<Event> getEvents() {
		return events;
	}
	
	public List<Event> getGaps() {
		return gaps;
	}
	
	public List<Event> getCommonTimes() {
		return commonTimes;
	}
	
	private String fileToString(File f) {
		// Convert file content into String
		FileInputStream fis;
		String fileString = new String();
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
				fileString = new String(data, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}			
		
		return fileString;
	}
	
	private boolean isFreeTime(String fileString) {
		
		// Find out if summary states "Free Time"
		int index = fileString.indexOf("SUMMARY:");
		int start = index+8;
		int end = start+9;

		String s = fileString.substring(start,end);
		if(s.equals("Free Time"))
			return true;
		else
			return false;
	}
	
	private String getFileStartDate(String fileString) {
		
		int index = fileString.indexOf(TIMEZONE);
		int start = index;
		while(!Character.isDigit(fileString.charAt(start))) 
			start++;

		int end = start;
		while(fileString.charAt(end) != '\n')
			end++;
		
		end--;

		return fileString.substring(start, start+8);
	}
	
	private String getFileEndDate(String fileString) {
		
		int index = fileString.indexOf(TIMEZONE);
		int start = index;
		while(!Character.isDigit(fileString.charAt(start))) 
			start++;

		int end = start;
		while(fileString.charAt(end) != '\n')
			end++;
		
		end--;
		start = end;
		
		while(!Character.isDigit(fileString.charAt(start))) 
			start++;
		
		end = start;
		
		while(fileString.charAt(end) != '\n')
			end++;
		
		end--;
		
		return fileString.substring(start,start+8);
	}
	
	private void sortEventsStartTime(ArrayList<Event> events) {
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
	}
}
