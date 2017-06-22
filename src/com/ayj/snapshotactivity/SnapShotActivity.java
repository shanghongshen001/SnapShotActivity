package com.ayj.snapshotactivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.ayj.snapshotactivity.service.FileService;
import com.example.snapshotactivity.R;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * 获取webView快照与屏幕的截屏
 * 
 */
public class SnapShotActivity extends Activity {
	public static com.itextpdf.text.Rectangle A4 = PageSize.A4;
	private static final String FOLDER_NAME = "/SnapShotImage/";
	private Bitmap bmp = null;
	private WebView webView = null;
	private ImageView image = null;
	private FileService fileService = null;
	private static final String TAG = "SnapShotActivity";

	public byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		return baos.toByteArray();
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_snap_shot);
		// 初始化view
		Button button1 = (Button) findViewById(R.id.btn01);
		Button button2 = (Button) findViewById(R.id.btn02);
		Button button3 = (Button) findViewById(R.id.btn03);
		image = (ImageView) findViewById(R.id.imageView);

		webView = (WebView) findViewById(R.id.webview);
		webView.loadUrl("file:///android_asset/1.html");
		//webView.loadUrl("http://api.idocv.com/view/vDjlLxw");
		webView.setDrawingCacheEnabled(true);
		WebSettings wSet = webView.getSettings();
		wSet.setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) { // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
				view.loadUrl(url);
				return true;
			}
		});
		// webView.setVisibility(View.INVISIBLE);
		fileService = new FileService(this);

		// 获取webView快照
		button1.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {

				bmp = captureWebView(webView);
				image.setBackgroundDrawable(new BitmapDrawable(bmp));
				String fileName = fileService.saveBitmapToSDCard(
						"" + System.currentTimeMillis() + ".png", bmp);
				

				Log.i(TAG, "获取快照");
				Toast.makeText(getApplicationContext(),
						"文件" + fileName + "保存成功！", Toast.LENGTH_LONG).show();
				Intent intent = getPdfFileIntent(Save(bmp));
				startActivity(intent);
			}
		});

		// 获取截屏
		button2.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {

				bmp = captureScreen(SnapShotActivity.this);

				System.out.println("bmpWidth=" + bmp.getWidth());
				System.out.println("bmpHeight=" + bmp.getHeight());

				image.setBackgroundDrawable(new BitmapDrawable(bmp));

				Log.i(TAG, "获取截屏");

				String fileName = fileService.saveBitmapToSDCard(
						"" + System.currentTimeMillis() + ".png", bmp);
				Toast.makeText(getApplicationContext(),
						"文件" + fileName + "保存成功！", Toast.LENGTH_SHORT).show();
				Intent intent = getPdfFileIntent(Save(bmp));
				startActivity(intent);
			}
		});

		// 获取webView显示区域的截图
		button3.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {

				bmp = captureWebViewVisibleSize(webView);

				System.out.println("bmpWidth=" + bmp.getWidth());
				System.out.println("bmpHeight=" + bmp.getHeight());

				image.setBackgroundDrawable(new BitmapDrawable(bmp));

				Log.i(TAG, "获取webView显示区域的截图");

				String fileName = fileService.saveBitmapToSDCard(
						"" + System.currentTimeMillis() + ".png", bmp);
				Toast.makeText(getApplicationContext(),
						"文件" + fileName + "保存成功！", Toast.LENGTH_SHORT).show();
				Intent intent = getPdfFileIntent(Save(bmp));
				startActivity(intent);
			}
		});
	}
	  //Android获取一个用于打开PDF文件的intent    
    public static Intent getPdfFileIntent( File file ){    
   
        Intent intent = new Intent("android.intent.action.VIEW");    
        intent.addCategory("android.intent.category.DEFAULT");    
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);    
        Uri uri = Uri.fromFile(file);    
        intent.setDataAndType(uri, "application/pdf");    
        return intent;    
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
	 * 截取webView可视区域的截图
	 * 
	 * @param webView
	 *            前提：WebView要设置webView.setDrawingCacheEnabled(true);
	 * @return
	 */
	private Bitmap captureWebViewVisibleSize(WebView webView) {
		Bitmap bmp = webView.getDrawingCache();
		return bmp;
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

	/**
	 * 截屏
	 * 
	 * @param context
	 * @return
	 */
	private Bitmap captureScreen(Activity context) {
		View cv = context.getWindow().getDecorView();
		Bitmap bmp = Bitmap.createBitmap(cv.getWidth(), cv.getHeight(),
				Config.ARGB_8888);
		Canvas canvas = new Canvas(bmp);
		cv.draw(canvas);
		return bmp;
	}

	/**
	 * 回收图片
	 */
	public void destoryBitmap() {
		if ((null != bmp) && (!bmp.isRecycled())) {
			bmp.recycle();
			System.out.println("回收图片！");
		}

	}

	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}

}