/**
 * 
 */
package ca.datamagic.google.places.inject;

import java.time.Duration;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Greg
 *
 */
public class PerformanceInterceptor implements MethodInterceptor {
	private static final Logger logger = LogManager.getLogger(PerformanceInterceptor.class);
	
	private static String getKey(MethodInvocation invocation) {
		StringBuffer key = new StringBuffer();
		key.append(invocation.getThis().getClass().getName());
		key.append(".");
		key.append(invocation.getMethod().getName());
		key.append("(");
		Object[] arguments = invocation.getArguments();
		if (arguments != null) {
			for (int ii = 0; ii < arguments.length; ii++) {
				if (ii > 0) {
					key.append(",");
				}
				key.append(arguments[ii].toString());
			}
		}
		key.append(")");
		return key.toString();
	}
	
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		long startTimeMillis = System.currentTimeMillis();
		String methodKey = getKey(invocation);
		try {
			return invocation.proceed();
		} finally {
			long endTimeMillis = System.currentTimeMillis();
			long runningTimeMillis = endTimeMillis - startTimeMillis;
			Duration duration = Duration.ofMillis(runningTimeMillis);
			logger.debug(methodKey + " => " + duration);
		}
	}

}
