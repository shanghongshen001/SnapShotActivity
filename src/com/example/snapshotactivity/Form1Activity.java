package com.example.snapshotactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Form1Activity extends Activity {

	EditText anhao;//案号
	EditText xfa;//_罚
	EditText qiangcuo;//强措
	EditText xhao;//_号
	EditText dName;//公民姓名
	EditText dIdCard;//公民身份证号
	EditText dAdress;//公民住址
	EditText dPhone;//公民联系电话
	EditText fName;//法人名称
	EditText fAdress;//法人住址
	EditText fPhone;//法人联系电话
	EditText fDPersion;//法定代表人
	Button btnPrint;//打印预览
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_form1);
		initView();
	}
	
	private void initView(){
		 anhao = (EditText)this.findViewById(R.id.anhao);//案号
		 xfa = (EditText)this.findViewById(R.id.xfa);//_罚
		 qiangcuo = (EditText)this.findViewById(R.id.qiangcuo);//强措
		 xhao = (EditText)this.findViewById(R.id.xhao);//_号
		 dName = (EditText)this.findViewById(R.id.dName);//公民姓名
		 dIdCard = (EditText)this.findViewById(R.id.dIdCard);//公民身份证号
		 dAdress = (EditText)this.findViewById(R.id.dAdress);//公民住址
		 dPhone = (EditText)this.findViewById(R.id.dPhone);//公民联系电话
		 fName = (EditText)this.findViewById(R.id.fName);//法人名称
		 fAdress = (EditText)this.findViewById(R.id.fAdress);//法人住址
		 fPhone = (EditText)this.findViewById(R.id.fPhone);//法人联系电话
		 fDPersion = (EditText)this.findViewById(R.id.fDPersion);//法定代表人
		 btnPrint = (Button)this.findViewById(R.id.btnPrint);
		 
		 initEvent();
	}
	private void initEvent(){
		btnPrint.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Form1Model model = new Form1Model();
				model.anhao = anhao.getText().toString();
				model.xfa = xfa.getText().toString();
				model.qiangcuo = qiangcuo.getText().toString();
				model.xhao = xhao.getText().toString();
				model.dName = dName.getText().toString();
				model.dIdCard = dIdCard.getText().toString();
				model.dAdress = dAdress.getText().toString();
				model.dPhone = dPhone.getText().toString();
				model.fName = fName.getText().toString();
				model.fAdress = fAdress.getText().toString();
				model.fPhone = fPhone.getText().toString();
				model.fDPersion = fDPersion.getText().toString();
				
				Intent intent = new Intent();  
				intent.setClass(Form1Activity.this, PrintPreviewActivity.class);  
				// 新建Bundle对象  
				Bundle mBundle = new Bundle();    
				// 放入account对象  
				mBundle.putSerializable("printData", model);    
				intent.putExtra("htmlUrl", "");
				intent.putExtras(mBundle);    
				startActivity(intent);  
			}
		});
	}
}
