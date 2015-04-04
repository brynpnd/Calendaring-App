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
	
	

}
