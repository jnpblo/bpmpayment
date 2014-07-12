package com.bpm.bpmpayment;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import com.bpm.adapters.ListAdapterClientes;
import com.bpm.adapters.ListAdapterClientes.ItemCliente;
import com.bpm.bpmpayment.json.JsonCont;
import com.bpm.bpmpayment.parcelable.ParcelableCliente;

public class FragmentClientes extends Fragment {

	private static ArrayList<Cliente> clientes;
	static JSONArray clients;
	private static final String INDEX = "index";
	private String usuario;
	UserLoginTask mAuthTask = null;
	public ProgressDialog pd = null;
	public LinearLayout llOpcionesFragments = null;
	GridView gv = null;

	public static FragmentClientes newInstance(int index, JSONObject jObject) {
		FragmentClientes fragment = new FragmentClientes();
		Bundle bundle = new Bundle();
		bundle.putInt(INDEX, index);
		fragment.setArguments(bundle);
		fragment.setRetainInstance(true);
		
		clientes = new ArrayList<Cliente>();
		try {
			clients = jObject.getJSONArray("clientes");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		
		Intent intent = getActivity().getIntent();
        usuario = intent.getStringExtra("usuario"); 
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	ViewGroup rootViewClients = (ViewGroup) inflater.inflate(R.layout.fragment_clientes, container, false);
    	rootViewClients.setBackgroundColor(Color.BLACK);

		if(clientes.size() != 0)
			clientes.clear();
			try {
		    	int n = clients.length();
		    	for(int i = 0 ; i < n ; i++) {
		    		JSONObject person = clients.getJSONObject(i);
		    		JSONArray telefonosCliente = person.getJSONArray("telefonos");
		    				    		
		    		StringBuilder tels = new StringBuilder();
		    		
		    		if(telefonosCliente.length() != 0) {
		    			for(int z = 0 ; z < telefonosCliente.length() ; z++) {
			    			JSONObject telefonoN = telefonosCliente.getJSONObject(z);
			    			
			    			if(z != (telefonosCliente.length() - 1) ) {
			    				tels.append(telefonoN.getString("tipo_telefono"));
				    			tels.append(":");
				    			tels.append(telefonoN.getString("telefono"));
				    			tels.append("_");
			    			}
			    			else {
			    				tels.append(telefonoN.getString("tipo_telefono"));
				    			tels.append(":");
				    			tels.append(telefonoN.getString("telefono"));
			    			}
			    		}
		    		}
		    		
		    		else {
		    			tels.append("");
		    		}
		    		
		    	    clientes.add( new Cliente(R.drawable.noimageuser, 
		    	    						  person.getInt("id_cliente"),
		    	    		                  person.getString("apellidop"),
		    	    		                  person.getString("apellidom"),
		    	    		                  person.getString("nombres"),
		    	    		                  person.getString("razon_social"),
		    	    		                  person.getString("email"),
		    	    		                  person.getString("calle_y_num"),
		    	    		                  person.getString("ciudad"),
		    	    		                  person.getString("colonia"),
		    	    		                  person.getString("delegacion"),
		    	    		                  person.getString("estado"),
		    	    		                  person.getString("cp"),
		    	    		                  person.getString("pais"),
		    	    		                  person.getString("rfc"),
		    	    		                  person.getString("foto"),
		    	    		                  person.getInt("id_usuario"),
		    	    		                  tels.toString()));
		    	}
		    			    	
				gv = (GridView) rootViewClients.findViewById(R.id.grid_view_clientes);
				gv.setAdapter(new ListAdapterClientes(getActivity(), clientes));
				gv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						final int user = arg2;
					   	   AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
						   builder.setTitle(clientes.get(user).getNombres() + " " + clientes.get(user).getApellidop() + " " + clientes.get(user).getApellidom());
						   builder.setItems(R.array.opciones_clientes, new DialogInterface.OnClickListener() {
							   public void onClick(DialogInterface dialog, int item) {
								   if (item == 0) {
									   Toast.makeText(getActivity().getBaseContext(), "Hacer factura", Toast.LENGTH_SHORT).show();
								   }
								   else if(item == 1) {
									   ParcelableCliente parcelableCliente = new ParcelableCliente(clientes.get(user));
									   Intent i = new Intent(getActivity().getBaseContext(), ClienteEditar.class);
								       i.putExtra("cliente", parcelableCliente);
								       i.putExtra("usuario", usuario);
								       startActivityForResult(i, 1);
								   }
								   else if(item == 2) {
									   AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
									   builder2.setTitle("Borrar cliente");
								        builder2.setMessage("Seguro que desea eliminar a:\n\"" + clientes.get(user).getNombres() + "\"")
								               .setPositiveButton("Si", new DialogInterface.OnClickListener() {
								                   public void onClick(DialogInterface dialog, int id) {
								                	   FragmentClientes.this.pd= ProgressDialog.show(getActivity(), "Procesando...", "Eliminando cliente...", true, false);
													   mAuthTask = new UserLoginTask();
													   mAuthTask.execute("http://bpmcart.com/bpmpayment/php/modelo/deleteCliente.php?emailCliente="+ clientes.get(user).getEmail());														
								                   }
								               })
								               .setNegativeButton("No", null);
								        AlertDialog alert = builder2.create();
										alert.show();
								   }
							    }
							});
							AlertDialog alert = builder.create();
							alert.show();
							
							return true;
					}
				});
				
				gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						final int user = arg2;					
						ParcelableCliente parcelableCliente = new ParcelableCliente(clientes.get(user));
						Intent i = new Intent(getActivity().getBaseContext(), ClienteDetalles.class);
				        i.putExtra("cliente", parcelableCliente);
				        i.putExtra("usuario", usuario);
				        startActivityForResult(i, 1);
					}
				});
			} catch(Exception e) {
				Log.w("ERROR", e.getMessage());
			}
		return rootViewClients;
	}
	
	@Override
	public String toString() {
		if(clientes.size() != 0) {
			return "Clientes (" + clientes.size() + ")";
		}
		return "Clientes (0)";
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    super.onCreateOptionsMenu(menu, inflater);
	    getActivity().getMenuInflater().inflate(R.menu.clientes, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
	    case R.id.menu_agregar_cliente:            
            Intent i = new Intent(getActivity().getBaseContext(), ClienteAgregar.class);
            i.putExtra("usuario", usuario);
            startActivityForResult(i, 1);
            return true;
	    default:
	        break;
	    }
	    return false;
	}
	
	public class UserLoginTask extends AsyncTask<String, Void, String>{
		@Override
		protected String doInBackground(String... urls) {
			try {
				return new JsonCont().readJSONFeed(urls[0]);
			} catch (Exception e) {
				return null;
			}
		}

		@Override
		protected void onPostExecute(String result) {
			mAuthTask = null;
        	try{
                if(!result.equals("false") || !result.equals("Argumentos invalidos")) {	                	
                	if (FragmentClientes.this.pd != null) {
                		FragmentClientes.this.pd.dismiss();
                		String temp = usuario;
                		
                		Intent returnIntent = new Intent(getActivity(), FragmentMainActivity.class);
                		returnIntent.putExtra("usuario", temp);
                		returnIntent.putExtra("ver", "0");
                		getActivity().setResult(android.app.Activity.RESULT_OK,returnIntent);
                		startActivity(returnIntent);
                		getActivity().finish();              		
	   	            }
                }
                else {
                	Toast.makeText(getActivity().getBaseContext(), "Hubo algún error",Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Log.d("ReadJSONFeedTask", e.getLocalizedMessage());
                Toast.makeText(getActivity().getBaseContext(), "Imposible conectarse a la red",Toast.LENGTH_LONG).show();
            }       
		}
		
		@Override
		protected void onProgressUpdate(Void... values) {	
		}
	}
}
