package xute.markdownrenderer;

public class ItemModel implements RendererViewItem {

  private String type = "";
  private String content = "";
  private String extra = "";

  public void setContent(String content) {
    this.content = content;
  }

  @Override
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Override
  public String getData() {
    return content;
  }

  @Override
  public String getExtra() {
    return extra;
  }

  public void setExtra(String extra) {
    this.extra = extra;
  }
}
