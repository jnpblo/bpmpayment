package com.bpm.bpmpayment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.bpm.bpmpayment.json.JSONParser;
import eu.janmuller.android.simplecropimage.CropImage;

public class ProductoAgregar extends Activity{
	private static final int REQUEST_CODE_CROP_IMAGE = 0;
	private UserLoginTask mAuthTask = null;
	private ProgressDialog pd = null;
	private EditText nombreView, precioView, descripcionView;
	private String nombre, precio, descripcion;
	private ImageView viewImageAddProduct, viewDeleteImage;
	private String usuario;
	private File file;
	private FileInputStream fileInputStream;
	private FileOutputStream fileOutputStream;
	protected boolean flagFoto = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.agregar_producto);
        
        Intent intent = getIntent();
        usuario = intent.getStringExtra("usuario");
        
        nombreView = (EditText) findViewById(R.id.productName);
        precioView = (EditText) findViewById(R.id.productPrice);
        descripcionView = (EditText) findViewById(R.id.productDescription);
        viewImageAddProduct = (ImageView) findViewById(R.id.imageViewAddProduct);
        viewDeleteImage = (ImageView) findViewById(R.id.productoImageDeleteFoto);
        
        viewImageAddProduct.setClickable(true);
        viewImageAddProduct.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {				
				final CharSequence[] options = { "Tomar Foto", "Escoger de la galer�a","Cancelar" };
				
				esconderTeclado();
				 
		        AlertDialog.Builder builder = new AlertDialog.Builder(ProductoAgregar.this);
		        builder.setTitle("Agrega foto del producto");
		        builder.setItems(options, new DialogInterface.OnClickListener() {
		            @Override
		            public void onClick(DialogInterface dialog, int item) {
		                if (item == 0) {
		                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
		                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
		                    startActivityForResult(intent, 1);
		                }
		                else if (item == 1) {
		                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		                    startActivityForResult(intent, 2);
		                }
		                else if (item == 2) {
		                    dialog.dismiss();
		                }
		            }
		        });
		        builder.show();
			}
		});
        
        viewDeleteImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				viewImageAddProduct.setImageResource(R.drawable.addimageproduct);
				file.delete();
				viewDeleteImage.setEnabled(false);
				viewDeleteImage.setVisibility(View.GONE);
				flagFoto  = false;
			}
		});
        
        this.flagFoto = false;
	}
	
	private void runCropImage(String filePath) {
	    Intent intent = new Intent(this, CropImage.class);
	    intent.putExtra(CropImage.IMAGE_PATH, filePath);
	    intent.putExtra(CropImage.SCALE, true);
	    
	    intent.putExtra(CropImage.ASPECT_X, 3);
	    intent.putExtra(CropImage.ASPECT_Y, 3);
	    startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
	    
	    this.viewDeleteImage.setEnabled(true);
	    this.viewDeleteImage.setVisibility(View.VISIBLE);
	    this.flagFoto = true;
	}
	
	private Bitmap reduceImagen() {		
		Bitmap bitmap;
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = 2;
        bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), bitmapOptions);
        
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int bounding = dpToPx(250);
        Log.i("Test", "original width = " + Integer.toString(width));
        Log.i("Test", "original height = " + Integer.toString(height));
        Log.i("Test", "bounding = " + Integer.toString(bounding));
        
        float xScale = ((float) bounding) / width;
        float yScale = ((float) bounding) / height;
        float scale = (xScale <= yScale) ? xScale : yScale;
        Log.i("Test", "xScale = " + Float.toString(xScale));
        Log.i("Test", "yScale = " + Float.toString(yScale));
        Log.i("Test", "scale = " + Float.toString(scale));
        
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        width = scaledBitmap.getWidth(); // re-use
        height = scaledBitmap.getHeight(); // re-use
        Log.i("Test", "scaled width = " + Integer.toString(width));
        Log.i("Test", "scaled height = " + Integer.toString(height));
        
        bitmap.recycle();
        
        OutputStream outFile = null;
        File file2 = new File(file.getAbsolutePath());
        try {
            outFile = new FileOutputStream(file2);
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 50, outFile);
            outFile.flush();
            outFile.close();
                                  
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return scaledBitmap;
	}
	
	private String eliminaEspacios(String palabras) {
    	return palabras.replaceAll("\\s", "~");
    }
	
	private void esconderTeclado() {
		InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); 
		inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),      
		InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	private int dpToPx(int dp)
    {
        float density = getApplicationContext().getResources().getDisplayMetrics().density;
        return Math.round((float)dp * density);
    }
	
	private File copyFile(String sourceFile) throws IOException {
		File destination = null;
		try {
			String path = FragmentMainActivity.dataAppDirectory;
		    File sd = new File(path);
		    
		    if (sd.canWrite()) {
		        String destinationImagePath = path + "tempFileClient.jpg";
		        File source= new File(sourceFile);
		        destination= new File(destinationImagePath);
		        if (source.exists()) {
		            fileInputStream = new FileInputStream(source);
					FileChannel src = fileInputStream.getChannel();
		            fileOutputStream = new FileOutputStream(destination);
					FileChannel dst = fileOutputStream.getChannel();
		            dst.transferFrom(src, 0, src.size());
		            src.close();
		            dst.close();
		        }
		    }
		} catch (Exception e) {
			Log.w("ERROR", "Error al copiar la imagen!");
		}
		
		return destination;
	}
	
	@SuppressWarnings("unchecked")
	private void agregaProducto() {
		nombre = eliminaEspacios(nombreView.getText().toString());
		precio = eliminaEspacios(precioView.getText().toString());
		descripcion = eliminaEspacios(descripcionView.getText().toString());
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("name", nombre));
        params.add(new BasicNameValuePair("price", precio));
        params.add(new BasicNameValuePair("desc", descripcion));
        params.add(new BasicNameValuePair("email", usuario));
        
		esconderTeclado();
		ProductoAgregar.this.pd = ProgressDialog.show(ProductoAgregar.this, "Procesando...", "Registrando datos...", true, false);
		mAuthTask = new UserLoginTask();
		mAuthTask.execute(params);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.product_add_actions, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.action_cancel:
	        	esconderTeclado();
			    Intent returnIntent = new Intent();
        		returnIntent.putExtra("result", usuario);
        		setResult(RESULT_CANCELED,returnIntent);     
        		finish();
	            return true;
	        case R.id.action_add:
	        	agregaProducto();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
        	if(requestCode == 0) {
                viewImageAddProduct.setImageBitmap(reduceImagen());
            }
        	else if (requestCode == 1) {
            	File f = new File(Environment.getExternalStorageDirectory().toString() + "/temp.jpg");
                
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmapOptions.inSampleSize = 2;
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
                    
                    String path = FragmentMainActivity.dataAppDirectory;
                    
                    f.delete();
                    OutputStream outFile = null;
                    file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");

                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                        runCropImage(file.getAbsolutePath());                       
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
            	Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                
                try {
					file = copyFile(picturePath);
					if( file != null ) {
						runCropImage(file.getAbsolutePath());
					}
					else {
						Toast.makeText(getBaseContext(), "Ocurrio alg�n error", Toast.LENGTH_SHORT).show();
					}					
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        }        
    } 
	
	public class UserLoginTask extends AsyncTask<List<NameValuePair>, Void, String>{
		@Override
		protected String doInBackground(List<NameValuePair>... params) {
			try {
				return new JSONParser().getJSONFromUrl("http://bpmcart.com/bpmpayment/php/modelo/addProduct_Post.php", params[0]);
			} catch (Exception e) {
				return null;
			}
		}

		@Override
		protected void onPostExecute(String result) {
			mAuthTask = null;
            	try{
	                if(!result.equals("false")) {	                	
	                	if (ProductoAgregar.this.pd != null) {
	                		ProductoAgregar.this.pd.dismiss();
	                		
	                		Toast.makeText(getBaseContext(), "Producto Agregado", Toast.LENGTH_SHORT).show();
	                		
	                		Intent returnIntent = new Intent();
	                		returnIntent.putExtra("result", usuario);
	                		returnIntent.putExtra("ver", "2");
	                		setResult(RESULT_OK,returnIntent);     
	                		finish();
		   	            }
	                }
	                else {
	                	Toast.makeText(getBaseContext(), "Credenciales inv�lidas",Toast.LENGTH_LONG).show();
	                }
	            } catch (Exception e) {
	                Log.d("ReadJSONFeedTask", e.getLocalizedMessage());
	                Toast.makeText(getBaseContext(), "Imposible conectarse a la red",Toast.LENGTH_LONG).show();
	            }          
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
		}
	}
}
