package nu.bluebit.safelight;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.IconTextView;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

	private IconTextView bulb;
	private boolean light;
	private MediaPlayer mp;
	private Camera camera = null;
	private final Context context = this;
	private Parameters parameters;
	private RelativeLayout layout;
	private SurfaceTexture texture;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		light = false;
		bulb = (IconTextView) findViewById(R.id.bulbView);
		layout = (RelativeLayout) findViewById(R.id.layout);
		texture = new SurfaceTexture(0);
		final PackageManager pm = context.getPackageManager();
		  if(!isCameraSupported(pm)){
		   AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		   alertDialog.setTitle("No Camera");
		      alertDialog.setMessage("The device's doesn't support camera.");
		      alertDialog.setButton(RESULT_OK, "OK", new DialogInterface.OnClickListener() {
		          public void onClick(final DialogInterface dialog, final int which) { 
		           Log.e("err", "The device's doesn't support camera.");
		          }
		       });
		   alertDialog.show();
		  }
		
		bulb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				PackageManager pm=context.getPackageManager();
				  if(isFlashSupported(pm)){
					  if(light) {
						  turnOffFlash();
					  } else {
						  turnOnFlash();
					  }
				  }else{
				   AlertDialog alertDialog = new AlertDialog.Builder(context).create();
				   alertDialog.setTitle("No Camera Flash");
				      alertDialog.setMessage("The device's camera doesn't support flash.");
				      alertDialog.setButton(RESULT_OK, "OK", new DialogInterface.OnClickListener() {
				          public void onClick(final DialogInterface dialog, final int which) { 
				           Log.e("err", "The device's camera doesn't support flash.");
				          }
				       });
				   alertDialog.show();
				  }
			}
		});
	}
	
	// getting camera parameters
	private void getCamera() {
	    if (camera == null) {
	        try {
	            camera = Camera.open();
	            camera.setPreviewTexture(texture);
	            parameters = camera.getParameters();
	        } catch (RuntimeException e) {
	            Log.e("Camera Error. Failed to Open. Error: ", e.getMessage());
	        } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}
	
	// change power of flash
	public void changeIntensity()
	{
	    camera.stopPreview();
	    camera.startPreview();
	}
	 
	 /*
	 * Turning On flash
	 */
	private void turnOnFlash() {
	    if (!light) {
	        if (camera == null || parameters == null) {
	            return;
	        }
	        // play sound
	        playSound();
	         
	        parameters = camera.getParameters();
	        parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
	        camera.setParameters(parameters);
	        camera.startPreview();
	        light = true;
	         
	        // changing button/switch image
	        toggleButtonImage();
	    }
	 
	}
	
	/*
	 * Turning Off flash
	 */
	private void turnOffFlash() {
	    if (light) {
	        if (camera == null || parameters == null) {
	            return;
	        }
	        // play sound
	        playSound();
	         
	        parameters = camera.getParameters();
	        parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
	        camera.setParameters(parameters);
	        camera.stopPreview();
	        light = false;
	         
	        // changing button/switch image
	        toggleButtonImage();
	    }
	}
	
	/*
	 * Toggle switch button images
	 * changing image states to on / off
	 * */
	private void toggleButtonImage(){
	    if(light){
	        bulb.setShadowLayer(16.0f, 0, 0, Color.BLACK);
	        bulb.setTextColor(Color.WHITE);
	        layout.setBackgroundColor(Color.WHITE);
	    }else{
	    	bulb.setShadowLayer(16.0f, 0, 0, Color.WHITE);
	        bulb.setTextColor(Color.BLACK);
	        layout.setBackgroundColor(Color.BLACK);
	    }
	}
	
	/*
	 * Playing sound
	 * will play button toggle sound on flash on / off
	 * */
	private void playSound(){
	    if(light){
	        mp = MediaPlayer.create(MainActivity.this, R.raw.bulb_off);
	    }else{
	        mp = MediaPlayer.create(MainActivity.this, R.raw.bulb_on);
	    }
	    mp.setOnCompletionListener(new OnCompletionListener() {
	 
	        @Override
	        public void onCompletion(MediaPlayer mp) {
	            // TODO Auto-generated method stub
	            mp.release();
	        }
	    }); 
	    mp.start();
	}
	
	@Override
	protected void onDestroy() {
	    super.onDestroy();
	}
	 
	@Override
	protected void onPause() {
	    super.onPause();
	     
	    // on pause turn off the flash
	    turnOffFlash();
	}
	 
	@Override
	protected void onRestart() {
	    super.onRestart();
	    
	    if(light)
	        turnOnFlash();
	}
	 
	@Override
	protected void onResume() {
	    super.onResume();
	     
	    // on resume turn on the flash
	    if(light)
	        turnOnFlash();
	}
	 
	@Override
	protected void onStart() {
	    super.onStart();
	     
	    // on starting the app get the camera params
	    getCamera();
	}
	 
	@Override
	protected void onStop() {
	    super.onStop();
	     
	    // on stop release the camera
	    if (camera != null) {
	        camera.release();
	        camera = null;
	    }
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/**
	  * @param packageManager
	  * @return true <b>if the device support camera flash</b><br/>
	  * false <b>if the device doesn't support camera flash</b>
	  */
	 private boolean isFlashSupported(PackageManager packageManager){ 
	  // if device support camera flash?
	  if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
	   return true;
	  } 
	  return false;
	 }

	 /**
	  * @param packageManager
	  * @return true <b>if the device support camera</b><br/>
	  * false <b>if the device doesn't support camera</b>
	  */
	 private boolean isCameraSupported(PackageManager packageManager){
	  // if device support camera?
	  if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
	   return true;
	  } 
	  return false;
	 }
	

}
