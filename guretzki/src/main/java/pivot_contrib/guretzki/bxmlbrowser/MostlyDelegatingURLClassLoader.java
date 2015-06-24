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
 * Behaves generally like {@link URLClassLoader}, with the addition that any number of
 * class names can be excepted from ClassLoader delegation. Those classes are then always
 * (re)loaded by the current ClassLoader.
 * @author thomas.guretzki
 *
 */
public class MostlyDelegatingURLClassLoader extends URLClassLoader
{
  private String[] _strictlyNonDelegatedClasses;


  /**
   * @param urls the URLs from which to load classes and resources
   * @param strictlyNonDelegatedClasses names of classes that must always be loaded by this instance
   */
  public MostlyDelegatingURLClassLoader(URL[] urls, String ... strictlyNonDelegatedClasses)
  {
    super(urls);
    storeStrictlyNonDelegatedClassNames(strictlyNonDelegatedClasses);
  }

  /**
   * @param urls the URLs from which to load classes and resources
   * @param parent the parent class loader for delegation
   * @param strictlyNonDelegatedClasses names of classes that must always be loaded by this instance
   */
  public MostlyDelegatingURLClassLoader(URL[] urls, ClassLoader parent, String ... strictlyNonDelegatedClasses)
  {
    super(urls, parent);
    storeStrictlyNonDelegatedClassNames(strictlyNonDelegatedClasses);
  }

  /**
   * @param parent the parent class loader for delegation
   * @param strictlyNonDelegatedClasses names of classes that must always be loaded by this instance
   */
  public MostlyDelegatingURLClassLoader(ClassLoader parent, String ... strictlyNonDelegatedClasses)
  {
    this (new URL[0], parent, strictlyNonDelegatedClasses);
    if (! (parent instanceof URLClassLoader))
      throw new IllegalStateException("Constructor darf nur von einem URLClassLoader aufgerufen werden!");
    for (URL url: ((URLClassLoader)parent).getURLs())
    {
      this.addURL(url);
    }
  }

  @Override
  public synchronized Class<?> loadClass(String name)
      throws ClassNotFoundException
  {
    Class<?> c = findLoadedClass(name);
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

    if (strictlyForbidden)
      c = findClass(name);
    else
      c = super.loadClass(name);

    return c;
  }

  /** Constructor helper
   *  (synchronized access not really necessary as this is only called by constructors
   *   FindBugs is happy now, however :-)
   * @param strictlyNonDelegatedClasses names of classes that must always be loaded by this instance
   */
  synchronized private void storeStrictlyNonDelegatedClassNames(
      String... strictlyNonDelegatedClasses)
  {
    if (strictlyNonDelegatedClasses == null)
      strictlyNonDelegatedClasses = new String[0];
    _strictlyNonDelegatedClasses = strictlyNonDelegatedClasses;
  }


}
