/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pivot_contrib.guretzki.bxmlbrowser;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.pivot.beans.BXMLSerializer;
import org.apache.pivot.collections.Map;
import org.apache.pivot.wtk.Alert;
import org.apache.pivot.wtk.Application;
import org.apache.pivot.wtk.DesktopApplicationContext;
import org.apache.pivot.wtk.Dialog;
import org.apache.pivot.wtk.DialogCloseListener;
import org.apache.pivot.wtk.Display;
import org.apache.pivot.wtk.MessageType;
import org.apache.pivot.wtk.Window;

/**
 * Pivot Application (including static {@link #main(String[])} method for direct startup)
 * that uses {@link BXMLBrowser} to allow selecting .bxml files below the current directory
 * and instantiating their contents via a {@link BXMLLoaderButton}.
 * <br>
 * .bxml files containing a {@link Window} (or any subclass including Sheets) as root element are
 * instantiated "as are" and opened in front of the main Window. Other root components are opened
 * as content and in front of a newly created Window. This allows testing of .bxml files that can
 * not be started with the Pivot Eclipse Plugin.
 * <p>
 * In addition to the functionality of BXMLBrowser, this class uses custom classloaders in order
 * to <em>reload all classes that are not part of Apache Pivot or pivot-contrib-guretzki.jar each time
 * a .bxml file gets loaded</em>. This allows developing .bxml files and the classes used therein
 * and conveniently testing/ previewing both by simply pressing F5 after each change.
 * <p> 
 * You can use this Tool easiest in Eclipse (or presumably any other IDE) by taking the following
 * steps:
 * <ol> 
 *  <li> run this class as Java Application, which creates a run configuration.</li> 
 *  <li> You can assign this run configuration (or a copy thereof) to the project containing the
 *        .bxml file(s) to be tested. </li>
 *  <li> All that remains is to add the directory or jar containing 
 *       the package <code>pivot_contrib.guretzki.bxmlbrowser</code> </li> to the classpath
 *       of that project
 * </ol>
 * You can use and reuse the created run configuration(s) for testing all .bxml files inside
 * the corresponding project(s).
 * <p>
 * Note that if you use this class in the way described, classes used inside the .bxml file
 * <em>do</em> get reloaded. If you don't want your own classes to be reloaded with each loading
 * of the .bxml file, use {@link BXMLBrowser} instead!
 *
 * @author Katharina Wegener and Thomas Guretzki
 *
 */
public class BXMLBrowserApp implements Application
{
  /**
   * Has this class been loaded by the {@link #main(String[])} method below?
   * Is set via Reflection in the main method mentioned
   */
  static private boolean viaMain = false;
  /**
   * Class path to be used by the temporary child ClassLoaders
   * Is set via Reflection in the {@link #main(String[])} method below
   */
  static URL[] childClassLoaderURLs = null;
  
  
  @Override
  public void startup(Display display, Map<String, String> properties)
      throws Exception
  {
    if (! viaMain)
    {
      Alert alert = new Alert(MessageType.ERROR, "Starting the BXMLBrowser as Pivot (Script) Application doesn't make sense!", null, false);
      alert.open(display,new DialogCloseListener()
      {
        
        @Override
        public void dialogClosed(Dialog dialog, boolean modal)
        {
          DesktopApplicationContext.exit();
        }
      });
      return;
    }
    BXMLSerializer loader = new BXMLSerializer();
    BXMLBrowser bxmlBrowser = (BXMLBrowser) loader.readObject(BXMLBrowser.class, "BXMLBrowser.bxml", true);
    bxmlBrowser.setTitle("BXMLBrowser Reloaded");
    bxmlBrowser.getKnopf1().setChildClassLoaderURLs(childClassLoaderURLs);
    bxmlBrowser.open(display);
  }

  @Override
  public boolean shutdown(boolean optional) throws Exception
  {
    return false;
  }

  @Override
  public void suspend() throws Exception
  {
  }

  @Override
  public void resume() throws Exception
  {
  }


