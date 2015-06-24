package pivot_contrib.guretzki.test;

import java.io.StringBufferInputStream;

import org.apache.pivot.beans.BXMLSerializer;
import org.apache.pivot.collections.ArrayList;
import org.apache.pivot.collections.Map;
import org.apache.pivot.wtk.Alert;
import org.apache.pivot.wtk.Application;
import org.apache.pivot.wtk.DesktopApplicationContext;
import org.apache.pivot.wtk.Dialog;
import org.apache.pivot.wtk.Display;
import org.apache.pivot.wtk.MessageType;
import org.apache.pivot.wtk.Window;

import pivot_contrib.guretzki.BlockingDialog;


/**
 * Demonstrates the {@link BlockingDialog} Class
 * @author thomas.guretzki
 *
 */
@SuppressWarnings("deprecation")
public class BlockingDialogTest implements Application
{
  static String sWindowDescription;

  @Override
  public void startup(Display display, Map<String, String> properties) throws Exception
  {
    log ("Start");

    ///// Prepare background
    log (sWindowDescription);
    BXMLSerializer bxmlSerializer = new BXMLSerializer();
    Window window = (Window) bxmlSerializer.readObject(new StringBufferInputStream(sWindowDescription));
    window.open(display);
    ///// Prepare and open BlockingDialog
    BlockingDialog bd = new BlockingDialog();
    bd.setContent(new Alert(MessageType.QUESTION, "Why?", new ArrayList<String>("Yes","No")));
    log("before BlockingDialog");
    Dialog erg = bd.open(display);
    log ("after BlockingDialog: " + erg.getResult() + ", selected:" + ((Alert)erg).getSelectedOptionIndex());
  }

  public void log(String text)
  {
    System.out.println (text);
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


  static public void main (String[] args)
  {
    DesktopApplicationContext.main(BlockingDialogTest.class, args);
  }

  static
  {
    sWindowDescription =
      "<Window title='ACME VeryImportant Application' maximized='true'"
     +"  xmlns='org.apache.pivot.wtk'"
     +"  xmlns:content='org.apache.pivot.wtk.content' xmlns:bxml='http://pivot.apache.org/bxml'>"
     +"    <BoxPane orientation='VERTICAL'>" +
             "    <Label text='ACME VeryImportant Application' styles='{font:{size:\"300%\"},padding:20}'/>" +
             "    <Label text='...urgently in need of a user decision...'/>" +
             "  </BoxPane>"
     +"</Window>";
  }
}
