import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;


public class CalendarTest {

	@Test
	public void testFreeTimes() {

		Calendar c = new Calendar();
		List<Event> events = c.getEvents();
		List<Event> gaps = c.getGaps();

		
		assertTrue(events.size() == gaps.size()+1);
		
		for(int i = 0; i < gaps.size(); i++) {
			
			Event currEvent = events.get(i);
			Event nextEvent = events.get(i+1);
			Event currGap = gaps.get(i);
			
			assertTrue(currEvent.getStartDate() <= currGap.getStartDate());
			assertTrue(currEvent.getEndDate() <= nextEvent.getStartDate());
			assertTrue(currEvent.getEndTime() == currGap.getStartTime());
			assertTrue(currGap.getEndTime() == nextEvent.getStartTime());
			
		}
			
		
	}
	
	@Test
	public void testCommonTimes() {
		Calendar c = new Calendar();
		List<Event> commonTimes = c.getCommonTimes();
		
		for(int i = 0; i < commonTimes.size(); i++) {
			Event currCT = commonTimes.get(i);
			
			for(int j = i+1; j < commonTimes.size(); j++) {
				Event otherCT = commonTimes.get(j);
				assertTrue(currCT.getEndTime() != otherCT.getStartTime());
				assertTrue(currCT.getStartTime() != otherCT.getEndTime());
				
				if(currCT.getStartTime() < otherCT.getStartTime()) 
					assertTrue(currCT.getEndTime() < otherCT.getStartTime());
				
				else
					assertTrue(currCT.getStartTime() > otherCT.getEndTime());
			}
		}
	}
	
	

}
