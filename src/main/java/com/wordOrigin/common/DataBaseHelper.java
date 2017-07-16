package com.wordOrigin.common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DataBaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String TAG = "DataBaseHelper";
    private static final String SRC_DATABASE_NAME = "word_origin.db";  // assetsフォルダにあるdbのファイル名
    private static final String DATABASE_NAME = "word_origin.db.db";  // コピー先のDB名
    private static final int DATABASE_VERSION = 1;

    private final Context context;
    private final File databasePath;
    private boolean createDatabase = false;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        this.context = context;
        this.databasePath = context.getDatabasePath(DATABASE_NAME);
    }

    @Override
    public synchronized SQLiteDatabase getWritableDatabase() {
        SQLiteDatabase database = super.getWritableDatabase();
        if (createDatabase) {
            try {
                database = copyDatabase(database);
            } catch (IOException e) {
                //Log.wtf(TAG, e);
            }
        }
        return database;
    }

    private SQLiteDatabase copyDatabase(SQLiteDatabase database) throws IOException {
        // dbがひらきっぱなしなので、書き換えできるように閉じる
        database.close();

        // コピー！
        InputStream input = context.getAssets().open(SRC_DATABASE_NAME);
        OutputStream output = new FileOutputStream(databasePath);
        copy(input, output);

        createDatabase = false;
        // dbを閉じたので、また開く
        return super.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        super.onOpen(db);
        // getWritableDatabase()したときに呼ばれてくるので、
        // 初期化する必要があることを保存する
        this.createDatabase = true;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    // CopyUtilsからのコピペ
    private static int copy(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024 * 4];
        int count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

}
