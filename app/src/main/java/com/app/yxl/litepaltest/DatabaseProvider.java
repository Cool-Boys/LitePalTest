package com.app.yxl.litepaltest;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.List;

public class DatabaseProvider extends ContentProvider {
    public static final int BOOK_DIR = 0;
    public static final int BOOK_ITEM = 1;
    public static final int CATEGORY_DIR = 2;
    public static final int CATEGORY_ITEM = 3;

    public static final String AUTHORITY = "com.app.yxl.litepaltest";
    private static UriMatcher uriMatcher;


    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "book", BOOK_DIR);
        uriMatcher.addURI(AUTHORITY, "book/#", BOOK_ITEM);
        uriMatcher.addURI(AUTHORITY, "category", CATEGORY_DIR);
        uriMatcher.addURI(AUTHORITY, "category/#", CATEGORY_ITEM);
    }

    public DatabaseProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        //throw new UnsupportedOperationException("Not yet implemented");
        int deleteRows = 0;
        switch (uriMatcher.match(uri)) {
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                DataSupport.deleteAll(Book.class, "id=?", bookId);
                break;
            default:
                break;
        }
        return deleteRows;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        // throw new UnsupportedOperationException("Not yet implemented");
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                return "vnd.android.cursor.dir/vnd.com.app.yxl.litepaltest.provider.book";

            case BOOK_ITEM:
                return "vnd.android.cursor.item/vnd.com.app.yxl.litepaltest.provider.book";

            default:
                break;
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        // throw new UnsupportedOperationException("Not yet implemented");
        Uri uriReturn = null;
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
            case BOOK_ITEM:
                Book book = new Book();
                book.setName(values.getAsString("name"));
                book.setAuthor(values.getAsString("author"));
                book.setPages(values.getAsInteger("pages"));
                book.setPrice(values.getAsDouble("price"));
                book.setPress(values.getAsString("press"));
                book.save();
                long newBookId = book.getId();
                uriReturn = Uri.parse("content://" + AUTHORITY + "/book/" + newBookId);
                break;
            default:
                break;

        }

        return uriReturn;

    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        Connector.getDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
//        throw new UnsupportedOperationException("Not yet implemented");
        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:

                cursor = DataSupport.findBySQL("SELECT * FROM BOOK");

                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                cursor = DataSupport.findBySQL("SELECT * FROM BOOK WHERE id=" + bookId);
                break;
            default:
                break;
        }
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        // throw new UnsupportedOperationException("Not yet implemented");
        Book book = new Book();
        int updateRows = 0;
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:


                book.setPrice(values.getAsDouble("price"));
                book.setPress(values.getAsString("press"));
                String strWhere = selection;
                for (String aa : selectionArgs) {
                    strWhere += aa + ",";
                }
                updateRows = book.updateAll(strWhere.substring(0, strWhere.length() - 1));
                break;
            case BOOK_ITEM:
                book.setPrice(values.getAsDouble("price"));
                book.setPress(values.getAsString("press"));
                String bookId = uri.getPathSegments().get(1);
                updateRows = book.update(Long.parseLong(bookId));
                break;
            default:
                break;
        }
        return updateRows;

    }
}
