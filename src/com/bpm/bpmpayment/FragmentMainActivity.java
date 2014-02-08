package com.bpm.bpmpayment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;
import com.bpm.adapters.MyFragmentAdapter;
import com.bpm.bpmpayment.json.JSONParser;
import com.viewpagerindicator.TitlePageIndicator;

public class FragmentMainActivity extends FragmentActivity {
	private ProgressDialog pd = null;
	private String usuario, verFragment;
	private int corrida = 0;
	private static JSONObject jObjectClientes = null;
	private static JSONObject jObjectFacturas = null;
	private static JSONObject jObjectProductos = null;
	private UserLoginTask mAuthTaskClientes = null;
	private UserLoginTask mAuthTaskFacturas = null;
	private UserLoginTask mAuthTaskProductos = null;
	private MyFragmentAdapter mAdapter;
	private ViewPager mPager;
	private TitlePageIndicator titleIndicator;
	public ImageView imgClientesIzq = null;
	public ImageView imgClientesDer = null;
	public ImageView imgFacturasIzq = null;
	public ImageView imgFacturasDer = null;
	public ImageView imgProductosIzq = null;
	public ImageView imgProductosDer = null;
	
	public static final String dataAppDirectory = android.os.Environment
			                                       .getExternalStorageDirectory() + File.separator
			                                       + "Android" + File.separator + "data" + File.separator
			                                       + "com.bpm.bpmpayment" + File.separator;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ActionBar action = getActionBar();
		action.setDisplayShowHomeEnabled(false);
		action.setDisplayShowTitleEnabled(false);
		
		this.pd = ProgressDialog.show(this, "Procesando...", "Descargando información...", true, false);
		Intent intent = getIntent();
		usuario = intent.getStringExtra("usuario");
		
		setContentView(R.layout.activity_principal);
		
		File directorio = new File(dataAppDirectory);
		if(!directorio.exists()) {
			directorio.mkdir();
		}
		
		imgClientesIzq = (ImageView) findViewById(R.id.iv_cliente_izquiedo);
		imgClientesDer = (ImageView) findViewById(R.id.iv_cliente_derecho);
		imgFacturasIzq = (ImageView) findViewById(R.id.iv_factura_izquierdo);
		imgFacturasDer = (ImageView) findViewById(R.id.iv_factura_derecho);
		imgProductosIzq = (ImageView) findViewById(R.id.iv_pruducto_izquierdo);
		imgProductosDer = (ImageView) findViewById(R.id.iv_producto_derecho);
		