  /**
   * Standard main method.
   * <p>
   * This uses the current SystemClassLoader to extract the relevant classpath and splits it in two parts:
   * <dl>
   *  <dt>parentURLs</dt>
   *  <dd>classes to remain unchanged during execution</dd>
   *  <dt>childURLs</dt>
   *  <dd>classes to be reloaded with each .bxml reload ({@link BXMLLoaderButton} does that)</dd>
   * </dl>
   * @param args
   * @throws ClassNotFoundException
   * @throws NoSuchMethodException
   * @throws SecurityException
   * @throws InvocationTargetException
   * @throws IllegalAccessException
   * @throws IllegalArgumentException
   * @throws NoSuchFieldException
   */
  public static void main(String[] args) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchFieldException
  {
    ///// get classpath and split it
    ClassLoader sysCL = ClassLoader.getSystemClassLoader();
    if (! (sysCL instanceof URLClassLoader))
      throw new IllegalStateException("Works only with URLClassLoader");
    
		List<URI> coreResources = new ArrayList<URI>();
    addResources(sysCL, coreResources, "org/apache/pivot/beans/BXMLSerializer.class");
    List<URI> pivotResources = new ArrayList<URI>(coreResources);
    addResources(sysCL, pivotResources, "org/apache/pivot/wtk/Component.class");
    addResources(sysCL, pivotResources, "org/apache/pivot/wtk/skin/terra/TerraFrameSkin.class");

    URL urls[] = ((URLClassLoader)sysCL).getURLs();
    ArrayList<URL> childURLs = new ArrayList<URL>();
    ArrayList<URL> parentURLs = new ArrayList<URL>();
    for (URL url: urls)
    {
      String path = url.getPath();
      log(path);
      boolean forParent = path.matches("(.*/org/apache/pivot/.*)" +
                                           "|(.*/pivot-.*\\.jar)" +
                                           "|(.*/pivot-contrib/guretzki/.*)" +
                                           "|(.*/pivot-contrib-guretzki.*\\.jar)" +
                                           "|(.*/svgSalamander\\.jar)");
      if (! forParent)
          forParent = isUriParentOfAny(url, pivotResources);
      if (! forParent)
      {
        try
        {
          File file = new File (url.toURI());
          if (file.isDirectory())
          {
            URL childURL = new URL (url, "pivot_contrib/guretzki/bxmlbrowser/BXMLBrowserApp.class");
            File childFile = new File (childURL.toURI());
            if (childFile.exists())
              forParent = true;
            childURL = new URL (url, "com/kitfox/svg");
            childFile = new File (childURL.toURI());
            if (childFile.exists())
              forParent = true;
          }
        } catch (URISyntaxException e)
        {
          e.printStackTrace();
        } catch (MalformedURLException e)
        {
          e.printStackTrace();
        }
      }
      if (forParent)
      {
        parentURLs.add(url);
        if (path.matches(".*pivot-core.*"))
          ///// child ClassLoader must be able to load BXMLSerializer
          childURLs.add(url);
        else if (isUriParentOfAny(url, coreResources))
          childURLs.add(url);
      }
      else
        childURLs.add(url);
    }
    log("BXMLBrowser Parent ClassPath Entries:");
    for (URL url: parentURLs) log(url);
    log("BXMLBrowser Child ClassLoader ClassPath Entries:");
    for (URL url: childURLs) log(url);

    ///// create parent classloader and use it to load this class via {@link DesktopApplicationContext}
//    URLClassLoader parent = new NonDelegatingURLClassLoader(urls, sysCL.getParent());
    URLClassLoader parent = new URLClassLoader(parentURLs.toArray(new URL [parentURLs.size()]), sysCL.getParent());
    Thread.currentThread().setContextClassLoader(parent);
    @SuppressWarnings("unchecked")
    Class<DesktopApplicationContext> clDac = (Class<DesktopApplicationContext>) parent.loadClass("org.apache.pivot.wtk.DesktopApplicationContext");

    /// set static "parameters" of this class (as loaded by <parent>)
    @SuppressWarnings("unchecked")
    Class<BXMLBrowserApp> clBrowse = (Class<BXMLBrowserApp>) parent.loadClass("pivot_contrib.guretzki.bxmlbrowser.BXMLBrowserApp");
    Field feld = clBrowse.getDeclaredField("viaMain");
    feld.setAccessible(true);
    feld.set(null, true);
    feld = clBrowse.getDeclaredField("childClassLoaderURLs");
    feld.setAccessible(true);
    feld.set(null, childURLs.toArray(new URL[childURLs.size()]));

    /// invoke DesktopApplicationContexts <main> method, which causes <startup()> (see above) to be invoked.
    Method main = clDac.getDeclaredMethod("main", Class.class, args.getClass());
    main.invoke(null, clBrowse, args);
  }

	private static boolean isUriParentOfAny(URL url, List<URI> pivotResources)
	{
		for (URI resource: pivotResources)
		{
			try
			{
				if (url.toURI().relativize(resource) != resource)
				{
					log (url.toString() + " is parent of " + resource + " => must be Apache Pivot component of classpath!");
					return true;
				}
			} catch (URISyntaxException e)
			{
				e.printStackTrace();
			}
		}
		return false;
	}

	private static void addResources(ClassLoader sysCL, List<URI> pivotResources, String exampleClass)
	{
		log ("Looking for " + exampleClass);
		try
		{
			Enumeration<URL> coreResources = sysCL.getResources(exampleClass);
			while (coreResources.hasMoreElements())
			{
				URL resource = coreResources.nextElement();
				log ("found: " + resource);
				pivotResources.add (resource.toURI());
			}
		} catch (IOException e1)
		{
			e1.printStackTrace();
		} catch (URISyntaxException e)
		{
			e.printStackTrace();
		}
	}

  
  /**
   * Logging helper
   * @param text to be logged
   */
  static private void log (Object text)
  {
    System.out.println(text.toString());
  }
}
