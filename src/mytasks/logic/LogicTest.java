package mytasks.logic;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

public class LogicTest {
	
	private static MyTasksLogic taskLogic = new MyTasksLogic();

	@Test
	public void testRemoveFirstWord() throws NoSuchMethodException,
			SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		Method method = MyTasksLogic.class.getDeclaredMethod("removeFirstWord",
				String.class);
		method.setAccessible(true);
		String result = (String) method.invoke(taskLogic, "add meeting 22sep #important");
		assertEquals("add", result);
	}
}
