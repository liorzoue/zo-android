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
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.Toast;

public class PiInfoActivity extends Activity {
	public Context thisContext;
	
	private SharedPreferences sharedPrefs;
	private ProgressDialog pDialog;

	ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    
    // URL to get contacts JSON
    
    private static String url = "get.php?what=infos";
    
    private Boolean isError;
    // contacts JSONArray
    JSONArray contacts = null;
    
    // Hashmap for ListView
    ArrayList<HashMap<String, String>> contactList;

 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// get preferences
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

		// get the listview
		thisContext = this;
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        
		new GetInfos().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{    
		switch (item.getItemId()) 
		{        
			case R.id.action_refresh:
	  			setContentView(R.layout.activity_main);
	  			expListView = null;
	  			expListView = (ExpandableListView) findViewById(R.id.lvExp);
	  			new GetInfos().execute();
	  			return true;
			default:            
				return super.onOptionsItemSelected(item);    
	   }
	}
	
	private class GetInfos extends AsyncTask<Void, Void, Void> {
		List<String> lstUptime = new ArrayList<String>();
		List<String> lstCPU = new ArrayList<String>();
		List<String> lstMemory = new ArrayList<String>();
		List<String> lstAPIInfo = new ArrayList<String>();
		List<String> lstPaths = new ArrayList<String>();
		List<String> lstDisk = new ArrayList<String>();
		
		
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(PiInfoActivity.this);
            pDialog.setMessage("Veuillez patienter...");
            pDialog.setCancelable(false);
            pDialog.show();
            
            
            isError = Utils.ERROR_NO;            
        }
 
        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler(thisContext);
            JSONArray arMovies_path;
            JSONObject oDisk;
            JSONObject oDrives;
            JSONObject oDrivesPct;
            JSONObject oDrivesType;
            JSONObject oDrivesSize;
            JSONObject oDrivesUsed;
            JSONObject oDrivesAvail;
            JSONObject oDrivesMount;
            
            // Lists
            listDataHeader = new ArrayList<String>();
            listDataChild = new HashMap<String, List<String>>();
            
            listDataHeader.add("Uptime");
            listDataHeader.add("CPU");
            listDataHeader.add("Memoire");
            listDataHeader.add("Disques");
            listDataHeader.add("Infos API");
            listDataHeader.add("Repertoires");
            
            String jsonStr;
            
            try {
                // Making a request to url and getting response
                jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
            } catch (Exception e) {
				// TODO: handle exception
            	Log.d("ERREUR", "Erreur ServiceHandler");
                Log.d("url", url);
                Log.d("url-pref", sharedPrefs.getString("prefApiPath", "NO_VALUE"));
            	Log.e("ServiceHandler", e.getMessage());
            	
            	isError = Utils.ERROR_GET;
                Toast.makeText(getApplicationContext(), "Erreur ! Verifiez vos parametres !", Toast.LENGTH_SHORT).show();
                
                Intent myIntent = new Intent(PiInfoActivity.this, SettingsActivity.class);
				// myIntent.putExtra("key", value); //Optional parameters
				PiInfoActivity.this.startActivity(myIntent);
				
				jsonStr = null;
			}
            
            Log.d("Response: ", "> " + jsonStr);
 
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONObject jsonSysInfo;
                    JSONObject jsonMem;
                    
                    jsonSysInfo = jsonObj.getJSONObject(Utils.TAG_SYSINFO);
                    jsonMem = jsonSysInfo.getJSONObject(Utils.TAG_MEMORY);
                   
                    // Uptime
                    lstUptime.add(jsonSysInfo.getString(Utils.TAG_UPTIME));
                    
                    // CPU
                    lstCPU.add("Frenquency : "+jsonSysInfo.getJSONObject(Utils.TAG_CPU).getString(Utils.TAG_FREQUENCY));
                    lstCPU.add("Load : "+jsonSysInfo.getJSONObject(Utils.TAG_CPU).getString(Utils.TAG_LOAD)+"%");
                    lstCPU.add("Temperature : "+jsonSysInfo.getJSONObject(Utils.TAG_CPU).getString(Utils.TAG_TEMPERATURE)+"C");
                    
                    // Memory
                    lstMemory.add("Total : "+jsonMem.getString(Utils.TAG_TOTAL)+" "+Utils.UNIT_MEM);
                    lstMemory.add("Used : "+jsonMem.getString(Utils.TAG_USED)+" "+Utils.UNIT_MEM);
                    lstMemory.add("Free : "+jsonMem.getString(Utils.TAG_FREE)+" "+Utils.UNIT_MEM);
                    
