package pivot_contrib.util.model;

import java.util.Comparator;

import junit.framework.TestCase;

import org.apache.pivot.collections.Map;
import org.apache.pivot.collections.MapListener;

import pivot_contrib.di.BeanInjector;

public class TestModelProxyFactory extends TestCase{

	public static final String TESTING_MESSAGE="Testing message";
	
	@Model
	private TestingModel testingModel;
	
	public TestModelProxyFactory() {
		BeanInjector.getBeanInjector().injectDependencies(this);
		testingModel.setMessage(TESTING_MESSAGE);
	}
	
	public void testGetter() {				
		assertEquals(TESTING_MESSAGE,testingModel.getMessage());
	}
	
	public void testSetter() {		
		TestingListener testingListener=new TestingListener();
		testingModel.getTestingModelListeners().add(testingListener);
		TestingMapListener testingMapListener=new TestingMapListener();
		testingModel.getMapListeners().add(testingMapListener);
		
		assertFalse(testingListener.changeEventFired);
		assertFalse(testingMapListener.valueUpdatedFired);
		assertFalse(testingListener.changeEventFired);
		testingModel.setMessage("Message");
		assertTrue(testingListener.changeEventFired);
		assertTrue(testingMapListener.valueUpdatedFired);
		assertEquals("Message", testingModel.getMessage());
	}
	
	public void testGet(){		
		assertEquals(TESTING_MESSAGE, testingModel.get("message"));
	}
	
	public void testPut() {		
		TestingListener testingListener=new TestingListener();
		testingModel.getTestingModelListeners().add(testingListener);
		TestingMapListener testingMapListener=new TestingMapListener();
		testingModel.getMapListeners().add(testingMapListener);
		
		assertFalse(testingListener.changeEventFired);
		assertFalse(testingMapListener.valueUpdatedFired);
		testingModel.put("message","Message");
		assertTrue(testingListener.changeEventFired);
		assertTrue(testingMapListener.valueUpdatedFired);
		assertEquals("Message", testingModel.getMessage());
	}


	public class TestingListener implements TestingModelListener {
		boolean changeEventFired=false;

		
		public void messageChanged(Object model) {
			changeEventFired=true;			
		}		
	}
	
	public class TestingMapListener implements MapListener<String, Object> {
		boolean valueUpdatedFired=false;
		public void valueAdded(Map<String, Object> map, String key) {
		}

		public void valueUpdated(Map<String, Object> map, String key,
				Object previousValue) {
			if ("message".equals(key)){
				valueUpdatedFired=true;
			}
		}

		public void valueRemoved(Map<String, Object> map, String key,
				Object value) {
		}

		public void mapCleared(Map<String, Object> map) {
		}

		public void comparatorChanged(Map<String, Object> map,
				Comparator<String> previousComparator) {
		}
		
	}

}
