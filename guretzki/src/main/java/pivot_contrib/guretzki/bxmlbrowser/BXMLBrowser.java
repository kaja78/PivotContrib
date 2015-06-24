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
import java.io.FileFilter;
import java.net.URL;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.HashMap;
import org.apache.pivot.collections.Map;
import org.apache.pivot.collections.Sequence;
import org.apache.pivot.util.Filter;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonPressListener;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.ComponentKeyListener;
import org.apache.pivot.wtk.ComponentListener;
import org.apache.pivot.wtk.FileBrowser;
import org.apache.pivot.wtk.FileBrowserListener;
import org.apache.pivot.wtk.Frame;
import org.apache.pivot.wtk.Keyboard.KeyCode;
import org.apache.pivot.wtk.Keyboard.KeyLocation;
import org.apache.pivot.wtk.Label;
import org.apache.pivot.wtk.PushButton;
import org.apache.pivot.wtk.Window;

/**
 * Frame that allows selecting .bxml files below the current directory
 * and instantiating their contents via a {@link BXMLLoaderButton}.
 * <br>
 * .bxml files containing a {@link Window} (or any subclass including Sheets) as root element are
 * instantiated "as are" and opened in front of this Window. Other root components are opened
 * as content and in front of a newly created Window. This allows testing of .bxml files that can
 * not be started with the Pivot Eclipse Plugin.
 * <p>
 * You can use this Tool easiest in Eclipse if you have the Pivot Eclipse Plugin installed.
 * In this case, you just
 * <ol>
 *  <li> run the script file BXMLBrowser.bxml as Pivot Script, which creates
 *       a run configuration.</li>
 *  <li> You can assign this (or a copy thereof) to the project containing the
 *        .bxml file(s) to be tested. </li>
 *  <li> All that remains is to add the directory or jar containing
 *       the package <code>pivot_contrib.guretzki.bxmlbrowser</code> </li> to the classpath
 *       of that project
 * </ol>
 * You can use and reuse the created run configuration(s) for testing all .bxml files inside
 * the corresponding project(s).
 * <p>
 * Note that if you use this class in the way described, classes used inside the .bxml file
 * are <em>not</em> reloaded. If you want your own classes to be reloaded with each loading
 * of the .bxml file, use {@link BXMLBrowserApp} instead!
 *
 * @author Katharina Wegener and Thomas Guretzki
 *
 */
public class BXMLBrowser extends Frame implements Bindable, FileBrowserListener, Filter<File>,
    ButtonPressListener, ComponentKeyListener
{
  private String _startDir;
  private DoppelklickListener _doppelklickListener;

  @BXML private FileBrowser fileBrowser;
  @BXML private Label datei1;
  @BXML private BXMLLoaderButton knopf1;
  @BXML private PushButton zuKnopf;
  @BXML private PushButton reloadKnopf;
  @BXML private HashMap<String, String> localeHashMap;

  @Override
  public void initialize(Map<String, Object> namespace, URL location, Resources resources)
  {
    _doppelklickListener = new DoppelklickListener(this);
    fileBrowser.getComponentMouseButtonListeners().add(_doppelklickListener);
    zuKnopf.setEnabled(false);

    ///// find first ambiguous file or directory below "src"
    _startDir = System.getProperty("user.dir");
    if (_startDir != null)
    {
      File startDir = new File(_startDir);
      File tempDir = new File(_startDir + "/src");
      FileFilter filter = new FileFilter()
      {
        @Override
        public boolean accept(File pathname)
        {
          if (pathname.isFile())
            return false; // ausfiltern
          if (pathname.getName().startsWith("."))
            return false;
          return true;
        }
      };
      while (tempDir != null && tempDir.isDirectory() && tempDir.exists())
      {
        startDir = tempDir;
        File[] kinder = tempDir.listFiles(filter);
        if (kinder.length == 1)
          tempDir = kinder[0];
        else
          tempDir = null;
      }
      fileBrowser.setRootDirectory(startDir);
    }

    ///// some more initializations
    fileBrowser.getFileBrowserListeners().add(this);
    fileBrowser.setDisabledFileFilter(this);
    zuKnopf.getButtonPressListeners().add(this);
    reloadKnopf.getButtonPressListeners().add(new ButtonPressListener()
    {
      @Override
      public void buttonPressed(Button button)
      {
        reload();
      }
    });
    getComponentListeners().add(new ComponentListener.Adapter()
    {
      @Override
      public void sizeChanged(Component component, int previousWidth, int previousHeight)
      {
        //System.out.println("sizeChanged");
        knopf1.adjustObjFrame();
      }
      
    });


  }

  @Override
  public void rootDirectoryChanged(FileBrowser fileBrowser, File previousRootDirectory)
  {
    fileBrowser.clearSelection();
  }

  @Override
  public void selectedFileAdded(FileBrowser fileBrowser, File file)
  {
  }

  @Override
  public void selectedFileRemoved(FileBrowser fileBrowser, File file)
  {
  }

  @Override
  public void selectedFilesChanged(FileBrowser fileBrowser, Sequence<File> previousSelectedFiles)
  {
    File selectedFile = fileBrowser.getSelectedFile();
    if (selectedFile != null)
    {
      String absolutePath = selectedFile.getAbsolutePath();
      String nurName = selectedFile.getName();
      knopf1.setTestDatei(absolutePath);
      if (selectedFile.isFile())
        datei1.setText(localeHashMap.get("currentFile") + nurName);
      else
        datei1.setText("");
    }
  }

  @Override
  public void multiSelectChanged(FileBrowser fileBrowser)
  {
  }

  @Override
  public void disabledFileFilterChanged(FileBrowser fileBrowser, Filter<File> previousDisabledFileFilter)
  {
  }

  @Override
  public boolean include(File item)
  {
    if (item.isDirectory())
      return false;
    if (item.getAbsolutePath().endsWith(".bxml"))
      return false;
    return true;
  }

  @Override
  public void buttonPressed(Button button)
  {
    knopf1.close();
  }

  @Override
  public boolean keyTyped(Component component, char character)
  {
    return false;
  }

  @Override
  public boolean keyPressed(Component component, int keyCode, KeyLocation keyLocation)
  {
    System.out.println(keyCode);
    if (keyCode == KeyCode.F5)
    {
      return reload();
    }
    return false;
  }

  /**
   * Reload the last selected .bxml file. Gets called by the keyPressed() method as well
   * as the "Reload" PushButton.
   * @return <code>true</code>
   */
  private boolean reload()
  {
    knopf1.close();
    knopf1.theAction();
    return true;
  }

  @Override
  public boolean keyReleased(Component component, int keyCode, KeyLocation keyLocation)
  {
    return false;
  }

  @Override
  public boolean keyPressed(int keyCode, KeyLocation keyLocation)
  {
    boolean consumed = super.keyPressed(keyCode, keyLocation);
    if (isEnabled() && !consumed)
    {
    }

    return consumed;
  }

  /**
   * @param startDir
   *          the startDir to set
   */
  public void setStartDir(String startDir)
  {
    _startDir = startDir;
  }

  /**
   * @return the startDir
   */
  public String getStartDir()
  {
    return _startDir;
  }

  /**
   * @return the rather complex {@link BXMLLoaderButton} object responsible for loading .bxml files
   * which is part of this frame.
   */
  public BXMLLoaderButton getKnopf1()
  {
    return knopf1;
  }

  /**
   * @return the {@link FileBrowser} which is part of this frame.
   */
  public FileBrowser getFileBrowser()
  {
    return fileBrowser;
  }

    public PushButton getZuKnopf()
    {
        return zuKnopf;
    }

}
