package com.bpm.bpmpayment;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import com.bpm.bpmpayment.json.JSONParser;
import com.bpm.bpmpayment.parcelable.ParcelableCliente;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class ClienteEditar extends Activity {
    private ProgressDialog pd = null;
    private DownloadTask mAuthTask = null;
    private EditText nombresView, apellidoPView, apellidoMView, emailView, razonSocialView, rfcView;
	private EditText paisView, estadoView, ciudadView, delegacionView, coloniaView, calleNumeroView, cpView;
	private LinearLayout layoutTelefonosAeditar;
	private String nombres, apellidpP, apellidpM, email, razonSocial, rfc;
	private String pais, estado, ciudad, delegacion, colonia, calleNumero, cp;
	private String usuario, id_cliente;
	private Cliente client;
	private String []tel;
	private int numTelefonos;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_cliente);
        
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); 
        
        Intent intent = getIntent();
        ParcelableCliente parcelableCliente = (ParcelableCliente) intent.getParcelableExtra("cliente");
        client = parcelableCliente.getCliente();
        usuario = intent.getStringExtra("usuario");
        
		nombresView = (EditText) findViewById(R.id.clienteEditaNombres);
		apellidoPView = (EditText) findViewById(R.id.clienteEditaApellidoP);
		apellidoMView = (EditText) findViewById(R.id.clienteEditaApellidoM);
        emailView = (EditText) findViewById(R.id.clienteEditaEmail);
        razonSocialView = (EditText)findViewById(R.id.clienteEditaRazonSocial);
        rfcView = (EditText)findViewById(R.id.clienteEditaRFC);
        
        nombresView.setText(client.getNombres());
        apellidoPView.setText(client.getApellidop());
        apellidoMView.setText(client.getApellidom());
        emailView.setText(client.getEmail());
        razonSocialView.setText(client.getRazon_social());
        rfcView.setText(client.getRfc());
        
        paisView = (EditText) findViewById(R.id.clienteEditaPais);
		estadoView = (EditText) findViewById(R.id.clienteEditaEstado);
		ciudadView = (EditText) findViewById(R.id.clienteEditaCiudad);
        delegacionView = (EditText) findViewById(R.id.clienteEditaDelegacion);
        coloniaView = (EditText)findViewById(R.id.clienteEditaColonia);
        calleNumeroView = (EditText)findViewById(R.id.clienteEditaCalleNumero);
        cpView = (EditText)findViewById(R.id.clienteEditaCP);
        
        layoutTelefonosAeditar = (LinearLayout)findViewById(R.id.layoutEditaTelefonos);
        
    	ImageView addPhone = (ImageView) findViewById(R.id.imageAddCliente);
    	addPhone.setOnClickListener(new View.OnClickListener() {
    	   	@Override
    		public void onClick(View v) {				
    			final LayoutInflater  inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    			LinearLayout ll = (LinearLayout)inflater.inflate(R.layout.layout_phones, null);
    			ImageView iv = (ImageView)ll.getChildAt(2);
    			iv.setOnClickListener(new View.OnClickListener() {
    				@Override
    				public void onClick(View v) {
    					layoutTelefonosAeditar.removeView((View) v.getParent());
    				}
    			});
    			layoutTelefonosAeditar.addView(ll, layoutTelefonosAeditar.getChildCount());
    		}
    	});
    	
    	numTelefonos = numTelefonos(client.getTelefonos());
        tel = client.getTelefonos().split("_");
        
        for (int i=0; i<numTelefonos; i++){
       	     LayoutInflater  inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			 LinearLayout ll = (LinearLayout)inflater.inflate(R.layout.layout_phones, null);
		     
			 String []tel2 = telefonos(tel[i]);
			 Spinner sp = (Spinner)ll.getChildAt(0);
			 sp.setSelection(obtenTipoTelefono(tel2[0]));
			 
			 EditText et = (EditText) ll.getChildAt(1);
			 et.setText(tel2[1]);
			
			 ImageView iv = (ImageView)ll.getChildAt(2);
			 iv.setOnClickListener(new View.OnClickListener() {
			     @Override
				 public void onClick(View v) {
				     layoutTelefonosAeditar.removeView((View) v.getParent());
				 }
			 });
			
			 layoutTelefonosAeditar.addView(ll, layoutTelefonosAeditar.getChildCount());
        }
        
        paisView.setText(client.getPais());
        estadoView.setText(client.getEstado());
        ciudadView.setText(client.getCiudad());
        delegacionView.setText(client.getDelegacion());
        coloniaView.setText(client.getColonia());
        calleNumeroView.setText(client.getCalle_y_num());
        cpView.setText(client.getCp());
    }
    
    private int obtenTipoTelefono(String tipo) {
   	 if(tipo.equals("Casa")){
   		 return 0;
   	 }
   	 else if(tipo.equals("Celular")){
   		 return 1;
   	 }
   	 else if(tipo.equals("Oficina")){
   		 return 2;
   	 }
   	 else {
   		 return 3;
   	 }
    }
    
    private String[] telefonos(String phones) {
		return phones.split(":");
	}
	
	private int numTelefonos(String tels) {
		if(!tels.isEmpty()) {
			return (tels.split("_")).length;
		}
		return 0;
	}
    
    private String eliminaEspacios(String palabras) {
    	return palabras.replaceAll("\\s", "~");
    }
	
	private void esconderTeclado() {
		InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); 
		inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),      
		InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	@SuppressWarnings("unchecked")
	private void editarCliente() {
		nombres = eliminaEspacios(nombresView.getText().toString());
		apellidpP = eliminaEspacios(apellidoPView.getText().toString());
		apellidpM = eliminaEspacios(apellidoMView.getText().toString());
		email = emailView.getText().toString();
		razonSocial = razonSocialView.getText().toString();
		rfc = rfcView.getText().toString();
		pais = paisView.getText().toString();
		estado = estadoView.getText().toString();
		ciudad = ciudadView.getText().toString();
		delegacion = delegacionView.getText().toString();
		colonia = coloniaView.getText().toString();
		calleNumero = calleNumeroView.getText().toString();
		cp = cpView.getText().toString();
				
		ArrayList<String> tels = new ArrayList<String>();
		ArrayList<String> typeTels = new ArrayList<String>();
		
		int childcount = layoutTelefonosAeditar.getChildCount();
		for (int i=1; i < childcount; i++){
		      LinearLayout tempView = (LinearLayout)layoutTelefonosAeditar.getChildAt(i);
		      int hijos = tempView.getChildCount();
		      
		      for(int j = 0 ;j < hijos ; j++) {
		    	  if( tempView.getChildAt(j) instanceof EditText ) {
		    		  if(!((EditText)tempView.getChildAt(j)).getText().toString().equals("")) {
		    			  String telefono =  ((EditText)tempView.getChildAt(j)).getText().toString();
			    		  tels.add(telefono);
		    		  }
		    	  }
		    	  
		    	  if( tempView.getChildAt(j) instanceof Spinner ) {
		    	      String tipoTelefono =  ((Spinner)tempView.getChildAt(j)).getSelectedItem().toString();
		    	      typeTels.add(tipoTelefono);
		    	  }
		      }
		}
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("nombres", nombres));
        params.add(new BasicNameValuePair("apellidoP", apellidpP));
        params.add(new BasicNameValuePair("apellidoM", apellidpM));
        params.add(new BasicNameValuePair("emailCliente", email));
        params.add(new BasicNameValuePair("razonSocial", razonSocial));
        params.add(new BasicNameValuePair("rfc", rfc));
        params.add(new BasicNameValuePair("pais", pais));
        params.add(new BasicNameValuePair("estado", estado));
        params.add(new BasicNameValuePair("ciudad", ciudad));
        params.add(new BasicNameValuePair("delegacion", delegacion));
        params.add(new BasicNameValuePair("colonia", colonia));
        params.add(new BasicNameValuePair("calleNumero", calleNumero));
        params.add(new BasicNameValuePair("cp", cp));
        
		params.add(new BasicNameValuePair("numTelefonos", String.valueOf(tels.size())));
		for(int i = 0 ; i < tels.size() ; i++) {
			params.add(new BasicNameValuePair("telefono" + String.valueOf(i+1), tels.get(i)));
		}
		
		params.add(new BasicNameValuePair("numTipoTelefonos", String.valueOf(typeTels.size())));
		for(int i = 0 ; i < typeTels.size() ; i++) {
			params.add(new BasicNameValuePair("tipoTelefono" + String.valueOf(i+1), typeTels.get(i)));
		}
		
		params.add(new BasicNameValuePair("idCliente", id_cliente));
		
		esconderTeclado();
		ClienteEditar.this.pd = ProgressDialog.show(ClienteEditar.this, "Procesando...", "Registrando datos...", true, false);
		mAuthTask = new DownloadTask();
		mAuthTask.execute(params);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.client_edit_actions, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.action_cancel:
	        	esconderTeclado();
			    Intent returnIntent = new Intent();
        		returnIntent.putExtra("result", usuario);
        		returnIntent.putExtra("ver", "0");
        		setResult(RESULT_CANCELED,returnIntent);     
        		finish();
	            return true;
	        case R.id.action_edit:
	        	editarCliente();
	            return true;
	        case android.R.id.home:
	        	esconderTeclado();
			    Intent returnIntent2 = new Intent();
        		returnIntent2.putExtra("result", usuario);
        		returnIntent2.putExtra("ver", "0");
        		setResult(RESULT_CANCELED,returnIntent2);     
        		finish();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

    private class DownloadTask extends AsyncTask<List<NameValuePair>, Void, String> {
         protected String doInBackground(List<NameValuePair>... params) {
        	try {
      			return new JSONParser().getJSONFromUrl("http://bpmcart.com/bpmpayment/php/modelo/updateClient_Post.php", params[0]);
      		} catch (Exception e) {
      			return null;
      		}
         }

         protected void onPostExecute(String result) {
             if (ClienteEditar.this.pd != null) {
	             ClienteEditar.this.pd.dismiss();
	             Toast.makeText(getBaseContext(), "Cliente Actualizado", Toast.LENGTH_SHORT).show();
             	 Intent returnIntent = new Intent();
             	 returnIntent.putExtra("result", usuario);
             	 returnIntent.putExtra("ver", "0");
             	 setResult(RESULT_OK,returnIntent);     
             	 finish();
	         }
         }
         
         private int obtenTipoTelefono(String tipo) {
        	 if(tipo.equals("Casa")){
        		 return 0;
        	 }
        	 else if(tipo.equals("Celular")){
        		 return 1;
        	 }
        	 else if(tipo.equals("Oficina")){
        		 return 2;
        	 }
        	 else {
        		 return 3;
        	 }
         }
    }    
}