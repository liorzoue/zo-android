package bzh.liorzoue.pizo;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class WelcomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		
		// Check if API IP is populate
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		if(sharedPrefs.getString("prefApiPath", "").length() == 0) {
        	Intent myIntent = new Intent(WelcomeActivity.this, FirstUseActivity.class);
        	WelcomeActivity.this.startActivity(myIntent);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.welcome, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{    
		switch (item.getItemId()) 
		{        
			case R.id.action_movies:
				Intent movies = new Intent(WelcomeActivity.this, FilmActivity.class);
				// myIntent.putExtra("key", value); //Optional parameters
				WelcomeActivity.this.startActivity(movies);
	  			return true;
			case R.id.action_tv_shows:
				Intent tvshows = new Intent(WelcomeActivity.this, TvShowsActivity.class);
				// myIntent.putExtra("key", value); //Optional parameters
				WelcomeActivity.this.startActivity(tvshows);
	  			return true;
			case R.id.action_pi_info:
				Intent infos = new Intent(WelcomeActivity.this, PiInfoActivity.class);
				// myIntent.putExtra("key", value); //Optional parameters
				WelcomeActivity.this.startActivity(infos);
	  			return true;
			case R.id.action_config_screen:
				Intent first_use = new Intent(WelcomeActivity.this, FirstUseActivity.class);
				// myIntent.putExtra("key", value); //Optional parameters
				WelcomeActivity.this.startActivity(first_use);
	  			return true; 
			case R.id.action_settings:
				Intent settings = new Intent(WelcomeActivity.this, SettingsActivity.class);
				// myIntent.putExtra("key", value); //Optional parameters
				WelcomeActivity.this.startActivity(settings);
	  			return true; 
			default:            
				return super.onOptionsItemSelected(item);    
	   }
	}
	
	public void launchMovies(View v)
	{
		Intent myIntent = new Intent(WelcomeActivity.this, FilmActivity.class);
		// myIntent.putExtra("key", value); //Optional parameters
		WelcomeActivity.this.startActivity(myIntent);
	}
	
	public void launchTvShows(View v)
	{
		Intent myIntent = new Intent(WelcomeActivity.this, TvShowsActivity.class);
		// myIntent.putExtra("key", value); //Optional parameters
		WelcomeActivity.this.startActivity(myIntent);
	}
	
	public void launchPiInfo(View v)
	{
		Intent myIntent = new Intent(WelcomeActivity.this, PiInfoActivity.class);
		// myIntent.putExtra("key", value); //Optional parameters
		WelcomeActivity.this.startActivity(myIntent);
	}

}
