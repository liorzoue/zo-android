package bzh.liorzoue.pizo;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import bzh.liorzoue.pizo.utils.ImageLoader;
import bzh.liorzoue.pizo.utils.ServiceHandler;
import bzh.liorzoue.pizo.utils.Utils;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class FilmDetailActivity extends Activity {
	private ProgressDialog pDialog;

    // URL to get contacts JSON
    private static String url = "?what=movie_info&q=";
    private String q;
    // JSON Node names
        
    String movieTagLine;
    String originalTitle;
    String movieResume;
    String moviePosterURL;
    String movieIMDBUrl;
    String movieRuntime;
    String movieStatus;
    String movieReleaseDate;
    String movieGenres;
    String movieProductionCie;
    String movieProductionCountries;
    String movieSpokenLanguages;
    
    Float movieVoteAverage;
    // contacts JSONArray
    JSONArray contacts = null;
    
    // Hashmap for ListView
    ArrayList<HashMap<String, String>> contactList;

    ImageView imgCover;
	int loader = R.drawable.ic_loader;
	
	public Context thisContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		setContentView(R.layout.activity_film_detail);
		imgCover = (ImageView) findViewById(R.id.backdrop);

		if(bundle.getString("title")!= null)
        {
            this.setTitle(bundle.get("title").toString());
        }
		if(bundle.getString("id")!= null && bundle.getString("cat")!= null)
        {
			q = bundle.getString("cat")+"+"+bundle.getString("id");
        }
		
		

		thisContext = this;
		new GetInfos(this).execute();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.movie_detail, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{    
		switch (item.getItemId()) 
		{        
			case R.id.action_refresh:
	  			setContentView(R.layout.activity_film_detail);
	  			imgCover = null;
	  			imgCover = (ImageView) findViewById(R.id.backdrop);
	  			new GetInfos(this).execute();
	  			return true;        
			default:            
				return super.onOptionsItemSelected(item);    
	   }
	}
	
	public void clickOnImage(View v)
	{
	    Toast.makeText(this, moviePosterURL, Toast.LENGTH_LONG).show();
	}
	
	public void clickOnIMDB(View v)
	{
	    Toast.makeText(this, "Opening IMDB ...", Toast.LENGTH_LONG).show();
	    Uri uri = Uri.parse(movieIMDBUrl);
	    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
	    startActivity(intent);
	}
	
	private class GetInfos extends AsyncTask<Void, Void, Void> {

		public FilmDetailActivity activity;

	    public GetInfos(FilmDetailActivity a)
	    {
	        this.activity = a;
	    }
	    
		protected void setCover(String url) {
			
	        ImageLoader imgLoader = new ImageLoader(thisContext);
	        imgLoader.DisplayImage(url, loader, imgCover);
		}
		
		protected void setText(int id, String value) {
			TextView txtView = (TextView) findViewById(id);
			txtView.setText(value);
		}

		@Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(FilmDetailActivity.this);
            pDialog.setMessage("Veuillez patienter...");
            pDialog.setCancelable(false);
            pDialog.show();
            
            
        }
 
        @Override
        protected Void doInBackground(Void... arg0) {
        	JSONArray arGenres;
        	JSONArray arProductionCie;
        	JSONArray arProductionCountries;
        	JSONArray arSpokenLanguages;
        	
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler(thisContext);

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url+q, ServiceHandler.GET);
 
            Log.d("Response: ", "> " + jsonStr);
 
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONObject jsonRes = jsonObj.getJSONObject(Utils.TAG_MOVIE_DETAIL);
                    movieTagLine = jsonRes.getString(Utils.TAG_TAGLINE);
                    originalTitle = jsonRes.getString(Utils.TAG_ORIGINAL_TITLE);
                    movieResume = jsonRes.getString(Utils.TAG_OVERVIEW);
                    movieStatus = jsonRes.getString(Utils.TAG_STATUS);
                    movieReleaseDate = jsonRes.getString(Utils.TAG_RELEASE_DATE);
                    moviePosterURL = "http://image.tmdb.org/t/p/original"+jsonRes.getString(Utils.TAG_BACKDROP_PATH);
                    movieVoteAverage = Float.parseFloat(jsonRes.getString(Utils.TAG_VOTE_AVERAGE));
                    movieIMDBUrl = "http://www.imdb.com/title/"+jsonRes.getString(Utils.TAG_IMDB_ID);

                    arGenres = null;
                    arGenres = jsonRes.getJSONArray(Utils.TAG_GENRES);
                    
                    movieGenres = "";
                    for (int i = 0; i < arGenres.length(); i++) {
                    	Log.d("genres", arGenres.getJSONObject(i).getString(Utils.TAG_NAME));
                    	movieGenres += arGenres.getJSONObject(i).getString(Utils.TAG_NAME);
                    	if (i != (arGenres.length() -1)) {
                    		movieGenres  += ", ";
                    	}
					}
                    
                    arProductionCie = null;
                    arProductionCie = jsonRes.getJSONArray(Utils.TAG_PRODUCTION_COMPANIES);
                    		
                    movieProductionCie = "";
                    for (int i = 0; i < arProductionCie.length(); i++) {
                    	movieProductionCie += arProductionCie.getJSONObject(i).getString(Utils.TAG_NAME);
                    	if (i != (arProductionCie.length() -1)) {
                    		movieProductionCie  += ", ";
                    	}
					}
                    
                    arProductionCountries = null;
                    arProductionCountries = jsonRes.getJSONArray(Utils.TAG_PRODUCTION_COUNTRIES);
                    		
                    movieProductionCountries = "";
                    for (int i = 0; i < arProductionCountries.length(); i++) {
                    	movieProductionCountries += arProductionCountries.getJSONObject(i).getString(Utils.TAG_NAME);
                    	if (i != (arProductionCountries.length() -1)) {
                    		movieProductionCountries  += ", ";
                    	}
					}
                    
                    arSpokenLanguages = null;
                    arSpokenLanguages = jsonRes.getJSONArray(Utils.TAG_SPOKEN_LANGUAGES);
                    		
                    movieSpokenLanguages = "";
                    for (int i = 0; i < arSpokenLanguages.length(); i++) {
                    	movieSpokenLanguages += arSpokenLanguages.getJSONObject(i).getString(Utils.TAG_NAME);
                    	if (i != (arSpokenLanguages.length() -1)) {
                    		movieSpokenLanguages  += ", ";
                    	}
					}
                    
                    // Format movie runtime
                    Integer movieRunHours = 0;
                    Integer movieRunMinutes = 0;
                    
                    movieRunMinutes = jsonRes.getInt(Utils.TAG_RUNTIME);
                    
                    movieRunHours = movieRunMinutes/60;
                    movieRunMinutes = movieRunMinutes - movieRunHours*60;
                    
                    movieRuntime = movieRunHours.toString()+"h"+movieRunMinutes.toString()+"m";
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
            return null;
        }
 
        @Override
        protected void onPostExecute(Void result) {
        
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            
            // Activity Title
            activity.setTitle(originalTitle);
            
            // Text
            setText(R.id.movieTagLine, movieTagLine);
            setText(R.id.movieResume, movieResume);
            setText(R.id.movieRuntime, movieRuntime);
            setText(R.id.movieStatus, movieStatus);
            setText(R.id.movieReleaseDate, movieReleaseDate);
            setText(R.id.movieGenres, movieGenres);
            setText(R.id.movieProductionCie, movieProductionCie);
            setText(R.id.movieProductionCountries, movieProductionCountries);
            setText(R.id.movieSpokenLanguages, movieSpokenLanguages);
            
            // Cover Image
            setCover(moviePosterURL);
            
            // Note
            RatingBar rtBar = (RatingBar) findViewById(R.id.voteAverage);
            rtBar.setRating(movieVoteAverage/2);
                        
        }
 
    }

}
