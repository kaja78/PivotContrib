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
package pivot_contrib.guretzki;

import static java.lang.Math.min;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import org.apache.pivot.beans.DefaultProperty;
import org.apache.pivot.collections.ArrayList;
import org.apache.pivot.wtk.Alert;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.ComponentListener;
import org.apache.pivot.wtk.DesktopApplicationContext;
import org.apache.pivot.wtk.DesktopApplicationContext.DisplayListener;
import org.apache.pivot.wtk.Dialog;
import org.apache.pivot.wtk.DialogCloseListener;
import org.apache.pivot.wtk.Dimensions;
import org.apache.pivot.wtk.Display;
import org.apache.pivot.wtk.MessageType;
import org.apache.pivot.wtk.Window;
import org.apache.pivot.wtk.content.ButtonData;

import pivot_contrib.guretzki.awtlink.HostDialog;


/**
 * Can host any Dialog outside of the normal Application HostFrame;
 * this way, awt is used to do the {@link #open(Display)} in a blocking way,
 * i. e. the flow of control doesn't return before the content Dialog is closed.
 * <p>
 * The content can be set in the constructor or (e. g. via bxml) using {@link #setContent(Dialog)}.
 * <br>
 * Both {@link #sAlert} Methods construct and open an Alert inside this host.
 * @author thomas.guretzki
 *
 */
@DefaultProperty("content")
public class BlockingDialog
{
  /**
   * the actual pivot Dialog or {@link Alert} to show
   */
  private Dialog _content;
  /**
   * used as owner inside this host
   */
  private Window _window = new Window ();
  /**
   * close host, when the content dialog is closed
   */
  private final DialogCloseListener _closeListener = new DialogCloseListener()
  {
    @Override
    public void dialogClosed(Dialog dialog, boolean modal)
    {
      System.out.println ("closed!");
      _hostDialog.setVisible(false);
      _hostDialog.removeWindowFocusListener(_focusListener);
    }
  };
  /**
   * Move the host to the same position as the content
   * (or at least in the same direction :-)
   */
  private final ComponentListener _moveListener = new ComponentListener.Adapter()
  {
    @Override
    public void locationChanged(Component component, int previousX, int previousY)
    {
      log ("locationChanged x=" + component.getX() + ", vorher=" + previousX);
      int x = _hostDialog.getX() + component.getX();
      int y = _hostDialog.getY() + component.getY();
      _hostDialog.setLocation(x, y);
      _content.setLocation(0, 0);
    }
  };
  /**
   * the actual host used
   */
  private HostDialog _hostDialog;
  /**
   * Transfers Focus to the {@link #_content}, when the host has been opened
   */
  private WindowFocusListener _focusListener = new WindowFocusListener()
  {
    @Override
    public void windowLostFocus(WindowEvent e)
    {
    }

    @Override
    public void windowGainedFocus(WindowEvent e)
    {
      log("windowFocus");
      _content.requestActive();
      _content.requestFocus();
    }
  };


  public BlockingDialog()
  {
  }

  public BlockingDialog(Dialog dialog)
  {
    this ();
    setContent(dialog);
  }


  /**
   * Open Dialog and return Content.
   * User's choice can be derived by {@link Dialog#getResult()} and {@link Alert#getSelectedOption()}.
   * @param display determines the opening position of the host
   * @return Content containing the user's choice.
   */
  public Dialog open(Display display)
  {
    Dimensions pSize = _content.getPreferredSize();
    int width = min(display.getWidth(), pSize.width);
    int height = min(display.getHeight(), pSize.height);
    log("Display x=" + display.getHostWindow().getX());
    _hostDialog = createDisplay(width, height,
        display.getHostWindow().getX() + (display.getWidth() - width) / 2
       ,display.getHostWindow().getY() + (display.getHeight() - height) / 2
       , true, true, true, display.getHostWindow(), null);
    Display newDisplay = _hostDialog.getDisplay();
    _window.open(newDisplay);
    _content.open(_window, _closeListener);
    _hostDialog.setTitle(_content.getTitle());
    _hostDialog.requestFocus();

    log ("vorm Visible: active:"+_hostDialog.isActive());
    _hostDialog.setVisible(true);
    return _content;
  }

  public void setContent(Dialog content)
  {
    if (_content == content)
      return;
    if (_content != null && _content.getComponentListeners().contains(_moveListener))
      _content.getComponentListeners().remove(_moveListener);
    _content = content;
    if ( ! content.getComponentListeners().contains(_moveListener))
      content.getComponentListeners().add(_moveListener);
  }

  public Dialog getContent()
  {
    return _content;
  }


  /**
   * Creates a new secondary display.
   * Slightly shortened method from {@link DesktopApplicationContext}
   * @param width
   * @param height
   * @param x
   * @param y
   * @param modal
   * @param owner
   */
  private HostDialog createDisplay(int width, int height, int x, int y, boolean modal, boolean resizable,
      boolean undecorated, java.awt.Window owner, DisplayListener displayCloseListener)
  {
    if (DesktopApplicationContext.isFullScreen())
    {
      throw new IllegalStateException();
    }

    final HostDialog hostDialog = new HostDialog(owner, modal, displayCloseListener);
    hostDialog.addWindowFocusListener(_focusListener );
    hostDialog.setLocation(x, y);
    hostDialog.setSize(width, height);
    hostDialog.setResizable(resizable);
    hostDialog.setUndecorated(undecorated);

    return hostDialog;
  }

  /**
   * little helper
   * @param string text to log
   */
  private void log(String string)
  {
    //System.out.println (string);
  }



  /**
   * Create and open an Alert in a blocking way
   * @see Alert#alert(MessageType, String, String, Component, Window, DialogCloseListener)
   * @param messageType
   * @param message
   * @param title
   * @param display
   * @param options Strings or {@link ButtonData}s for the choice PushButtons
   * @return the {@link Alert} created
   */
  static public Alert sAlert (MessageType messageType, String message, String title, Display display, Object... options)
  {
    Alert alert = new Alert (messageType, message, new ArrayList<Object>(options));
    alert.setTitle(title);
    BlockingDialog bDialog = new BlockingDialog(alert);
    bDialog.open(display);

    return alert;
  }

  /**
   * Create and open an Alert with one "OK" Button in a blocking way
   * @see Alert#alert(MessageType, String, String, Component, Window, DialogCloseListener)
   * @param messageType
   * @param message
   * @param title
   * @param display
   * @return the {@link Alert} created
   */
  static public Alert sAlert (MessageType messageType, String message, String title, Display display)
  {
    return sAlert(messageType, message, title, display, "OK");
  }
}
