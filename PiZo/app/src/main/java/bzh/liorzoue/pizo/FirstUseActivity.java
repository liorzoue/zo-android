package bzh.liorzoue.pizo;


import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import bzh.liorzoue.pizo.utils.ServiceHandler;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class FirstUseActivity extends FragmentActivity {
	private static ProgressDialog pDialog;
	public static Context thisContext;
	
	// URL to get contacts JSON
    private static String url = "get.php?what=api_test";
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		thisContext = this;
				
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first_use);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = null;
			Bundle args = new Bundle();
			switch (position) {
				case 0:
					fragment = new WelcomeSectionFragment();
					args.putInt(WelcomeSectionFragment.ARG_SECTION_NUMBER, position + 1);
					break;
				case 1:
					fragment = new APISectionFragment();
					args.putInt(APISectionFragment.ARG_SECTION_NUMBER, position + 1);
					break;
			}

			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_fu_fragment_welcome).toUpperCase(l);
			case 1:
				return getString(R.string.title_fu_fragment_settings).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class WelcomeSectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public WelcomeSectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_fu_welcome,
					container, false);
			TextView dummyTextView = (TextView) rootView
					.findViewById(R.id.section_label);
			dummyTextView.setText(R.string.app_description);
			return rootView;
		}
	}
	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */

	public void checkApi(View v)
	{
		String pref_url;
		String pref_username;
		String pref_password;
		
    	GetInfos infos = new GetInfos();
    	try {
        	TextView txtUrl = (TextView) findViewById(R.id.txtApiUrl);
    		pref_url = txtUrl.getText().toString();
        	TextView txtUsername = (TextView) findViewById(R.id.txtApiLogin);
        	pref_username = txtUsername.getText().toString();
        	TextView txtPassword = (TextView) findViewById(R.id.txtApiPass);
        	pref_password = txtPassword.getText().toString();
        	
            if (txtUrl.length() == 0) {
            	txtUrl.setError("URL is empty");
            } else {
	            Log.d("url", pref_url);
	           
	            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(thisContext);
	            SharedPreferences.Editor editor = sharedPrefs.edit();
	            editor.putString("prefApiPath", pref_url);
	            editor.putString("prefUsername", pref_username);
	            editor.putString("prefPassword", pref_password);
	            editor.commit();
	            
	            Log.d("pref_url", sharedPrefs.getString("prefApiPath", ""));
	            Log.d("pref_username", sharedPrefs.getString("prefUsername", ""));
	            Log.d("pref_password", sharedPrefs.getString("prefPassword", ""));

	    		// Toast
	        	// Toast.makeText(thisContext, "Checking API...", Toast.LENGTH_SHORT).show();
	            // Execute when all is ok
	        	infos.execute();
            }
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("erreur API", e.getMessage());
		}

    	
	}
	
	public static class APISectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public APISectionFragment() {
			
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_fu_api,
					container, false);
			
			
			
			/*
				TextView dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
				dummyTextView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
			*/
			return rootView;
		}
		
	}
	

	public class GetInfos extends AsyncTask<Void, Void, Void> {
		
		public Boolean result;
		
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(thisContext);
            pDialog.setMessage("Veuillez patienter...");
            pDialog.setCancelable(false);
            pDialog.show();
            
        }
 
        @Override
        protected Void doInBackground(Void... arg0) {
        	result = false;
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler(thisContext);

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
 
            Log.d("Response: ", "> " + jsonStr);
 
            
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    String str = jsonObj.getString("test").toString();
                    
                    Log.d("test", str);
                    
                    if (str.equalsIgnoreCase("ok")) {
                    	result = true;
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
            
            
            if (!this.result) {
            	TextView btnValidate = (Button) findViewById(R.id.btnApiValidate);
            	btnValidate.setError("API Error");
            } else {
            	Intent myIntent = new Intent(FirstUseActivity.this, WelcomeActivity.class);
            	FirstUseActivity.this.startActivity(myIntent);
            	// Toast
                // Toast.makeText(thisContext, "Check complete : " + this.result.toString(), Toast.LENGTH_SHORT).show();
            }
        }
 
    }

}

