package net.orcacreation.booksearch;

class Book {
    private final String mTitle;
    private final String mAuthor;
    private final String mUrlWeb;
    private final String mUrlImage;

    Book(String title, String author, String urlWeb, String urlImage){
        mTitle = title;
        mAuthor = author;
        mUrlWeb = urlWeb;
        mUrlImage = urlImage;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public String getmUrlWeb() {
        return mUrlWeb;
    }

    public String getmUrlImage() {
        return mUrlImage;
    }
}
