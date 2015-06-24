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

import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.ComponentMouseButtonListener;
import org.apache.pivot.wtk.FileBrowser;
import org.apache.pivot.wtk.Mouse.Button;

/**
 * Used to start testing a selected .bxml file by double clicking it.
 * <br>
 * Gets attached to the {@link FileBrowser} component.
 * @author Katharina Wegener and Thomas Guretzki
 *
 */
class DoppelklickListener extends ComponentMouseButtonListener.Adapter implements ComponentMouseButtonListener
{
  /**
   * Window that this Listener belongs to
   */
  private BXMLBrowser _bxmlBrowser;

  public DoppelklickListener(BXMLBrowser bxmlBrowser)
  {
    _bxmlBrowser = bxmlBrowser;
  }

  @Override
  public boolean mouseClick(Component component, Button button, int x, int y,
      int count)
  {
    if (count != 2) return false;

    ///// On doubleclick, do some checks and simulate a button press
    File selectedFile = _bxmlBrowser.getFileBrowser().getSelectedFile();
    if (selectedFile == null || selectedFile.isDirectory()) return false;

    if (_bxmlBrowser.getFileBrowser().getFileAt(x,y) == null) return false;

    _bxmlBrowser.getKnopf1().theAction();
    return true;
  }

}
