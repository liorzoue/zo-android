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
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;

public class FilmActivity extends Activity {
	private ProgressDialog pDialog;

	ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    
    // URL to get contacts JSON
    private static String url = "?what=movies&q=list";
    
    // contacts JSONArray
    JSONArray contacts = null;
    
    // Hashmap for ListView
    ArrayList<HashMap<String, String>> contactList;

	public Context thisContext;
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_film);
		// get the listview
		thisContext = this;
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
		new GetInfos().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.movies, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{    
		switch (item.getItemId()) 
		{        
			case R.id.action_refresh:
	  			setContentView(R.layout.activity_film);
	  			expListView = null;
	  			expListView = (ExpandableListView) findViewById(R.id.lvExp);
	  			new GetInfos().execute();
	  			return true;        
			default:            
				return super.onOptionsItemSelected(item);    
	   }
	}
	
	
	
	private class GetInfos extends AsyncTask<Void, Void, Void> {
		List<String> lstFullHD3D = new ArrayList<String>();
		List<String> lstFullHD = new ArrayList<String>();
		List<String> lstHD = new ArrayList<String>();
		List<String> lstDVDRip = new ArrayList<String>();
		
		
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(FilmActivity.this);
            pDialog.setMessage("Veuillez patienter...");
            pDialog.setCancelable(false);
            pDialog.show();
            
            
        }
 
        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler(thisContext);
            JSONArray arFullHD3D;
            JSONArray arFullHD;
            JSONArray arHD;
            JSONArray arDVDRIp;
            // Lists
            listDataHeader = new ArrayList<String>();
            listDataChild = new HashMap<String, List<String>>();
            

            listDataHeader.add(Utils.CAT_FULLHD);
            listDataHeader.add(Utils.CAT_FULLHD3D);
            listDataHeader.add(Utils.CAT_HD);
            listDataHeader.add(Utils.CAT_DVDRIP);
            
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
 
            Log.d("Response: ", "> " + jsonStr);
 
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    arFullHD = jsonObj.getJSONArray(Utils.TAG_MOVIES).getJSONArray(0);
                    arFullHD3D = jsonObj.getJSONArray(Utils.TAG_MOVIES).getJSONArray(1);
                    arHD = jsonObj.getJSONArray(Utils.TAG_MOVIES).getJSONArray(2);
                    arDVDRIp = jsonObj.getJSONArray(Utils.TAG_MOVIES).getJSONArray(3);
                    

                    for (int i = 0; i < arFullHD3D.length(); i++) {
                    	lstFullHD3D.add(arFullHD3D.getString(i));
					}
                    
                    for (int i = 0; i < arFullHD.length(); i++) {
                    	lstFullHD.add(arFullHD.getString(i));
					}
                    
                    for (int i = 0; i < arHD.length(); i++) {
                    	lstHD.add(arHD.getString(i));
					}
                    
                    for (int i = 0; i < arDVDRIp.length(); i++) {
                    	lstDVDRip.add(arDVDRIp.getString(i));
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

            listDataChild.put(listDataHeader.get(0), lstFullHD);
            listDataChild.put(listDataHeader.get(1), lstFullHD3D);
            listDataChild.put(listDataHeader.get(2), lstHD);
            listDataChild.put(listDataHeader.get(3), lstDVDRip);
            
            listAdapter = new ExpandableListAdapter(thisContext, listDataHeader, listDataChild);
            Log.d("Debug - ListAdapter", listAdapter.getChild(0, 0).toString());
            expListView.setAdapter(listAdapter);
            
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
                	Intent myIntent = new Intent(FilmActivity.this, FilmDetailActivity.class);
    				myIntent.putExtra("title", dataTitle);
    				myIntent.putExtra("cat", dataCat);
    				myIntent.putExtra("id", dataID);
                	FilmActivity.this.startActivity(myIntent);
    	  			return true;
                }
            });
        }
 
    }

}
