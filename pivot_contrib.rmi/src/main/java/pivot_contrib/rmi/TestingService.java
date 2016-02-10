package pivot_contrib.rmi;

import pivot_contrib.di.Service;

@Service
@RolesAllowed("tomcat")
public interface TestingService {
	public String getMessage();
	public String getMessage(String message);
	public void increaseCounter();
	public void throwException();
	@RolesAllowed("admin")
	public void unauthorizedMethod();
}
