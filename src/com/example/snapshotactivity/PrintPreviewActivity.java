package com.example.snapshotactivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.ayj.snapshotactivity.service.FileService;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

public class PrintPreviewActivity extends Activity {

	String htmlUrl;
	Object printModel;
	WebView webView;
	Button btnPrint;

	public static com.itextpdf.text.Rectangle A4 = PageSize.A4;
	private static final String FOLDER_NAME = "/SnapShotImage/";
	Bitmap bmp = null;
	FileService fileService = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_print_preview);

		initView();
		initData();
		initEvent();
	}

	private void initView() {
		webView = (WebView) findViewById(R.id.webview);
		btnPrint = (Button)this.findViewById(R.id.btnPrint);
	}

	private void initEvent() {
		btnPrint.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				webView.setInitialScale(100);
				Timer timer = new Timer();  
				MyTimerTask timerTask = new MyTimerTask();  
				timer.schedule(timerTask, 1000, 1); // 延迟3秒钟,执行1次  
			}
		});
	}
	
	class MyTimerTask extends TimerTask {  
	    @Override  
	    public void run() {  
	    	PrintPreviewActivity.this.runOnUiThread(new Runnable(){
				@Override
				public void run() {
					try {
						bmp = captureWebView(webView);
						String fileName = fileService.saveBitmapToSDCard(
								"" + System.currentTimeMillis() + ".png", bmp);
						Toast.makeText(getApplicationContext(),
								"文件" + fileName + "保存成功！", Toast.LENGTH_LONG).show();
						Intent intent = getPdfFileIntent(Save(bmp));
						startActivity(intent);
						
					} catch (final Exception e) {
						Toast.makeText(PrintPreviewActivity.this, e.getMessage(), Toast.LENGTH_LONG);
					}
				}
	    	});
	    	this.cancel();
	    }  
	}  

	private void initData() {
		fileService = new FileService(this);
		printModel = getIntent().getSerializableExtra("printData");
		htmlUrl = getIntent().getStringExtra("htmlUrl");
		webView.loadUrl("file:///android_asset/1.html");
		webView.setDrawingCacheEnabled(true);
		
		WebSettings wSet = webView.getSettings();
		wSet.setJavaScriptEnabled(true);
		webView.addJavascriptInterface(new AndroidToastForJs(this,printModel), "jsobj");
		
		wSet.setJavaScriptCanOpenWindowsAutomatically(true);
		// 设置可以支持缩放 
		wSet.setSupportZoom(true); 
		// 设置出现缩放工具 
		wSet.setBuiltInZoomControls(true);
		//扩大比例的缩放
		wSet.setUseWideViewPort(true);
		//自适应屏幕
		wSet.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		wSet.setLoadWithOverviewMode(true);
		
		webView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) { // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
				view.loadUrl(url);
				return true;
			}			
		});
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					final JsResult result) {
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
				result.confirm();
				return true;
			}
		});
	}
  public static Intent getPdfFileIntent( File file ){    
	   
        Intent intent = new Intent("android.intent.action.VIEW");    
        intent.addCategory("android.intent.category.DEFAULT");    
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);    
        Uri uri = Uri.fromFile(file);    
        intent.setDataAndType(uri, "application/pdf");    
        return intent;    
    }
	public byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		return baos.toByteArray();
	}
	private File Save(Bitmap bitmap) {

		File localFile = new File(Environment.getExternalStorageDirectory()
				+ FOLDER_NAME + System.currentTimeMillis() + ".pdf");

		Document localDocument = new Document(A4);
		localDocument.setMargins(0.0F, 0.0F, 0.0F, 0.0F);
		PdfWriter localPdfWriter = null;
		while (true) {
			Image localImage = null;
			try {
				localPdfWriter = PdfWriter.getInstance(localDocument,
						new FileOutputStream(localFile));
				localPdfWriter.open();
				localDocument.open();
				localImage = Image.getInstance(Bitmap2Bytes(bmp));
				float f1 = localImage.getHeight() / localImage.getWidth();
				float f2 = A4.getHeight() / A4.getWidth();
				

				localImage.scaleToFit(A4.getWidth(), A4.getHeight());
				localDocument.add(localImage);
				return localFile;
				
				/*if (f1 == f2) {
					localImage.scaleToFit(A4.getWidth(), A4.getHeight());
					localDocument.add(localImage);
					return localFile;
				}
				if (f1 < f2) {
					localImage.scaleToFit(A4.getWidth(), A4.getWidth()
							/ localImage.getWidth() * localImage.getHeight());
					continue;
				}*/
			} catch (Exception localException) {
			} finally {
				try {
					if (localPdfWriter != null)
						localPdfWriter.close();
					if (localDocument != null)
						localDocument.close();
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
			localImage.scaleToFit(A4.getHeight() / localImage.getHeight()
					* localImage.getWidth(), A4.getHeight());
		}
	}

	/**
	 * 截取webView快照(webView加载的整个内容的大小)
	 * 
	 * @param webView
	 * @return
	 */
	private Bitmap captureWebView(WebView webView) {
		Picture snapShot = webView.capturePicture();
		Bitmap bmp = Bitmap.createBitmap(snapShot.getWidth(),
				snapShot.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bmp);
		snapShot.draw(canvas);
		return bmp;
	}

	
	
	public class AndroidToastForJs {
		
		private Context mContext;
		private Object printModel;

		public AndroidToastForJs(Context context,Object printModel){
				this.mContext = context;
				this.printModel = printModel;
		}
		
		//webview中调用toast原生组件
		public void showToast(String toast) {
				Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
		}
			
		 //以json实现webview与js之间的数据交互
		public String jsontohtml(){
				return JSONHelper.toJSON(printModel);
		}
	}
}
