package com.example.sprint;

import com.mirasense.scanditsdk.ScanditSDKAutoAdjustingBarcodePicker;
import com.mirasense.scanditsdk.interfaces.ScanditSDK;
import com.mirasense.scanditsdk.interfaces.ScanditSDKListener;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class ScannerActivity extends Activity implements ScanditSDKListener{

    // The main object for recognizing a displaying barcodes.
    private ScanditSDK mBarcodePicker;
    
    // Enter your Scandit SDK App key here.
    // Your Scandit SDK App key is available via your Scandit SDK web account.
    public static final String sScanditSdkAppKey = "mmMSMpuiEeKCffNGhQHFH0C0OHhGPQBgQL81+Kg0pO0";

    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize and start the bar code recognition.
        initializeAndStartBarcodeScanning();
    }
    
    @Override
    protected void onPause() {
        // When the activity is in the background immediately stop the 
        // scanning to save resources and free the camera.
        mBarcodePicker.stopScanning();
        super.onPause();
    }
    
    @Override
    protected void onResume() {
        // Once the activity is in the foreground again, restart scanning.
        mBarcodePicker.startScanning();
        super.onResume();
    }

    /**
     * Initializes and starts the bar code scanning.
     */
    public void initializeAndStartBarcodeScanning() {
        // Switch to full screen.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        // We instantiate the automatically adjusting barcode picker that will
        // choose the correct picker to instantiate. Be aware that this picker
        // should only be instantiated if the picker is shown full screen as the
        // legacy picker will rotate the orientation and not properly work in
        // non-fullscreen.
        ScanditSDKAutoAdjustingBarcodePicker picker = new ScanditSDKAutoAdjustingBarcodePicker(
                    this, sScanditSdkAppKey, ScanditSDKAutoAdjustingBarcodePicker.CAMERA_FACING_BACK);
        
        // Add both views to activity, with the scan GUI on top.
        setContentView(picker);
        mBarcodePicker = picker;
        
        // Register listener, in order to be notified about relevant events 
        // (e.g. a successfully scanned bar code).
        mBarcodePicker.getOverlayView().addListener(this);
        
        // show search bar in scan user interface
        mBarcodePicker.getOverlayView().showSearchBar(true);
        
        // In the old version, the title and tool bar can be hidden as follows:
        //mBarcodePicker.getOverlayView().showTitleBar(false);
        //mBarcodePicker.getOverlayView().showToolBar(false);
        
        // To activate recognition of 2d codes
        mBarcodePicker.setQrEnabled(true);
        mBarcodePicker.setDataMatrixEnabled(true);
    }

    /** 
     *  Called when a barcode has been decoded successfully.
     *  
     *  @param barcode Scanned barcode content.
     *  @param symbology Scanned barcode symbology.
     */
    public void didScanBarcode(String barcode, String symbology) {
        // Remove non-relevant characters that might be displayed as rectangles
        // on some devices. Be aware that you normally do not need to do this.
        // Only special GS1 code formats contain such characters.
        String cleanedBarcode = "";
        for (int i = 0 ; i < barcode.length(); i++) {
            if (barcode.charAt(i) > 30) {
                cleanedBarcode += barcode.charAt(i);
            }
        }

        Toast.makeText(this, symbology + ": " + cleanedBarcode, 10000).show();
        // Example code that would typically be used in a real-world app using 
        // the Scandit SDK.
        /*
        // Access the image in which the bar code has been recognized.
        byte[] imageDataNV21Encoded = barcodePicker.getCameraPreviewImageOfFirstBarcodeRecognition();
        int imageWidth = barcodePicker.getCameraPreviewImageWidth();
        int imageHeight = barcodePicker.getCameraPreviewImageHeight();
        
        // Stop recognition to save resources.
        mBarcodePicker.stopScanning();
        */
    }
    
    /** 
     * Called when the user entered a bar code manually.
     * 
     * @param entry The information entered by the user.
     */
    public void didManualSearch(String entry) {
        // Example code that would typically be used in a real-world app using 
        // the Scandit SDK.
        
    	Toast.makeText(this, "User entered: " + entry, 10000).show();
    }
    
    @Override
    public void didCancel() {
        mBarcodePicker.stopScanning();
        finish();
    }
    
    @Override
    public void onBackPressed() {
        mBarcodePicker.stopScanning();
        finish();
    }
}



