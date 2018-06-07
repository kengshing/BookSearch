package net.orcacreation.booksearch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

class BookAdaptor extends ArrayAdapter {
    private static final String LOG_TAG = BookAdaptor.class.getSimpleName();

    BookAdaptor(@NonNull Context context, @NonNull ArrayList<Book> books) {
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

        if (currentBook == null){
            return listView;
        }

        TextView textTitleView = listView.findViewById(R.id.txt_title_view);
        textTitleView.setText(currentBook.getmTitle());

        TextView textAuthorView = listView.findViewById(R.id.txt_author_view);
        textAuthorView.setText(currentBook.getmAuthor());

        ImageView bookImageView = listView.findViewById(R.id.book_image_view);
        String urlImageString = currentBook.getmUrlImage();
        if (urlImageString !=null){
            Uri urlImage = Uri.parse(urlImageString);
            Picasso.get().load(urlImage).into(bookImageView);
        }
        return listView;
    }
    }
