package com.simplemobiletools.calendar.pro.net;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.simplemobiletools.calendar.pro.helpers.Config;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class AsyncHttpTask extends AsyncTask<String, Void, String> {
	private Handler handler;
	int DataContent;
	String responseData;
	String fileName;
	String url, myResult;
	Context context;
	ArrayList paramNames, paramValues, files;
	int handlernum = 1;

	// Upload
	private static FileInputStream mFileInputStream = null;
	private static URL connectUrl = null;

	public AsyncHttpTask(Context cx, String urls, Handler handler,
                         ArrayList pNames, ArrayList pValues, ArrayList fe, int hnum, int Data) {
		//Log.i("Test", "asyc callec");
		// Set handler
		this.handler = handler;
		// Set context
		context = cx;
		// set url
		url = urls;
		// Arraylists
		paramNames = pNames;
		paramValues = pValues;
		files = fe;
		// set hanler return number
		handlernum = hnum;
		DataContent = Data;

		super.execute("");
	}
	


	@Override
	protected String doInBackground(String... urls) {
		// urls[0]의 URL부터 데이터를 읽어와 String으로 리턴
		// Log.i("URL", url);
		return Task(url);

	}

	@Override
	public void onPreExecute() {
		// Log.i("Test", "onPreExecute Called on global");
	}

	@Override
	protected void onPostExecute(String responseData) {
		Message msg = handler.obtainMessage();
		msg.what = handlernum;
		msg.obj = myResult;
		msg.arg1 = DataContent;
		handler.sendMessage(msg);
	}

	public String Task(String url) {

		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";

		try {
			connectUrl = new URL(url);

			// open connection
			HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);

			// wri te data
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			OutputStreamWriter out = new OutputStreamWriter(
					conn.getOutputStream(), "UTF-8");// EUC-KR");

			// Check it is null
			if (paramNames != null && paramValues != null) {
                //Public value
				paramNames.add("api_key");
				paramNames.add("auth");
				paramValues.add("xT3FP4AuctM");
				paramValues.add(new Config(context).getAuth());

				for (int i = 0; i < paramNames.size(); i++) {
				//	Log.i("value", paramNames.get(i).toString());
					out.write(twoHyphens + boundary + lineEnd); // 필드 구분자
																// 시작
					out.write("Content-Disposition: form-data; name=\""
							+ paramNames.get(i) + "\"" + lineEnd);
					out.write(lineEnd);
					out.write(paramValues.get(i).toString());
					out.write(lineEnd);
				}
			}

			if (paramNames == null && paramValues ==null) {
				Message msg = handler.obtainMessage();
				 msg.arg2= 0;
			}

			if (files != null) {
				// Log.i("Access", "We can access to files");
				for (int i = 0; i < files.size(); i++) {
					// ======================start
					// fis = new FileInputStream(files.get(files.size()-1));
					mFileInputStream = new FileInputStream(files.get(i)
							.toString());
				//	Log.d("Test", "mFileInputStream  is " + mFileInputStream);
					dos.writeBytes(twoHyphens + boundary + lineEnd);
					dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\""
							+ files.get(i).toString() + "\"" + lineEnd);
					dos.writeBytes(lineEnd);

					int bytesAvailable = mFileInputStream.available();
					int maxBufferSize = 1024;
					int bufferSize = Math.min(bytesAvailable, maxBufferSize);

					byte[] buffer = new byte[bufferSize];
					int bytesRead = mFileInputStream
							.read(buffer, 0, bufferSize);

					// read image
					while (bytesRead > 0) {
						dos.write(buffer, 0, bufferSize);
						bytesAvailable = mFileInputStream.available();
						bufferSize = Math.min(bytesAvailable, maxBufferSize);
						bytesRead = mFileInputStream
								.read(buffer, 0, bufferSize);
					}
					dos.writeBytes(lineEnd);
				}
			}

			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

			if (files != null) {
				mFileInputStream.close();
			}

			dos.flush(); // finish upload...
			out.flush(); // finish upload...

			// get response
			int ch;
			InputStreamReader tmp = new InputStreamReader(
					conn.getInputStream(), "UTF-8");
			BufferedReader reader = new BufferedReader(tmp);
			StringBuilder builder = new StringBuilder();
			String str;

			while ((str = reader.readLine()) != null) { // 서버에서 라인단위로 보내줄
				// 것이므로 라인단위로 읽는다
				builder.append(str); // View에 표시하기 위해 라인 구분자 추가
			}

			myResult = builder.toString(); // 전송결과를 전역 변수에 저장

			dos.close();
			out.close();
			// onPostExecute(myResult);

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
			// Infoalert(context, mod.getString(R.string.error),
			// mod.getString(R.string.error_des), mod.getString(R.string.yes));
			//Change to error code
			handlernum = -1;
		}
		return null;
	}
}
