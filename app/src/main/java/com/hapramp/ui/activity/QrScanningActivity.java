package com.hapramp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.vision.barcode.Barcode;
import com.hapramp.R;
import com.hapramp.analytics.AnalyticsParams;
import com.hapramp.analytics.AnalyticsUtil;
import com.hapramp.utils.FontManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.androidhive.barcode.BarcodeReader;

public class QrScanningActivity extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener {

  public static final String EXTRAA_PPK = "ppk";
  @BindView(R.id.closeBtn)
  TextView closeBtn;
  @BindView(R.id.toolbar_title)
  TextView toolbarTitle;
  @BindView(R.id.toolbar_container)
  RelativeLayout toolbarContainer;
  private BarcodeReader barcodeReader;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_qr_scanning);
    ButterKnife.bind(this);
    barcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(R.id.barcode_scanner);
    closeBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
    closeBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
  }

  @Override
  public void onScanned(Barcode barcode) {
    onScannedBarCode(barcode);
  }

  @Override
  public void onScannedMultiple(List<Barcode> barcodes) {

  }

  @Override
  public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

  }

  @Override
  public void onScanError(String errorMessage) {

  }

  @Override
  public void onCameraPermissionDenied() {

  }

  private void onScannedBarCode(Barcode barcode) {
    barcodeReader.playBeep();
    Intent data = new Intent();
    data.putExtra(EXTRAA_PPK, barcode.displayValue);
    setResult(RESULT_OK, data);
    AnalyticsUtil.logEvent(AnalyticsParams.EVENT_SCAN);
    finish();
  }

}
