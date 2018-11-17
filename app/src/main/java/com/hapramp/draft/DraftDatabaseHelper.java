package com.hapramp.draft;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.hapramp.preferences.HaprampPreferenceManager;

import java.util.ArrayList;

import xute.markdeditor.models.DraftModel;

public class DraftDatabaseHelper extends SQLiteOpenHelper {
  public static final String COLUMN_ID = "draftID";
  public static final String COLUMN_DRAFT_TYPE = "draftType";
  public static final String COLUMN_DRAFT = "draft";
  private static final int DATABASE_VERSION = 2;
  private static final String DATABASE_NAME = "draftDB.db";
  public static String TABLE_DRAFTS = HaprampPreferenceManager.getInstance().getCurrentSteemUsername() + "_Drafts";
  String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_DRAFTS + "(" + COLUMN_ID +
    " INTEGER PRIMARY KEY ," + COLUMN_DRAFT + " TEXT ," + COLUMN_DRAFT_TYPE + " INTEGER)";

  public DraftDatabaseHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
    createTable(getWritableDatabase());
  }

  private void createTable(SQLiteDatabase sqLiteDatabase) {
    sqLiteDatabase.execSQL(CREATE_TABLE);
  }

  /**
   * Create
   *
   * @param draftModel
   * @return
   */
  public boolean insertBlogDraft(DraftModel draftModel) {
    return insertRawBlogDraft(draftModel.getDraftId(), new Gson().toJson(draftModel));
  }

  /**
   * Inserts a row of raw blog draft
   *
   * @param draftId id of Draft (timestamp)
   * @param json    de-serialized form of DraftModel
   * @return status of insert
   */
  public boolean insertRawBlogDraft(long draftId, String json) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put(COLUMN_DRAFT_TYPE, String.valueOf(DraftType.BLOG));
    contentValues.put(COLUMN_ID, draftId);
    contentValues.put(COLUMN_DRAFT, json);
    long id = db.insert(TABLE_DRAFTS, null, contentValues);
    return id != -1;
  }

  public boolean insertContestDraft(ContestDraftModel draftModel) {
    return insertRawContestDraft(draftModel.getDraftId(), new Gson().toJson(draftModel));
  }

  /**
   * Inserts a row of raw contest draft
   *
   * @param draftId id of Draft (timestamp)
   * @param json    de-serialized form of ContestDraftModel
   * @return status of insert
   */
  public boolean insertRawContestDraft(long draftId, String json) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put(COLUMN_DRAFT_TYPE, String.valueOf(DraftType.COMPETITION));
    contentValues.put(COLUMN_ID, draftId);
    contentValues.put(COLUMN_DRAFT, json);
    long id = db.insert(TABLE_DRAFTS, null, contentValues);
    return id != -1;
  }

  /**
   * @return all the stored drafts
   */
  public ArrayList<DraftModel> getAllBlogDrafts() {
    ArrayList<DraftModel> drafts = new ArrayList<>();
    SQLiteDatabase db = this.getReadableDatabase();
    try {
      Cursor res = db.query(TABLE_DRAFTS,
        new String[]{COLUMN_ID, COLUMN_DRAFT, COLUMN_DRAFT_TYPE},
        COLUMN_DRAFT_TYPE + "=?", new String[]{String.valueOf(DraftType.BLOG)}, null, null, null);
      if (res != null) {
        res.moveToFirst();
        boolean hasMore = res.getCount() > 0;
        while (hasMore) {
          String json = res.getString(res.getColumnIndex(COLUMN_DRAFT));
          DraftModel draft = new Gson().fromJson(json, DraftModel.class);
          drafts.add(draft);
          hasMore = res.moveToNext();
        }
      }
    }
    catch (Exception e) {

    }
    return drafts;
  }

  /**
   * @return all the stored drafts
   */
  public ArrayList<ContestDraftModel> getAllContestDrafts() {
    ArrayList<ContestDraftModel> drafts = new ArrayList<>();
    SQLiteDatabase db = this.getReadableDatabase();
    try {
      Cursor res = db.query(TABLE_DRAFTS,
        new String[]{COLUMN_ID, COLUMN_DRAFT, COLUMN_DRAFT_TYPE},
        COLUMN_DRAFT_TYPE + "=?", new String[]{String.valueOf(DraftType.COMPETITION)}, null, null, null);
      if (res != null) {
        res.moveToFirst();
        boolean hasMore = res.getCount() > 0;
        while (hasMore) {
          String json = res.getString(res.getColumnIndex(COLUMN_DRAFT));
          ContestDraftModel draft = new Gson().fromJson(json, ContestDraftModel.class);
          drafts.add(draft);
          hasMore = res.moveToNext();
        }
      }
    }
    catch (Exception e) {

    }
    return drafts;
  }

  /**
   * @param draftId id of draft
   * @return draft matching draftid
   */
  public DraftModel findBlogDraftById(long draftId) {
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.query(TABLE_DRAFTS,
      new String[]{COLUMN_DRAFT, COLUMN_ID},
      COLUMN_ID + " =?",
      new String[]{String.valueOf(draftId)},
      null, null, null);
    if (cursor != null) {
      cursor.moveToFirst();
      return new Gson().fromJson(cursor.getString(cursor.getColumnIndex(COLUMN_DRAFT)), DraftModel.class);
    }
    return null;
  }

  public ContestDraftModel findContestDraftById(long draftId) {
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.query(TABLE_DRAFTS,
      new String[]{COLUMN_DRAFT, COLUMN_ID, COLUMN_DRAFT_TYPE},
      COLUMN_ID + " =?",
      new String[]{String.valueOf(draftId)},
      null, null, null);
    if (cursor != null) {
      cursor.moveToFirst();
      return new Gson().fromJson(cursor.getString(cursor.getColumnIndex(COLUMN_DRAFT)), ContestDraftModel.class);
    }
    return null;
  }

  /**
   * @param draftModel updated draft
   * @return position of update
   */
  public boolean updateBlogDraft(DraftModel draftModel) {
    return updateRawBlogDraft(draftModel.getDraftId(), new Gson().toJson(draftModel));
  }

  public boolean updateRawBlogDraft(long draftId, String json) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(COLUMN_ID, draftId);
    values.put(COLUMN_DRAFT, json);
    values.put(COLUMN_DRAFT_TYPE, String.valueOf(DraftType.BLOG));
    return db.update(TABLE_DRAFTS, values, COLUMN_ID + " = ?",
      new String[]{String.valueOf(draftId)}) != -1;
  }

  public boolean updateContestDraft(ContestDraftModel draftModel) {
    return updateRawContestDraft(draftModel.getDraftId(), new Gson().toJson(draftModel));
  }

  public boolean updateRawContestDraft(long draftId, String json) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(COLUMN_ID, draftId);
    values.put(COLUMN_DRAFT, json);
    values.put(COLUMN_DRAFT_TYPE, String.valueOf(DraftType.COMPETITION));
    return db.update(TABLE_DRAFTS, values, COLUMN_ID + " = ?",
      new String[]{String.valueOf(draftId)}) != -1;
  }

  public boolean deleteDraft(long id) {
    SQLiteDatabase db = this.getWritableDatabase();
    int pos = db.delete(TABLE_DRAFTS, COLUMN_ID + " = ?",
      new String[]{String.valueOf(id)});
    db.close();
    return pos != -1;
  }

  @Override
  public void onCreate(SQLiteDatabase sqLiteDatabase) {
    createTable(sqLiteDatabase);
  }



  @Override
  public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_DRAFTS);
    onCreate(sqLiteDatabase);
  }
}
