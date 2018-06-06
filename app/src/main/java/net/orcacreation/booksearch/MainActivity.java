package net.orcacreation.booksearch;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Book>> {

    public static final String LOG_TAG = MainActivity.class.getName();
    Toolbar mainToolbar;
    SearchView mainSearchView;
    String googleBookRequestUrl;
    TextView statusMessageView;
    ProgressBar progressBarView;
    TextView emptyListMessageView;

    /**
     * Test URL for harry portal search data from the Google Book
     */
    // private static final String GOOGLE_BOOK_REQUEST_URL =
    // "https://www.googleapis.com/books/v1/volumes?tbm=bks&amp;q=harry+potter";

    private BookAdaptor mAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusMessageView = findViewById(R.id.status_message_view);
        progressBarView = findViewById(R.id.progress_bar_view);
        emptyListMessageView = findViewById(R.id.empty_list_message_view);
        mainToolbar = findViewById(R.id.toolbar_view);
        mainSearchView = findViewById(R.id.search_view);

        if (mainToolbar != null) {
            setSupportActionBar(mainToolbar);
            mainToolbar.setTitle(R.string.app_name);
        } else {
            Log.e(LOG_TAG, "mainToolbar is null");
        }

        googleBookRequestUrl = getString(R.string.googlebook_request_url_templete) + getString(R.string.query_test);
        //googleBookRequestUrl = getString(R.string.googlebook_request_url_harry_potter);
        Log.e(LOG_TAG, "googleBookRequestUrl is:" + googleBookRequestUrl);
        refreshScreen();
    }

    private void refreshScreen() {

        // Find a reference to the {@link ListView} in the layout
        ListView bookListView = findViewById(R.id.list_view);
        bookListView.setEmptyView(findViewById(R.id.empty_list_message_view));
        statusMessageView.setText(R.string.loading_message);

        // Create a new {@link ArrayAdapter} of books
        mAdaptor = new BookAdaptor(this, new ArrayList<Book>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        bookListView.setAdapter(mAdaptor);

        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book currentBook = (Book) mAdaptor.getItem(position);
                Uri urlWeb = Uri.parse(currentBook.getmUrlWeb());
                Intent webIntent = new Intent(Intent.ACTION_VIEW, urlWeb);
                startActivity(webIntent);
            }
        });

        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            getLoaderManager().initLoader(0, null, this);
        } else {
            statusMessageView.setText(R.string.no_internet_message);
            statusMessageView.setVisibility(View.VISIBLE);
            progressBarView.setVisibility(View.GONE);
        }
    }


    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        return new BookLoader(this, googleBookRequestUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> data) {

        emptyListMessageView.setText(null);
        statusMessageView.setVisibility(View.GONE);
        progressBarView.setVisibility(View.GONE);

        //Clear the adapter of previous book data
        mAdaptor.clear();

        //If there is a valid list of Books, then add them to the adapter's
        //dataset. This will trigger the ListView update

        if ((data != null && !data.isEmpty())) {
            mAdaptor.addAll(data);

        } else {
            emptyListMessageView.setText(R.string.empty_list_message);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        mAdaptor.clear();
    }
}
