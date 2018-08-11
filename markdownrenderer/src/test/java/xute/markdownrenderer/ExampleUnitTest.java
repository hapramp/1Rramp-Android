package xute.markdownrenderer;

import org.junit.Test;

import xute.markdownrenderer.utils.MarkdownRendererUtils;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
  @Test
  public void addition_isCorrect() {
   String body = "<center>https://www.google.com</center>";
    body = MarkdownRendererUtils.doSomePreProcessing(body);
    System.out.println(body);
  }
}
