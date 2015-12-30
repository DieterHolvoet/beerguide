package be.dieterholvoet.beerapp.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class BeerSearchSuggestionProvider extends ContentProvider {
    private static final String AUTHORITY = "be.dieterholvoet.beerapp.provider.BeerSearchSuggestionProvider";
    private static final String BEERS_API = "beers";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BEERS_API);
    public static final int BEERS = 1;

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, BEERS_API, BEERS);
    }

    public BeerSearchSuggestionProvider() {
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int uriType = sURIMatcher.match(uri);

        switch (uriType) {
            case BEERS:
                //String urlString = URL + "search" + "";
                //new CallAPI().execute("search", selection);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI");
        }
    return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /*private class CallAPI extends AsyncTask<String, String> {
        private final String URL = "http://api.brewerydb.com/v2/";
        private final String API_KEY = "63d5648e9125519e5f284d89a1e50f3e";

        @Override
        protected String doInBackground(Object[] params) {
            String endpoint = (String) params[0];
            String urlString = "";
            String resultToDisplay = "";

            switch(endpoint) {
                case "search":
                    urlString = URL + "?key=" + API_KEY + "?q=" + query;
                    break;

                default:
                    System.err.println("Unknown endpoint");
                    break;
            }

            InputStream in = null;

            try {
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                in = new BufferedInputStream(urlConnection.getInputStream());

            } catch (Exception e ) {
                System.out.println(e.getMessage());
                return e.getMessage();
            }

            return resultToDisplay;
        }

        protected void onPostExecute(String result) {

        }

        public List<Message> readJsonStream(InputStream in) throws IOException {
            JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
            List<Message> messages = new ArrayList<Message>();
            reader.beginArray();
            while (reader.hasNext()) {
                Message message = gson.fromJson(reader, Message.class);
                messages.add(message);
            }
            reader.endArray();
            reader.close();
            return messages;
        }
    }*/
}
