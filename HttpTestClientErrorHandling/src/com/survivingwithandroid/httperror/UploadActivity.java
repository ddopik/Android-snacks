package com.survivingwithandroid.httperror;

/*
 * Copyright (C) 2013 Surviving with Android (http://www.survivingwithandroid.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.survivingwithandroid.httperror.R;



import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UploadActivity extends Activity {

	private MenuItem item;
	private String url = "http://10.0.2.2:8080/TestAndroid/UploadServlet";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload);
		
		final EditText edtTxt1 = (EditText) findViewById(R.id.editTextUpl1);
		final EditText edtTxt2 = (EditText) findViewById(R.id.editTextUpl2);
		Button btnUpl = (Button) findViewById(R.id.upload);
		
		btnUpl.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String param1 = edtTxt1.getText().toString();
				String param2 = edtTxt2.getText().toString();
				
				item.setActionView(R.layout.progress);
				SendHttpRequestTask t = new SendHttpRequestTask();
				
				String[] params = new String[]{url, param1, param2};
				t.execute(params);
			}
		});
		

	}
	
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		item = menu.getItem(0);
		return true;
	}


	private class SendHttpRequestTask extends AsyncTask<String, Void, String> {

		
		@Override
		protected String doInBackground(String... params) {
			String url = params[0];
			String param1 = params[1];
			String param2 = params[2];
			Bitmap b = BitmapFactory.decodeResource(UploadActivity.this.getResources(), R.drawable.logo);
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			b.compress(CompressFormat.PNG, 0, baos);
	
			try {
				HttpClient client = HttpClient.getInstance();
				client.setUrl(url);

				client.connectForMultipart();
				client.addFormPart("param1", param1);
				client.addFormPart("param2", param2);
				client.addFilePart("file", "logo.png", baos.toByteArray());
				client.finishMultipart();
				String data = client.getResponse();
			}
			catch(Throwable t) {
				t.printStackTrace();
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(String data) {			
			item.setActionView(null);
			
		}
		
		
		
	}


}
