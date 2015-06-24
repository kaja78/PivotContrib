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

import java.net.URL;
import java.net.URLClassLoader;

/**
 * Uses a hierarchy of <code>NonDelegatingURLClassLoader</code>s to find out if any ancestor ClassLoader
 * has already loaded a class that was requested; however, the actual loading of new classes is done by
 * the lowermost <code>NonDelegatingURLClassLoader</code>.
 *
 * Only if that fails, using the system ClassLoader
 * is considered, but only if the requested class is not included in {@link #_strictlyNonDelegatedClasses}.
 *
 * <p>This class is not actually used by the current version of {@link BXMLBrowserApp}</p>
 * @author Thomas Guretzki
 *
 */
public class NonDelegatingURLClassLoader extends URLClassLoader
{
  /**
   * ClassLoader to be used if all else fails
   */
  private ClassLoader _lastDelegate;
  /**
   * Class names that are not to be passed to {@link #_lastDelegate}
   */
  private String[] _strictlyNonDelegatedClasses;


  /**
   * Constructor.
   * An {@link URLClassLoader} without URLs is used as last Delegate.
   * @param urls the URLs from which to load classes and resources
   * @param strictlyNonDelegatedClasses Class names that are not to be passed to {@link #_lastDelegate}
   */
  public NonDelegatingURLClassLoader(URL[] urls, String ... strictlyNonDelegatedClasses)
  {
    super(urls);
    storeStrictlyNonDelegatedClassNames(strictlyNonDelegatedClasses);
    findLastDelegate();
  }

  /**
   * "Luxury" constructor
   * @param urls the URLs from which to load classes and resources
   * @param parent the parent class loader for delegation
   * @param strictlyNonDelegatedClasses Class names that are not to be passed to {@link #_lastDelegate}
   */
  public NonDelegatingURLClassLoader(URL[] urls, ClassLoader parent, String ... strictlyNonDelegatedClasses)
  {
    super(urls, parent);
    storeStrictlyNonDelegatedClassNames(strictlyNonDelegatedClasses);
    findLastDelegate();
  }

  /**
   * Another constructor
   * @param parent the parent class loader for delegation
   * @param strictlyNonDelegatedClasses Class names that are not to be passed to {@link #_lastDelegate}
   */
  public NonDelegatingURLClassLoader(ClassLoader parent, String ... strictlyNonDelegatedClasses)
  {
    this (new URL[0], parent, strictlyNonDelegatedClasses);
    if (! (parent instanceof URLClassLoader))
      throw new IllegalStateException("Constructor darf nur von einem URLClassLoader aufgerufen werden!");
    for (URL url: ((URLClassLoader)parent).getURLs())
    {
      this.addURL(url);
    }
  }

  /**
   * Lookup a possibly loaded class without causing its loading
   * @param name Class name
   * @return the class, if it was already loaded. Otherwise <code>null</code>
   */
  public Class<?> publicFindLoadedClass (String name)
  {
    // TODO: check if local Classes of <name> have already been loaded, or if <name> is a local class of another class already loaded
    // in both cases, <name> has to be loaded and returned
    // testable with ComponentExplorer
    Class<?> erg = findLoadedClass(name);
    if (erg == null)
    {
      if (name.contains("$"))
      {
        String mainName = name.replaceAll("\\$[^$]+", "");
        System.out.println(name + "=>" + mainName + "(" + this + ")");
        if (findLoadedClass(mainName) != null)
        {
          try
          {
            System.out.println("Loading " + name);
            erg = findClass(name);
          } catch (ClassNotFoundException e)
          {
            System.out.println("Class not found");
            erg = null;
          }
        }
      }
    }
    return erg;
  }


  @Override
  protected synchronized Class<?> loadClass(String name, boolean resolve)
      throws ClassNotFoundException
  {
    Class<?> c = publicFindLoadedClass(name);
    if (c != null)
      return c;

    if (getClass().getCanonicalName().equals(name))
      return getClass();

    boolean strictlyForbidden = false;
    for (String className: _strictlyNonDelegatedClasses)
      if (name.startsWith(className))
      {
        strictlyForbidden = true;
        break;
      }

    if (! strictlyForbidden)
    {
      ClassLoader cl = getParent();
      while (cl != null && cl instanceof NonDelegatingURLClassLoader)
      {
        c = ((NonDelegatingURLClassLoader)cl).publicFindLoadedClass(name);
        if (c != null)
          return c;
        cl = cl.getParent();
      }
    }

    try
    {
      c = findClass(name);
    }
    catch (ClassNotFoundException e)
    {
      if (strictlyForbidden)
        throw (e);
      else
        c = _lastDelegate.loadClass(name);
    }
    return c;
  }

  /**
   * @param strictlyNonDelegatedClasses param from constructor
   *  (synchronized access not really necessary as this is only called by constructors
   *   FindBugs is happy now, however :-)
   */
  synchronized private void storeStrictlyNonDelegatedClassNames(
      String... strictlyNonDelegatedClasses)
  {
    if (strictlyNonDelegatedClasses == null)
      strictlyNonDelegatedClasses = new String[0];
    _strictlyNonDelegatedClasses = strictlyNonDelegatedClasses;
  }

  /**
   * Constructor helper
   */
  private void findLastDelegate()
  {

    ClassLoader cl = getParent();
    if (cl == null)
    {
      _lastDelegate = new URLClassLoader(null);
      return;
    }
    while (cl.getParent() != null)
      cl = cl.getParent();
    _lastDelegate = cl;
  }


}
