package com.majelismdpl.majelis_mdpl.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * ============================================
 * LocalDatabase (Database HP)
 * Fungsi: Buat & atur database SQLite di HP
 * ============================================
 */
public class LocalDatabase extends SQLiteOpenHelper {

    // Informasi Database
    private static final String DATABASE_NAME = "db_majelis";
    private static final int DATABASE_VERSION = 1;

    // Nama Tabel
    public static final String TABLE_USERS = "users";

    // Kolom Tabel User (sesuai database web)
    public static final String COL_ID_USER = "id_user";
    public static final String COL_USERNAME = "username";
    public static final String COL_EMAIL = "email";
    public static final String COL_ROLE = "role";
    public static final String COL_WHATSAPP = "whatsapp";
    public static final String COL_ALAMAT = "alamat";
    public static final String COL_FOTO_PROFIL = "foto_profil";
    public static final String COL_IS_LOGGED_IN = "is_logged_in";
    public static final String COL_CREATED_AT = "created_at";

    // Singleton instance
    private static LocalDatabase instance;

    /**
     * Get singleton instance (hanya 1 database untuk seluruh app)
     */
    public static synchronized LocalDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new LocalDatabase(context.getApplicationContext());
        }
        return instance;
    }

    /**
     * Constructor private (tidak bisa dibuat dari luar)
     */
    private LocalDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Buat tabel users
        String CREATE_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COL_ID_USER + " INTEGER PRIMARY KEY,"
                + COL_USERNAME + " TEXT NOT NULL,"
                + COL_EMAIL + " TEXT,"
                + COL_ROLE + " TEXT,"
                + COL_WHATSAPP + " TEXT,"
                + COL_ALAMAT + " TEXT,"
                + COL_FOTO_PROFIL + " TEXT,"
                + COL_IS_LOGGED_IN + " INTEGER DEFAULT 0,"
                + COL_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                + ")";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Hapus tabel lama jika ada update
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    /**
     * Hapus semua data (untuk logout)
     */
    public void clearAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, null, null);
        db.close();
    }
}
