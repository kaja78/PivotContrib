package pivot_contrib.rmi;

import java.io.Serializable;

/**
 * Remote method invocation request.
 * */
public class RMIRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final String remoteInterfaceName;
	private final String methodName;
	private final String[] parameterTypeNames;
	private final Object[] paramaters;

	public RMIRequest(String remoteInterfaceName, String methodName,
			Class<?>[] parameterTypes,Object[] paramaters) {
		this.remoteInterfaceName = remoteInterfaceName;
		this.parameterTypeNames=getParameterTypeNames(parameterTypes);
		this.methodName = methodName;
		this.paramaters = paramaters;
	}
	
	private String[] getParameterTypeNames(Class<?>[] parameterTypes) {
		String[] typeNames=new String[parameterTypes.length];
		for (int i = 0; i < typeNames.length; i++) {
			typeNames[i]=parameterTypes[i].getName();
		}
		return typeNames;
	}

	public RMIRequest(String remoteInterfaceName, String methodName) {
		this(remoteInterfaceName,methodName,new Class<?>[]{},new Object[]{});
	}

	public String getRemoteInterfaceName() {
		return remoteInterfaceName;
	}

	public String getMethodName() {
		return methodName;
	}

	public Object[] getParamaters() {
		return paramaters;
	}
	
	public Class<?>[] getParameterTypes() {
		Class<?>[] parameterTypes = new Class<?>[parameterTypeNames.length];
		for (int i = 0; i < parameterTypeNames.length; i++) {
			try {
				parameterTypes[i] = Class.forName(this.parameterTypeNames[i]);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
		return parameterTypes;
	}
	
	@Override
	public String toString() {
		StringBuffer sb=new StringBuffer(remoteInterfaceName+'.'+methodName+'(');
		for (Class<?> type : getParameterTypes()) {
			sb.append(type.getSimpleName());
		}
		sb.append(')');
		return sb.toString();
	}
}
