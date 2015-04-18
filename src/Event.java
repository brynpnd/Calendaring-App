public class Event {
		
		private int startTime;
		private int endTime;
		private int startDate;
		private int endDate;
		
		public Event() {
			startTime = 0;
			endTime = 0;
			startDate = 0;
			endDate = 0;
		}
		
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

	
