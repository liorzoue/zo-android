package bzh.liorzoue.pizo.utils;

import java.io.InputStream;
import java.io.OutputStream;
  
public class Utils {
	
	// JSON Node names
    // API
    public static final String TAG_APPLICATION = "application_name";
    public static final String TAG_VERSION = "version";
    public static final String TAG_AUTHOR = "author";
    public static final String TAG_WEBSITE = "website";
    public static final String TAG_API_KEY = "api_key";
    public static final String TAG_API_URL = "apiUrl";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_AUTH = "auth";
    
    // Sys
    public static final String TAG_SYSINFO = "sysinfo";
    public static final String TAG_UPTIME = "uptime";
    

    public static final String TAG_CPU = "cpu";
    public static final String TAG_FREQUENCY = "frequency";
    public static final String TAG_TEMPERATURE = "temperature";
    public static final String TAG_LOAD = "load";

    public static final String TAG_DISK = "disk";
    public static final String TAG_DRIVE = "drive";
    public static final String TAG_DRIVE_PCT = "drive_pct";
    
    public static final String TAG_MEMORY = "mem";
    
    public static final String TAG_FREE = "free";
    public static final String TAG_USED = "used";
    public static final String TAG_SIZE = "sizes";
    public static final String TAG_AVAILABLE = "avail";
    public static final String TAG_TYPEX = "typex";
    public static final String TAG_MOUNT = "mount";
    public static final String TAG_TOTAL = "total";
    
    // Paths
    public static final String TAG_PATH = "path";
    public static final String TAG_MOVIES = "movies";
    public static final String TAG_MUSIC = "music";
    public static final String TAG_FILENAME = "fileName";
    public static final String TAG_IMDB_ID = "imdb_id";
    public static final String TAG_TV_SHOWS = "tvShows";
    public static final String TAG_LIST = "list";
    
    public static final String TAG_RESULTS = "results";
    public static final String TAG_MOVIE_DETAIL = "movieDetail";
    public static final String TAG_BACKDROP_PATH = "backdrop_path";
    public static final String TAG_POSTER_PATH = "poster_path";
    public static final String TAG_ORIGINAL_TITLE = "original_title";
    public static final String TAG_TAGLINE = "tagline";
    public static final String TAG_OVERVIEW = "overview";
    public static final String TAG_VOTE_AVERAGE = "vote_average";
    public static final String TAG_RUNTIME = "runtime";
    public static final String TAG_STATUS = "status";
    public static final String TAG_GENRES = "genres";
    public static final String TAG_NAME = "name";
    public static final String TAG_RELEASE_DATE = "release_date";
    public static final String TAG_PRODUCTION_COMPANIES = "production_companies";
    public static final String TAG_PRODUCTION_COUNTRIES = "production_countries";
    public static final String TAG_SPOKEN_LANGUAGES = "spoken_languages";
    
    public static final Boolean ERROR_NO = true;
    public static final Boolean ERROR_GET = false;
    
    public static final String NO_VALUE_STRING = "NO_VALUE";
    public static final String DEFAULT_URL = "http://your_url/api/";
    
    // Nom des categories
    public static final String CAT_TV_SHOWS = "TV Shows";
    public static final String CAT_FULLHD3D = "1080p 3D";
    public static final String CAT_FULLHD = "1080p";
    public static final String CAT_HD = "720p";
    public static final String CAT_DVDRIP = "DVD-rip";

    public static final String UNIT_MEM = "MB";
    
    
    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
}