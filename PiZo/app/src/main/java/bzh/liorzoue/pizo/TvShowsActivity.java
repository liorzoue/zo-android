package bzh.liorzoue.pizo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import bzh.liorzoue.pizo.utils.ExpandableListAdapter;
import bzh.liorzoue.pizo.utils.ServiceHandler;
import bzh.liorzoue.pizo.utils.Utils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.support.v4.app.NavUtils;

public class TvShowsActivity extends Activity {
	private ProgressDialog pDialog;
	
	ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    
	// URL to get contacts JSON
    private static String url = "get.php?what=tvshows";
    
	public Context thisContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tv_shows);
		// Show the Up button in the action bar.
		setupActionBar();
		
		thisContext = this;
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
		new GetInfos().execute();
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tv_shows, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	private class GetInfos extends AsyncTask<Void, Void, Void> {
		List<String> lstTvShows = new ArrayList<String>();
		
		
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(TvShowsActivity.this);
            pDialog.setMessage("Veuillez patienter...");
            pDialog.setCancelable(false);
            pDialog.show();
            
            
        }
 
        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler(thisContext);
            JSONArray arTvShows;
            // Lists
            listDataHeader = new ArrayList<String>();
            listDataChild = new HashMap<String, List<String>>();
            

            listDataHeader.add(Utils.CAT_TV_SHOWS);
            
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
 
            Log.d("Response: ", "> " + jsonStr);
 
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    arTvShows = jsonObj.getJSONObject(Utils.TAG_TV_SHOWS).getJSONArray(Utils.TAG_LIST);
                    
                    for (int i = 0; i < arTvShows.length(); i++) {
                    	for (int j = 0; j < arTvShows.getJSONArray(i).length(); j++) {
                    		lstTvShows.add(arTvShows.getJSONArray(i).getString(j));
                    	}
					}

                    
                    
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
            /**
             * Updating parsed JSON data into ListView
             * */

            listDataChild.put(listDataHeader.get(0), lstTvShows);
            
            listAdapter = new ExpandableListAdapter(thisContext, listDataHeader, listDataChild);
            Log.d("Debug - ListAdapter", listAdapter.getChild(0, 0).toString());
            expListView.setAdapter(listAdapter);
            
            /*
            expListView.setOnChildClickListener(new OnChildClickListener() {
           	 
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                	String dataTitle;
                	String dataCat;
                	String dataID;
                	
                	
                	dataTitle = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
                	dataCat = String.valueOf(groupPosition);
                	dataID = String.valueOf(childPosition);
                    // Toast
                	Toast.makeText(getApplicationContext(), dataCat+":"+dataTitle, Toast.LENGTH_SHORT).show();
                	
                	// Nouvelle vue
                	Intent myIntent = new Intent(TvShowsActivity.this, FilmDetailActivity.class);
    				myIntent.putExtra("title", dataTitle);
    				myIntent.putExtra("cat", dataCat);
    				myIntent.putExtra("id", dataID);
                	TvShowsActivity.this.startActivity(myIntent);
    	  			return true;
                }
            });
            
            */
        }
 
    }

}
