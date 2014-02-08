package com.bpm.bpmpayment;

import com.bpm.bpmpayment.parcelable.ParcelableCliente;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ClienteDetalles extends Activity{
	private Cliente client;
	private String usuario;	
	private TextView detalleEmail;
	private TextView detalleRazonSocial;
	private TextView detalleRfc;
	private TextView detalleDireccion;
	private LinearLayout layoutTelefonosAmostrar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detalle_cliente);
		
		ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); 
		
        Intent intent = getIntent();
        ParcelableCliente parcelableCliente = (ParcelableCliente) intent.getParcelableExtra("cliente");
        client = parcelableCliente.getCliente();
        usuario = intent.getStringExtra("usuario");
        
        setTitle(client.getNombres() + " " + client.getApellidop() + " " + client.getApellidom());
        
        detalleEmail = (TextView) findViewById(R.id.detalle_cliente_email);
    	detalleRazonSocial = (TextView) findViewById(R.id.detalle_cliente_Razon_Social);
    	detalleRfc = (TextView) findViewById(R.id.detalle_cliente_RFC);
    	detalleDireccion = (TextView) findViewById(R.id.detalle_cliente_pais);
    	
    	detalleEmail.setText(client.getEmail());
    	detalleRazonSocial.setText(client.getRazon_social());
    	detalleRfc.setText(client.getRfc());
    	detalleDireccion.setText(client.getCalle_y_num() + " " + 
    	                         client.getColonia() + "\n" + 
    			                 client.getDelegacion() + " " + 
    	                         client.getCp() + "\n" + 
    			                 client.getPais() + " " + 
    	                         client.getEstado());
    	
    	layoutTelefonosAmostrar = (LinearLayout)findViewById(R.id.layout_detalle_cliente_telefonos);
    	
    	int numTelefonos = numTelefonos(client.getTelefonos());
    	
    	if(numTelefonos != 0) {
    		String []tel = client.getTelefonos().split("_");
    		
    		for(int i = 0 ; i < numTelefonos ; i++) {
    			LayoutInflater  inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        		LinearLayout ll = (LinearLayout)inflater.inflate(R.layout.layout_phones_detalles, null);    
        		String []tel2 = telefonos(tel[i]); 
        		TextView et = (TextView) ll.getChildAt(0);
        		
    			if(i != numTelefonos - 1) {				
            		et.setText(tel2[0] + "\n" + tel2[1] + "\n");
            		layoutTelefonosAmostrar.addView(ll, layoutTelefonosAmostrar.getChildCount());
    			}
    			else {
            		et.setText(tel2[0] + "\n" + tel2[1]);
            		layoutTelefonosAmostrar.addView(ll, layoutTelefonosAmostrar.getChildCount());
    			}
        	}
    	}
    	
    	else {
    		LayoutInflater  inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		LinearLayout ll = (LinearLayout)inflater.inflate(R.layout.layout_phones_detalles, null);
    		TextView et = (TextView) ll.getChildAt(0);
    		et.setText("No tiene registrado ningún teléfono");
    		layoutTelefonosAmostrar.addView(ll, layoutTelefonosAmostrar.getChildCount());
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK) {
			usuario = data.getStringExtra("result");	        
	        Intent returnIntent = new Intent();
    		returnIntent.putExtra("result", usuario);
    		returnIntent.putExtra("ver", "0");
    		setResult(RESULT_OK,returnIntent);     
    		finish();
		}
		
		if (resultCode == RESULT_CANCELED) {}
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.client_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.editar_cliente:
        	ParcelableCliente parcelableCliente = new ParcelableCliente(client);
        	Intent i = new Intent(getBaseContext(), ClienteEditar.class);
	        i.putExtra("cliente", parcelableCliente);
	        i.putExtra("usuario", usuario);
	        startActivityForResult(i, 1);
            return true;
        case android.R.id.home:
		    Intent returnIntent2 = new Intent();
    		returnIntent2.putExtra("result", usuario);
    		setResult(RESULT_CANCELED,returnIntent2);     
    		finish();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}