package com.gatonim.lol;

import com.gatonimo.lol.R;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;


public class MainActivity extends ActionBarActivity implements SeekBar.OnSeekBarChangeListener {
	
	private ShareActionProvider myShareActionProvider;
	MediaPlayer mpRes;
	int PosActual= 0;
	Handler seekBarHandler;
	SeekBar seekbar;
	boolean hilocreado=false;
	boolean buscando=false;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ImageButton stop= (ImageButton) findViewById(R.id.botonStop);
        ImageButton play= (ImageButton) findViewById(R.id.botonPlay);
        ImageButton pause= (ImageButton) findViewById(R.id.botonPausa);
        seekbar=(SeekBar)findViewById(R.id.seekBar1);
        seekbar.setOnSeekBarChangeListener(this);
        seekBarHandler = new Handler();
        
        mpRes=new MediaPlayer();
        mpRes= MediaPlayer.create(getApplicationContext(), R.raw.risasmini);
        mpRes.setOnCompletionListener(new OnCompletionListener() {
        	  public void onCompletion(MediaPlayer mpRes) {
        	    PosActual=0;
        	  }

        	});
        
        
        stop.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mpRes.isPlaying()){
					mpRes.pause();
					PosActual=0;
				}
			}
		});
        
        play.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(!mpRes.isPlaying()){
					
					mpRes.seekTo(PosActual);
					if(!hilocreado){
						new Thread(new Runnable() {

							@Override
						    public void run() {
						    	hilocreado=true;
						    	int i=1;
						    	while(hilocreado){
							        if (mpRes.isPlaying()) {
							        	int progreso=(mpRes.getCurrentPosition()*100)/mpRes.getDuration();
							            seekbar.setProgress(progreso);
							            try {
											Thread.sleep(1000);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
							           
							        }
						    	}
						    }
						}).start();
					}
					mpRes.start();
					
				}
			}
		});
        
        pause.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mpRes.isPlaying()){
					PosActual=mpRes.getCurrentPosition();
					mpRes.pause();
				}
			}
		});
    }
    
    @Override protected void onPause() {
    	   pausar();
    	   super.onPause();
    	}

    private void pausar() {
		setHilocreado(false);
		if(mpRes.isPlaying())
			PosActual=mpRes.getCurrentPosition();
			mpRes.pause();
		
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        
        MenuItem ShareOpt = menu.findItem(R.id.compartir);
        myShareActionProvider = (ShareActionProvider)MenuItemCompat.getActionProvider(ShareOpt); 
        
        Intent i=new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.gatonimo.lol");
        myShareActionProvider.setShareIntent(i);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        
        return super.onOptionsItemSelected(item);
    }


	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		
		
	}


	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		
		
	}


	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		buscando=true;
		if(mpRes.isPlaying())
			mpRes.pause();
		PosActual=(mpRes.getDuration()*seekbar.getProgress())/100;
		mpRes.seekTo(PosActual);
		mpRes.start();
		buscando=false;
		
	}
	
	public void setHilocreado(boolean b){
		hilocreado=b;
	}
}