                    // API
                    lstAPIInfo.add("Nom : "+jsonObj.getString(Utils.TAG_APPLICATION));
                    lstAPIInfo.add("Version : "+jsonObj.getString(Utils.TAG_VERSION));
                    lstAPIInfo.add("Auteur : "+jsonObj.getString(Utils.TAG_AUTHOR));
                    lstAPIInfo.add("Site web : "+jsonObj.getString(Utils.TAG_WEBSITE));
                    lstAPIInfo.add("API Key : "+jsonObj.getString(Utils.TAG_API_KEY));
                    lstAPIInfo.add("API URL : "+jsonObj.getString(Utils.TAG_API_URL));
                    lstAPIInfo.add("Logged as : "+jsonObj.getJSONObject(Utils.TAG_AUTH).getString(Utils.TAG_USERNAME));

                    // Paths
                    arMovies_path = jsonObj.getJSONObject(Utils.TAG_PATH).getJSONArray(Utils.TAG_MOVIES);
                    for (int i = 0; i < arMovies_path.length(); i++) {
                    	lstPaths.add("Films ("+i+") : "+arMovies_path.getString(i));
					}
                    
                    arMovies_path = jsonObj.getJSONObject(Utils.TAG_PATH).getJSONArray(Utils.TAG_MUSIC);
                    for (int i = 0; i < arMovies_path.length(); i++) {
                    	lstPaths.add("Music ("+i+") : "+arMovies_path.getString(i));
					}
                    
                    // Disques
                    oDisk = jsonObj.getJSONObject(Utils.TAG_SYSINFO).getJSONObject(Utils.TAG_DISK);
                    oDrives = oDisk.getJSONObject(Utils.TAG_DRIVE);
                    oDrivesPct = oDisk.getJSONObject(Utils.TAG_DRIVE_PCT);
                    oDrivesType = oDisk.getJSONObject(Utils.TAG_TYPEX);
                    oDrivesSize = oDisk.getJSONObject(Utils.TAG_SIZE);
                    oDrivesUsed = oDisk.getJSONObject(Utils.TAG_USED);
                    oDrivesAvail = oDisk.getJSONObject(Utils.TAG_AVAILABLE);
                    oDrivesMount = oDisk.getJSONObject(Utils.TAG_MOUNT);

                    for (int i = 1; i <= oDrives.length(); i++) {
                    	Log.d("oDrives", oDrives.getString(String.valueOf(i)));
                    	lstDisk.add(
                    			getResources().getString(R.string.ui_label_drive)+
                    			" : " + oDrives.getString(String.valueOf(i))+"\n"+
                    			getResources().getString(R.string.ui_label_mount)+
                    			" : " + oDrivesMount.getString(String.valueOf(i))+"\n"+
                    			getResources().getString(R.string.ui_label_type)+
                    			" : " + oDrivesType.getString(String.valueOf(i))+"\n"+
                    			getResources().getString(R.string.ui_label_size)+
                    			" : " + oDrivesSize.getString(String.valueOf(i))+" total / "+
                    			oDrivesUsed.getString(String.valueOf(i))+" used ("+
                    			oDrivesPct.getString(String.valueOf(i))+"%)");
					}
                    
                    
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                Log.d("url", url);
                Log.d("url-pref", sharedPrefs.getString("prefApiPath", "NO_VALUE"));
                Toast.makeText(getApplicationContext(), "Erreur ! Verifiez vos parametres !", Toast.LENGTH_SHORT).show();
                
                Intent myIntent = new Intent(PiInfoActivity.this, SettingsActivity.class);
				// myIntent.putExtra("key", value); //Optional parameters
				PiInfoActivity.this.startActivity(myIntent);
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
            if (isError != Utils.ERROR_GET) {
	            listDataChild.put(listDataHeader.get(0), lstUptime); // Header, Child data // Header, Child data
	            listDataChild.put(listDataHeader.get(1), lstCPU);
	            listDataChild.put(listDataHeader.get(2), lstMemory);
	            listDataChild.put(listDataHeader.get(3), lstDisk);
	            listDataChild.put(listDataHeader.get(4), lstAPIInfo);
	            listDataChild.put(listDataHeader.get(5), lstPaths);
	            
	            listAdapter = new ExpandableListAdapter(thisContext, listDataHeader, listDataChild);
	            expListView.setAdapter(listAdapter);
            } else {
            	Log.d("OnPostExecute-PiInfo", "Erreur");
            }
        }
 
    }

}
