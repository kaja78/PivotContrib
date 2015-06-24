package pivot_contrib.rmi;



public class RemoteServiceProxyInvocationHandler extends ServiceProxyInvocationHandler {
	
	private String endpointUrl;
	private String basicAuthorisation;
	
	public RemoteServiceProxyInvocationHandler(String remoteInterfaceName,String endpointUrl) {
		this(remoteInterfaceName,endpointUrl,null);
	}

	public RemoteServiceProxyInvocationHandler(String remoteInterfaceName,
			String endpointUrl, String basicAuthorisation) {
		super(remoteInterfaceName);
		this.endpointUrl=endpointUrl;
		this.basicAuthorisation=basicAuthorisation;
	}

	protected Object invoke(RMIRequest request) {
		HttpRMIRequestInvoker invoker=new HttpRMIRequestInvoker(endpointUrl,basicAuthorisation,request);
		return invoker.getResult();		
	}

	
}
