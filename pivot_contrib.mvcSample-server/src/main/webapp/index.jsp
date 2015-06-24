<!DOCTYPE JNLP>
<%@page import="pivot_contrib.rmiServer.HttpRequestHelper"%>
<%@ page language="java" contentType="application/x-java-jnlp-file"%>
<%String contextUrl=HttpRequestHelper.getServletContextURL(request);%>
<jnlp spec="6.0+" codebase="<%=contextUrl%>">
  <information>
    <title>mvcSample</title>
    <vendor>pivot_contrib</vendor>
    <description>Apache Pivot RIA/IIA sample application delivered using Java Web Start.</description>    
  </information>
  <update check="always" policy="always"/>
  <offline-allowed>false</offline-allowed>
  <security>
      <all-permissions/>
  </security>
  <resources>
        <!-- Application Resources -->
        <j2se version="1.6+" href="http://java.sun.com/products/autodl/j2se"/>
        <jar href="jws-lib/pivot_contrib.mvcSample_1.0.jar" main="false" />
        <jar href="jws-lib/pivot_contrib.mvcSample-api_1.0.jar" main="false" />
		<jar href="jws-lib/pivot_contrib.di_1.0.jar" main="false" />
		<jar href="jws-lib/pivot_contrib.rmi_1.0.jar" main="false" />
		<jar href="jws-lib/pivot_contrib.util_1.0.jar" main="true" />
		<jar href="jws-lib/pivot-core-2.0.2.jar" main="false" />
		<jar href="jws-lib/pivot-wtk-2.0.2.jar" main="false" />
		<jar href="jws-lib/pivot-wtk-terra-2.0.2.jar" main="false" />
    </resources>
  <application-desc main-class="pivot_contrib.util.launcher.LaunchThin">
  	<argument>/pivot_contrib/mvcSample/SampleView.bxml</argument>
  	<argument><%=contextUrl%>/rmi</argument>  	  
  </application-desc>
</jnlp> 