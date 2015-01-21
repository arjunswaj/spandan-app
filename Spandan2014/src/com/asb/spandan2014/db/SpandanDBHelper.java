package com.asb.spandan2014.db;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.asb.spandan2014.constants.DBConstants;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class SpandanDBHelper extends SQLiteOpenHelper {

  private static final String DB_PATH = "/data/data/com.asb.spandan2014/databases/";

  private static final String DB_NAME = "spandan2014.sqlite";

  private SQLiteDatabase myDataBase;

  private final Context myContext;

  public SpandanDBHelper(Context context) {
    super(context, DB_NAME, null, 1);
    this.myContext = context;
  }

  /**
   * Creates a empty database on the system and rewrites it with your own
   * database.
   * */
  public void createDataBase() throws IOException {

    boolean dbExist = checkDataBase();

    if (dbExist) {
      // do nothing - database already exist
    } else {

      // By calling this method and empty database will be created into the
      // default system path
      // of your application so we are gonna be able to overwrite that database
      // with our database.
      this.getReadableDatabase();

      try {

        copyDataBase();

      } catch (IOException e) {

        throw new Error("Error copying database");

      }
    }

  }

  /**
   * Check if the database already exist to avoid re-copying the file each time
   * you open the application.
   * 
   * @return true if it exists, false if it doesn't
   */
  private boolean checkDataBase() {

    SQLiteDatabase checkDB = null;

    try {
      String myPath = DB_PATH + DB_NAME;
      checkDB = SQLiteDatabase.openDatabase(myPath, null,
          SQLiteDatabase.OPEN_READONLY);

    } catch (SQLiteException e) {

      // database does't exist yet.

    }

    if (checkDB != null) {

      checkDB.close();

    }

    return checkDB != null ? true : false;
  }

  /**
   * Copies your database from your local assets-folder to the just created
   * empty database in the system folder, from where it can be accessed and
   * handled. This is done by transfering bytestream.
   * */
  private void copyDataBase() throws IOException {

    // Open your local db as the input stream
    InputStream myInput = myContext.getAssets().open(DB_NAME);

    // Path to the just created empty db
    String outFileName = DB_PATH + DB_NAME;

    // Open the empty db as the output stream
    OutputStream myOutput = new FileOutputStream(outFileName);

    // transfer bytes from the inputfile to the outputfile
    byte[] buffer = new byte[1024];
    int length;
    while ((length = myInput.read(buffer)) > 0) {
      myOutput.write(buffer, 0, length);
    }

    // Close the streams
    myOutput.flush();
    myOutput.close();
    myInput.close();

  }

  public void openDataBase() throws SQLException {

    // Open the database
    String myPath = DB_PATH + DB_NAME;
    myDataBase = SQLiteDatabase.openDatabase(myPath, null,
        SQLiteDatabase.OPEN_READONLY);

  }

  @Override
  public synchronized void close() {
    if (myDataBase != null)
      myDataBase.close();

    super.close();

  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // TODO Auto-generated method stub

  }

  public Cursor getContacts() {
    return myDataBase.query(DBConstants.CONTACT, new String[] {
        DBConstants.CONTACT_ID, DBConstants.CONTACT_NAME,
        DBConstants.CONTACT_PHONE, DBConstants.CONTACT_EMAIL,
        DBConstants.CONTACT_IMAGE_NAME, DBConstants.CONTACT_COURSE }, null, null,
        null, null, null);
  }

  public Cursor getGameRulesByGameId(int gameId) {
    String[] args = { String.valueOf(gameId) };
    return myDataBase.query(DBConstants.GAME_DETAILS, new String[] {
        DBConstants.GAME_ID, DBConstants.GAME_DETAILS_ID,
        DBConstants.INFO_TYPE, DBConstants.INFO }, "GAME_ID = ?", args, null,
        null, null);
  }

  public Cursor getSpocsByGameId(int gameId) {
    String[] args = { String.valueOf(gameId) };
    return myDataBase.query(DBConstants.SPOC, new String[] {
        DBConstants.SPOC_ID, DBConstants.SPOC_NAME, DBConstants.SPOC_PHONE,
        DBConstants.SPOC_EMAIL, DBConstants.SPOC_IMAGE_NAME }, "GAME_ID = ?",
        args, null, null, null);
  }

  public Cursor getAllGames() {
    return myDataBase.query(DBConstants.GAMES, new String[] {
        DBConstants.GAMES_ID, DBConstants.GAME, DBConstants.IMAGE }, null,
        null, null, null, null);
  }
}