		imgClientesIzq.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {	
				Toast.makeText(getBaseContext(), "Buscar Cliente", Toast.LENGTH_SHORT).show();
			}
		});
		
		imgClientesDer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {	
				//Toast.makeText(getBaseContext(), "Agregar Cliente", Toast.LENGTH_SHORT).show();
				Intent i = new Intent(getBaseContext(), ClienteAgregar.class);
	            i.putExtra("usuario", usuario);
	            startActivityForResult(i, 1);
			}
		});
		
		imgFacturasIzq.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {	
				Toast.makeText(getBaseContext(), "Ordenar Facturas", Toast.LENGTH_SHORT).show();
			}
		});
		
		imgFacturasDer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {	
				Toast.makeText(getBaseContext(), "Agregar Factura", Toast.LENGTH_SHORT).show();
			}
		});
		
		imgProductosIzq.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {	
				Toast.makeText(getBaseContext(), "Buscar Producto", Toast.LENGTH_SHORT).show();
			}
		});
		
		imgProductosDer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {	
				//Toast.makeText(getBaseContext(), "Agregar Producto", Toast.LENGTH_SHORT).show();
				Intent i = new Intent(getBaseContext(), ProductoAgregar.class);
	            i.putExtra("usuario", usuario);
	            startActivityForResult(i, 1);
			}
		});
				
		mPager = (ViewPager) findViewById(R.id.pager);
		titleIndicator = (TitlePageIndicator) findViewById(R.id.indicator);
		
		List<NameValuePair> paramsClientes = new ArrayList<NameValuePair>();
		paramsClientes.add(new BasicNameValuePair("email", usuario));
		paramsClientes.add(new BasicNameValuePair("obtener", "clientes"));
		corrida = 1;
		mAuthTaskClientes = new UserLoginTask();
		mAuthTaskClientes.execute(paramsClientes);
		
		List<NameValuePair> paramsFacturas = new ArrayList<NameValuePair>();
        paramsFacturas.add(new BasicNameValuePair("email", usuario));
        paramsFacturas.add(new BasicNameValuePair("obtener", "facturas"));
		mAuthTaskFacturas = new UserLoginTask();
		mAuthTaskFacturas.execute(paramsFacturas);
		
		List<NameValuePair> paramsProductos = new ArrayList<NameValuePair>();
        paramsProductos.add(new BasicNameValuePair("email", usuario));
        paramsProductos.add(new BasicNameValuePair("obtener", "productos"));
		mAuthTaskProductos = new UserLoginTask();
		mAuthTaskProductos.execute(paramsProductos);
		
		if(intent.getStringExtra("ver") == null || intent.getStringExtra("ver").isEmpty()) {
			verFragment = "0";
		}
		else {
			verFragment = intent.getStringExtra("ver");
		}
	}
	
	@Override
	public void onBackPressed() {
		if (this.mPager.getCurrentItem() == 0)
			super.onBackPressed();
		else
			this.mPager.setCurrentItem(0);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK) {
			usuario = data.getStringExtra("result");
			verFragment = data.getStringExtra("ver");
			String temp = usuario;
			String tempFrag = verFragment;
			
			Intent refresh = new Intent(this, FragmentMainActivity.class);
			finish();
			refresh.putExtra("usuario", temp);
			refresh.putExtra("ver", tempFrag);
			Log.w("-----------", tempFrag);
	        startActivity(refresh);
		}
		
		if (resultCode == RESULT_CANCELED) {}
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
	}
	
	public class UserLoginTask extends AsyncTask<List<NameValuePair>, Void, String>{
		@Override
		protected String doInBackground(List<NameValuePair>... params) {
			try {return new JSONParser().getJSONFromUrl("http://bpmcart.com/bpmpayment/php/modelo/getCPF_Post.php", params[0]); }
			catch (Exception e) { return null; }
		}

		@Override
		protected void onPostExecute(String result) {
            	try{
	                if(!result.equals("false")) {
	                	if(corrida == 1) {
	                		corrida = 2;
	                		jObjectClientes = new JSONObject(result);
	                	}
	                	else if(corrida == 2) {
	                		corrida = 3;
	                		jObjectFacturas = new JSONObject(result);		            		
	                	}
	                	else if(corrida == 3){
	                		jObjectProductos = new JSONObject(result);
	                		
	                		ArrayList<ImageView> imagenesClientes = new ArrayList<ImageView>();
	                		imagenesClientes.add(imgClientesIzq);
	                		imagenesClientes.add(imgClientesDer);
	                		
	                		ArrayList<ImageView> imagenesFacturas = new ArrayList<ImageView>();
	                		imagenesFacturas.add(imgFacturasIzq);
	                		imagenesFacturas.add(imgFacturasDer);
	                		
	                		ArrayList<ImageView> imagenesProductos = new ArrayList<ImageView>();
	                		imagenesProductos.add(imgProductosIzq);
	                		imagenesProductos.add(imgProductosDer);
	                		
	                		ArrayList<ArrayList<ImageView>> imagenes = new ArrayList<ArrayList<ImageView>>();
	                		imagenes.add(imagenesClientes);
	                		imagenes.add(imagenesFacturas);
	                		imagenes.add(imagenesProductos);
	                		
	                		ArrayList<JSONObject> datos = new ArrayList<JSONObject>();
	                		datos.add(jObjectClientes);
	                		datos.add(jObjectFacturas);
	                		datos.add(jObjectProductos);
	                		
	                		//Al metodo se le agrego el atributo "imagenes"
	                		mAdapter = new MyFragmentAdapter(getSupportFragmentManager(), 3, datos, imagenes);
		            		mPager.setAdapter(mAdapter);
		            		
		            		titleIndicator.setViewPager(mPager);
		            		
		            		if (pd != null) {
		            			pd.dismiss();
			   	            }
		            		
		            		mPager.setCurrentItem(Integer.parseInt(verFragment));
		            		mPager.setOnPageChangeListener(titleIndicator);
		            		
	                	}
	                }
	                else {
	                	Toast.makeText(getBaseContext(), "Credenciales inválidas",Toast.LENGTH_LONG).show();
	                }
	            } catch (JSONException e) {
	                Log.d("ReadJSONFeedTask", "BLAAA");
	                Toast.makeText(getBaseContext(), "Imposible conectarse a la red",Toast.LENGTH_LONG).show();
	            }          
		}

		@Override
		protected void onCancelled() {
		}
	}
}
