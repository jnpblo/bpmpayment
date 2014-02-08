package com.bpm.bpmpayment;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.bpm.bpmpayment.json.JSONParser;

public class LoginActivity extends Activity {
	private UserLoginTask mAuthTask = null;
	public static String mEmail;
	private String mPassword;

	private EditText mEmailView;
	private EditText mPasswordView;
	private ProgressDialog pd;
	
	public boolean login;
	public String storedMail;
	public String storedPass;
	public String pass;
	public String email;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		SharedPreferences storedPreferences = getSharedPreferences("datos",Context.MODE_PRIVATE);
		storedMail = storedPreferences.getString("mail", "");
		storedPass = storedPreferences.getString("pass", "");
		
		if(!storedMail.equals("") && !storedPass.equals("")){
			pass = storedPass;
			email = storedMail;
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
	        params.add(new BasicNameValuePair("email", email));
	        params.add(new BasicNameValuePair("password", pass));	
			
			mAuthTask = new UserLoginTask();
			mAuthTask.execute(params);
		}
		
		setContentView(R.layout.activity_login);
		mEmailView = (EditText) findViewById(R.id.email);
		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						if (!attemptLogin()) {
							if(checkConnectivity()) {
								pass = (storedPass.equals(""))? mPasswordView.getText().toString():storedPass;
								email = (storedMail.equals(""))? mEmailView.getText().toString():storedMail;
								
								pass =  mPasswordView.getText().toString();
								email = mEmailView.getText().toString();
								
								List<NameValuePair> params = new ArrayList<NameValuePair>();
						        params.add(new BasicNameValuePair("email", email));
						        params.add(new BasicNameValuePair("password", pass));						
								
								mAuthTask = new UserLoginTask();
								mAuthTask.execute(params);
							}
							else {
								Toast.makeText(getBaseContext(), "No tiene acceso a internet", Toast.LENGTH_SHORT).show();
							}
						}
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	public boolean attemptLogin() {
		boolean cancelar = false;
		
		mEmailView.setError(null);
		mPasswordView.setError(null);

		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError("Este campo es Obligatorio");
			cancelar = true;
		}
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError("Este campo es Obligatorio");
			cancelar = true;
		}
		else if (!mEmail.contains("@")) {
			mEmailView.setError("Este campo es Obligatorio");
			cancelar = true;
		}

		esconderTeclado();
		return cancelar;
	}
	
	private void esconderTeclado() {
		InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); 
		inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),      
		InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	private boolean checkConnectivity()
    {
		Context context = getApplicationContext();
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(
		        Context.CONNECTIVITY_SERVICE);

		    NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		    if (wifiNetwork != null && wifiNetwork.isConnected()) {
		      return true;
		    }

		    NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		    if (mobileNetwork != null && mobileNetwork.isConnected()) {
		      return true;
		    }

		    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		    if (activeNetwork != null && activeNetwork.isConnected()) {
		      return true;
		    }

		    return false;        
    }

	public class UserLoginTask extends AsyncTask<List<NameValuePair>, Void, String>{
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(LoginActivity.this);
			pd.setTitle("Processing...");
			pd.setMessage("Please wait.");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}
		
		@Override
		protected String doInBackground(List<NameValuePair>... params) {
			try {
				return new JSONParser().getJSONFromUrl("http://bpmcart.com/bpmpayment/php/modelo/loginPost.php", params[0]);
			} catch (Exception e) {
				Log.w("Error doInBackground", e.getMessage());
				return null;
			}
		}

		@Override
		protected void onPostExecute(String result) {
			mAuthTask = null;
            	try{
	                if(!result.equals("false")) {
	                	JSONObject jObject  = new JSONObject(result);
	                	String usuario = jObject.getString("email");
	                			                
		                Intent i = new Intent(getApplicationContext(), FragmentMainActivity.class);
		                i.putExtra("usuario", usuario);
		                
		                SharedPreferences storedPreferences = getSharedPreferences("datos",Context.MODE_PRIVATE);
		                Editor editor = storedPreferences.edit();
		                editor.putString("mail", email);
		                editor.putString("pass", pass);
		                editor.commit();
		                
						startActivity(i);
						pd.dismiss();
						finish();
	                }
	                else {
	                	Toast.makeText(getBaseContext(), "Credenciales inválidas",Toast.LENGTH_LONG).show();
	                }
	            } catch (Exception e) {
	            	pd.dismiss();
	                Log.d("ReadJSONFeedTask", e.getMessage());
	                Toast.makeText(getBaseContext(), "Imposible conectar",Toast.LENGTH_LONG).show();
	            }          
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			pd.dismiss();
		}
	}
}
