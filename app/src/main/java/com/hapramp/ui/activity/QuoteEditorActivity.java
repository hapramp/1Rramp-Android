package com.hapramp.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.utils.FontManager;
import com.hapramp.views.AutoFitEditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class QuoteEditorActivity extends AppCompatActivity {
  public static final String[] colors = new String[]{
    "#3F72AF",
    "#F44336",
    "#E91E63",
    "#9C27B0",
    "#673AB7",
    "#3F51B5",
    "#2196F3",
    "#00BCD4",
    "#009688",
    "#4CAF50",
    "#8BC34A",
    "#FF9800",
    "#FF5722",
    "#795548",
    "#9E9E9E",
    "#607D8B",
    "#000000"
  };

  public static final String[] fonts = new String[]{
    FontManager.FONT_ROBOTO,
    FontManager.FONT_LOBSTER,
    FontManager.FONT_RALEWAY,
    FontManager.FONT_SHRINKLAND
  };
  public static final String QUOTE_IMAGE_URL = "quote_image_url";
  RelativeLayout quoteContainer;
  ImageView frameLayout;
  RelativeLayout rootView;
  AutoFitEditText quoteEt;
  AppCompatEditText quoteSource;
  ImageView colorSwitcher;
  TextView fontSwitcher;

  private int currentFontIndex = 0;
  private int currentColorIndex = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_quote_editor);
    AutoFitEditText editText = findViewById(R.id.quote_box);
    quoteContainer = findViewById(R.id.quote_container);
    frameLayout = findViewById(R.id.sendBtn);
    rootView = findViewById(R.id.root);
    quoteEt = findViewById(R.id.quote_box);
    quoteSource = findViewById(R.id.quote_source);
    colorSwitcher = findViewById(R.id.switchColorBtn);
    fontSwitcher = findViewById(R.id.switchFontBtn);
    colorSwitcher.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        changeColor();
      }
    });
    fontSwitcher.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        changeFont();
      }
    });
    frameLayout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        handleSendButton();
      }
    });
    editText.setMinTextSize(80f);
  }

  private void changeColor() {
    if (currentColorIndex == colors.length - 1) {
      currentColorIndex = 0;
    } else {
      currentColorIndex++;
    }
    int color = Color.parseColor(colors[currentColorIndex]);
    rootView.setBackgroundColor(color);
    quoteContainer.setBackgroundColor(color);
  }

  private void changeFont() {
    if (currentFontIndex == fonts.length - 1) {
      currentFontIndex = 0;
    } else {
      currentFontIndex++;
    }
    Typeface typeface = FontManager.getInstance().getTypeFace(fonts[currentFontIndex]);
    quoteSource.setTypeface(typeface);
    quoteEt.setTypeface(typeface);
    fontSwitcher.setTypeface(typeface);
  }

  private void handleSendButton() {
    //remote focus from edit text
    createBitmapFromView();
  }

  private void createBitmapFromView() {
    //measure the view
    int height = quoteContainer.getMeasuredHeight();
    int width = quoteContainer.getMeasuredWidth();
    Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    quoteContainer.draw(canvas);
    writeBitmapToFile(bitmap);
  }

  private void writeBitmapToFile(Bitmap bmp) {
    File file = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + "quote_image.png");
    try {
      FileOutputStream out = new FileOutputStream(file);
      bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
      sendBackResult(file.getAbsolutePath());
    }
    catch (IOException e) {
      e.printStackTrace();
      sendBackResult(null);
    }
  }

  private void sendBackResult(String url) {
    Intent mIntent = new Intent();
    if (url != null) {
      mIntent.putExtra(QUOTE_IMAGE_URL, url);
      setResult(RESULT_OK, mIntent);
    } else {
      setResult(RESULT_CANCELED, mIntent);
    }
    finish();
  }
}
