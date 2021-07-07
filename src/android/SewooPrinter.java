package com.epsilonnet.sewooprinter;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;

import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PermissionHelper;

//import gr.sdk.spire.callbacks.front.paxpos.IPaxPrinterListener;
import gr.sdk.commonsdk.utils.paxdevice.printer.IPaxPrinterListener;
//import gr.sdk.spire.common.debug.Debug;
//import gr.sdk.spire.utils.common.receiptbuilder.ReceiptBuilder;
import gr.sdk.commonsdk.utils.receiptbuilder.ReceiptBuilder;
//import gr.sdk.spire.utils.paxpos.device.printer.PaxPrinter;
import gr.sdk.commonsdk.utils.paxdevice.printer.PaxPrinter;
import gr.sdk.spire.manager.DeviceManager;

public class SewooPrinter extends CordovaPlugin {

    private static final String LOG_TAG = "CardlinkPrinter";

    private String bitString = "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";


    public SewooPrinter() {
    }

    @Override
    public void pluginInitialize() {
        Context cons = webView.getContext();
        DeviceManager.getInstance().initialize(cons);
    }


    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // do something
            }
            return;
        }
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("printText")) {
            try {
                JSONObject res = getPrinterStatus();
                if (res != null && res.getInt("statusCode") == 0) {
                    JSONArray arr = args.getJSONArray(0);
                    ReceiptBuilder receiptBuilder = new ReceiptBuilder(1300);
                    receiptBuilder.setTextSize(80F);
                    //Step Builder
                    for (int i = 0; i < arr.length(); i++) {
                        receiptBuilder.addText(arr.getString(i), true).addBlankSpace(50);
                    }
                    receiptBuilder.addBlankSpace(200);

                    Bitmap bitmap = receiptBuilder.build();
                    IPaxPrinterListener iPaxPrinterListener = new IPaxPrinterListener() {
                        public void onSuccessPrint(String actionCode) {
                            //   Debug.LogInfo(LOG_TAG, "onSuccessPrint $actionCode");
                            callbackContext.success("OK");
                        }

                        public void onFailedPrint(int errorCode, String errorMessage, String actionCode) {
                            callbackContext.error(errorMessage);
                            //Debug.LogInfo(LOG_TAG, "onSuccessPrint $actionCode");
                        }
                    };

                    PaxPrinter.printTheReceipt(bitmap, 300, "1", true, iPaxPrinterListener);
                } else
                    callbackContext.error(res.getString("statusDescription"));
            } catch (Exception e) {
                callbackContext.error(e.getMessage());
            }
            return true;
        } else if (action.equals("printPDF")) {
            try {
                JSONObject res = getPrinterStatus();
                if (res != null && res.getInt("statusCode") == 0) {
                    String base64String = args.getString(0);
                    File file = createPdfFile(base64String);
                    IPaxPrinterListener iPaxPrinterListener = new IPaxPrinterListener() {
                        public void onSuccessPrint(String actionCode) {
                            //   Debug.LogInfo(LOG_TAG, "onSuccessPrint $actionCode");
                            callbackContext.success("OK");
                        }

                        public void onFailedPrint(int errorCode, String errorMessage, String actionCode) {
                            callbackContext.error(errorMessage);
                            //Debug.LogInfo(LOG_TAG, "onSuccessPrint $actionCode");
                        }
                    };

                    ArrayList<Bitmap> bitmaps = pdfToBitmap(file);
                    for (int i = 0; i < bitmaps.size(); i++) {
                        IPaxPrinterListener prm = null;
                        if (i == bitmaps.size() - 1)
                            prm = iPaxPrinterListener;
                        PaxPrinter.printTheReceipt(bitmaps.get(i), 300, "1", true, prm);
                    }

                } else
                    callbackContext.error(res.getString("statusDescription"));
            } catch (Exception e) {
                callbackContext.error(e.getMessage());
            }
            return true;
        } else if (action.equals("printPDFfromPath")) {
            try {
                JSONObject res = getPrinterStatus();
                if (res != null && res.getInt("statusCode") == 0) {
                    String filePath = args.getString(0);
                    File file = new File(filePath);
                    IPaxPrinterListener iPaxPrinterListener = new IPaxPrinterListener() {
                        public void onSuccessPrint(String actionCode) {
                            //   Debug.LogInfo(LOG_TAG, "onSuccessPrint $actionCode");
                            callbackContext.success("OK");
                        }

                        public void onFailedPrint(int errorCode, String errorMessage, String actionCode) {
                            callbackContext.error(errorMessage);
                            //Debug.LogInfo(LOG_TAG, "onSuccessPrint $actionCode");
                        }
                    };

                    ArrayList<Bitmap> bitmaps = pdfToBitmap(file);
                    for (int i = 0; i < bitmaps.size(); i++) {
                        IPaxPrinterListener prm = null;
                        if (i == bitmaps.size() - 1)
                            prm = iPaxPrinterListener;
                        PaxPrinter.printTheReceipt(bitmaps.get(i), 300, "1", true, prm);
                    }

                } else
                    callbackContext.error(res.getString("statusDescription"));
            } catch (Exception e) {
                callbackContext.error(e.getMessage());
            }
            return true;
        } else if (action.equals("printBitmap")) {
            try {
                JSONObject res = getPrinterStatus();
                if (res != null && res.getInt("statusCode") == 0) {
                    String base64String = args.getString(0);
                    Bitmap bitmap = getDecodedBitmap(base64String);
                   // storeImage(bitmap, false);
                    bitmap = getGrayScaleBitmap(bitmap);
                   // storeImage(bitmap, true);
                    IPaxPrinterListener iPaxPrinterListener = new IPaxPrinterListener() {
                        public void onSuccessPrint(String actionCode) {
                            //   Debug.LogInfo(LOG_TAG, "onSuccessPrint $actionCode");
                            callbackContext.success("OK");
                        }

                        public void onFailedPrint(int errorCode, String errorMessage, String actionCode) {
                            callbackContext.error(errorMessage);
                            //Debug.LogInfo(LOG_TAG, "onSuccessPrint $actionCode");
                        }
                    };
                    PaxPrinter.printTheReceipt(bitmap, 300, "1", true, iPaxPrinterListener);
                } else
                    callbackContext.error(res.getString("statusDescription"));
            } catch (Exception e) {
                callbackContext.error(e.getMessage());
            }
            return true;
        } else if (action.equals("getPrinterStatus")) {
            try {
                JSONObject res = getPrinterStatus();
                callbackContext.success(res);
            } catch (Exception e) {
                callbackContext.error(e.getMessage());
            }
            return true;
        }
        return false;
    }

    private JSONObject getPrinterStatus() {
        try {
            JSONObject res = new JSONObject();
            if (!PaxPrinter.printerExist()) {
                res.put("statusCode", -100);
                res.put("statusDescription", "Printer not found");
            }
            int statusCode = PaxPrinter.getPrinterStatus();
            String statusDescription = PaxPrinter.getStringByStatus(statusCode);
            res.put("statusCode", statusCode);
            res.put("statusDescription", statusDescription);
            return res;
        } catch (Exception e) {
            return null;
        }
    }


    private Bitmap getDecodedBitmap(String base64EncodedData) {
        byte[] imageAtBytes = Base64.decode(base64EncodedData.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAtBytes, 0, imageAtBytes.length);
    }

    private File createPdfFile(String base64EncodedData) {
        File dwldsPath = null;
        try {
            dwldsPath = new File(Environment.getExternalStorageDirectory(), "myPdfTemp123.pdf");
            byte[] pdfAsBytes = Base64.decode(base64EncodedData.getBytes(), Base64.DEFAULT);
            FileOutputStream os;
            ActivityCompat.requestPermissions(this.cordova.getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            os = new FileOutputStream(dwldsPath, false);
            os.write(pdfAsBytes);
            os.flush();
            os.close();
        } catch (IOException e) {
            Log.d(LOG_TAG, "File.toByteArray() error");
            e.printStackTrace();
        } finally {
            return dwldsPath;
        }
    }

    private ArrayList<Bitmap> pdfToBitmap(File pdfFile) {
        ArrayList<Bitmap> bitmaps = new ArrayList<>();

        try {
            PdfRenderer renderer = new PdfRenderer(ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY));

            Bitmap bitmap;
            final int pageCount = renderer.getPageCount();
            for (int i = 0; i < pageCount; i++) {
                PdfRenderer.Page page = renderer.openPage(i);
                int width = page.getWidth();
                int height = page.getHeight();
                bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_PRINT);

                bitmaps.add(bitmap);

                // close the page
                page.close();

            }

            // close the renderer
            renderer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return bitmaps;

    }

    private Bitmap getGrayScaleBitmap(Bitmap original) {
        // You have to make the Bitmap mutable when changing the config because there will be a crash
        // That only mutable Bitmap's should be allowed to change config.
        Bitmap bmp = original.copy(Bitmap.Config.ARGB_8888, true);
        Bitmap bmpGrayscale = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0f);
        ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(colorMatrix);
        paint.setColorFilter(colorMatrixFilter);
        canvas.drawBitmap(bmp, 0F, 0F, paint);
        /**
        ArgbEvaluator evaluator = new ArgbEvaluator();

        float fraction = 0 / 100.0F;

        int mul = (int) evaluator.evaluate(fraction, 0xFF7F7F7F, 0xFFFFFFFF);
        int add = (int) evaluator.evaluate(fraction, 0x00000000, 0x00222222);

        Paint lighteningPaint = new Paint();
        lighteningPaint.setColorFilter(new LightingColorFilter(mul, add));
        canvas.drawBitmap(bmpGrayscale, new Matrix(), lighteningPaint);
         */
        return bmpGrayscale;
    }

    /**
     * Create a File for saving an image or video
     */
    private File getOutputMediaFile(boolean isGreyScale) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + webView.getContext().getPackageName()
                + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        String gscale = isGreyScale ? "GScale" : "";
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName = "MI_" + timeStamp + gscale + ".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    private void storeImage(Bitmap image, boolean isGreyScale) {
        File pictureFile = getOutputMediaFile(isGreyScale);
        if (pictureFile == null) {
            Log.d(LOG_TAG,
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(LOG_TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(LOG_TAG, "Error accessing file: " + e.getMessage());
        }
    }

}

