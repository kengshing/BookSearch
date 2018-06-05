package net.orcacreation.booksearch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

class BookAdaptor extends ArrayAdapter {
    private static final String LOG_TAG = BookAdaptor.class.getSimpleName();

    public BookAdaptor(@NonNull Context context, @NonNull ArrayList<Book> books) {
        super(context, 0, books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listView = convertView;

        if (listView == null){
            listView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        final Book currentBook = (Book) getItem(position);

        TextView textTitleView = listView.findViewById(R.id.txt_title_view);
        textTitleView.setText(currentBook.getmTitle());

        TextView textAuthorView = listView.findViewById(R.id.txt_author_view);
        textAuthorView.setText(currentBook.getmAuthor());

        //TO-DO: load cover image
        ImageView bookImageView = listView.findViewById(R.id.book_image_view);

        return listView;
    }
    }
