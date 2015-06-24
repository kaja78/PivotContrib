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
import java.lang.reflect.Method;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;

import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonPressListener;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.ComponentKeyListener;
import org.apache.pivot.wtk.Display;
import org.apache.pivot.wtk.Frame;
import org.apache.pivot.wtk.PushButton;
import org.apache.pivot.wtk.Sheet;
import org.apache.pivot.wtk.Window;
import org.apache.pivot.wtk.WindowStateListener;

/**
 * PushButton that includes the capability to load any .bxml file containing a {@link Component} as root element
 * <p>
 * Optionally, this can be done using a temporary {@link ClassLoader}, so that changes to the classes during
 * runtime get included. To do that, just use {@link #setChildClassLoaderURLs(URL[])}.
 *
 * @author Katharina Wegener and Thomas Guretzki
 *
 */
public class BXMLLoaderButton extends PushButton
{
private static final int UPPER_EDGE_PIXEL = 80;
//  protected final Logger fLog = Logger.getLogger(getClass());

  private static final String BXML_SERIALIZER_CLASSNAME = "org.apache.pivot.beans.BXMLSerializer";
  private String _testDatei;
  private Window _objFrame;
  private ClassLoader _previousContextClassLoader;
  /**
   * URLs of all classpath elements that are to be reloaded. Should normally <em>not</em> include any
   * of the Apache Pivot classes.
   */
  private URL[] childClassLoaderURLs;

  /**
   * Constructor
   * <p>
   * initializes the ButtonPressListener, too.
   */
  public BXMLLoaderButton()
  {
    // without parameter to be instantiable by bxml-serializer
    this.getButtonPressListeners().add(new ButtonPressListener()
    {
      @Override
      public void buttonPressed(Button button)
      {
        theAction();
      }
    });
  }

  /**
   * Do the actual loading of the .bxml file.
   * <p>
   * <code>public</code>, so that it can be called by {@link DoppelklickListener}, too.
   */
  public void theAction()
  {
      if (_testDatei == null)
      {
          System.err.println ("No file selected yet!");
          return;
      }
      if ( ! isEnabled())
      {
          System.err.println ("File still in test!");
          return;
      }
    System.out.println(getClass().getClassLoader());
    ClassLoader cl;
    _previousContextClassLoader = Thread.currentThread().getContextClassLoader();
    if (getClass().getClassLoader() instanceof NonDelegatingURLClassLoader || childClassLoaderURLs != null)
    {
      cl = AccessController.doPrivileged(new PrivilegedAction<MostlyDelegatingURLClassLoader>()
      {
        public MostlyDelegatingURLClassLoader run()
        {
          return new MostlyDelegatingURLClassLoader(childClassLoaderURLs, getClass().getClassLoader(), BXML_SERIALIZER_CLASSNAME);
        }
      });
      System.out.println("new URLClassLoader: " );
      Thread.currentThread().setContextClassLoader(cl);
    }
    else
      cl = getClass().getClassLoader();
    try
    {
      Class<?> clBXMLS = cl.loadClass(BXML_SERIALIZER_CLASSNAME);
      Object bxmlSerializer = clBXMLS.newInstance();
      URL url = new File(_testDatei).toURI().toURL();
      Method method = clBXMLS.getDeclaredMethod("readObject", url.getClass());
      Object obj = method.invoke(bxmlSerializer, url);
      System.out.println(cl.toString() + "/" + obj.getClass().getClassLoader());
      System.out.println(Component.class.getClassLoader());

      if (!(obj instanceof Window))
      {
        _objFrame = new Frame();
        _objFrame.setContent((Component) obj);
      }
      else
        _objFrame = (Window) obj;
      try
      {
        _objFrame.getComponentKeyListeners().add((ComponentKeyListener) this.getWindow());
      }
      catch (ClassCastException e)
      {
        e.printStackTrace();
      }
      _objFrame.getWindowStateListeners().add(new WindowStateListener.Adapter()
      {
                @Override
                public void windowClosed(Window window, Display display, Window owner)
                {
                    close();
                }
      });
      _objFrame.open(this.getWindow());
      enableZuKnopf(true);
      _objFrame.requestFocus();
      if (!(_objFrame instanceof Sheet))
      {
        _objFrame.setMaximized(false);
        adjustObjFrame();
      }
    } catch (Exception e1)
    {
      throw new RuntimeException(e1);
    }
  }

  public void adjustObjFrame()
  {
    if (_objFrame == null)
      return;
    _objFrame.setX(0);
    _objFrame.setY(UPPER_EDGE_PIXEL);
    _objFrame.setPreferredWidth(getWindow().getWidth());
    _objFrame.setPreferredHeight(getWindow().getHeight() - UPPER_EDGE_PIXEL);
  }

  /**
   * Close the window resulting from the loaded .bxml file
   * <p>
   * The classloader used for loading is (hopefully) discarded, too.
   */
  public void close()
  {
      if (_objFrame == null)
          return;
    if (_objFrame.isOpen())
    _objFrame.close();
    if (_objFrame.isClosed() && _previousContextClassLoader != null)
    {
      Thread.currentThread().setContextClassLoader(_previousContextClassLoader);
      _previousContextClassLoader = null;
      System.out.println("ContextClassLoader reset");
    }
    enableZuKnopf(false);
  }

    private void enableZuKnopf(boolean enable)
    {
        BXMLBrowser bxmlBrowser = (BXMLBrowser) getAncestor(BXMLBrowser.class);
        if (bxmlBrowser != null)
        {
          bxmlBrowser.getZuKnopf().setEnabled(enable);
        }
        setEnabled( ! enable);
    }

  /**
   * @param testDatei .bxml file to be loaded/ tested/ previewed
   */
  public void setTestDatei(String testDatei)
  {
    this._testDatei = testDatei;
  }

  /**
   * Standard getter
   * @return .bxml file to be loaded/ tested/ previewed
   */
  public String getTestDatei()
  {
    return _testDatei;
  }

  /**
   * Standard getter
   * @return {@link #childClassLoaderURLs}
   */
  public URL[] getChildClassLoaderURLs()
  {
    return childClassLoaderURLs;
  }

  /**
   * Standard setter,
   * used by {@link BXMLBrowserApp}
   * @see #childClassLoaderURLs
   * @param childClassLoaderURLs
   */
  public void setChildClassLoaderURLs(URL[] childClassLoaderURLs)
  {
    this.childClassLoaderURLs = childClassLoaderURLs;
  }
}
