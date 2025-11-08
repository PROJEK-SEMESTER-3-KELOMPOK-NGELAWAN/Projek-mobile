package com.majelismdpl.majelis_mdpl.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.majelismdpl.majelis_mdpl.models.User;

/**
 * ============================================
 * UserManager (Pengatur Data User)
 * Fungsi: Simpan, ambil, update data user dari database HP
 * Menggunakan OOP Pattern untuk clean code
 * ============================================
 */
public class UserManager {

    private LocalDatabase database;
    private Context context;

    /**
     * Constructor
     */
    public UserManager(Context context) {
        this.context = context;
        this.database = LocalDatabase.getInstance(context);
    }

    /**
     * Simpan user yang login ke database HP
     */
    public long saveLoggedInUser(User user) {
        SQLiteDatabase db = null;
        long id = -1;

        try {
            db = database.getWritableDatabase();

            // ========== FIX: Hapus user lama TANPA close database ==========
            db.delete(LocalDatabase.TABLE_USERS, null, null);

            // Siapkan data
            ContentValues values = new ContentValues();
            values.put(LocalDatabase.COL_ID_USER, user.getId());
            values.put(LocalDatabase.COL_USERNAME, user.getUsername());
            values.put(LocalDatabase.COL_EMAIL, user.getEmail());
            values.put(LocalDatabase.COL_ROLE, user.getRole() != null ? user.getRole() : "user");
            values.put(LocalDatabase.COL_WHATSAPP, user.getWhatsapp());
            values.put(LocalDatabase.COL_ALAMAT, user.getAlamat());
            values.put(LocalDatabase.COL_FOTO_PROFIL, user.getFotoUrl());
            values.put(LocalDatabase.COL_IS_LOGGED_IN, 1);

            // Insert ke database
            id = db.insert(LocalDatabase.TABLE_USERS, null, values);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // ========== CLOSE DATABASE DI SINI ==========
            if (db != null && db.isOpen()) {
                db.close();
            }
        }

        return id;
    }

    /**
     * Ambil user yang sedang login
     */
    public User getLoggedInUser() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        User user = null;

        try {
            db = database.getReadableDatabase();

            String query = "SELECT * FROM " + LocalDatabase.TABLE_USERS
                    + " WHERE " + LocalDatabase.COL_IS_LOGGED_IN + " = 1 LIMIT 1";

            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(LocalDatabase.COL_ID_USER)));
                user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(LocalDatabase.COL_USERNAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(LocalDatabase.COL_EMAIL)));
                user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(LocalDatabase.COL_ROLE)));
                user.setWhatsapp(cursor.getString(cursor.getColumnIndexOrThrow(LocalDatabase.COL_WHATSAPP)));
                user.setAlamat(cursor.getString(cursor.getColumnIndexOrThrow(LocalDatabase.COL_ALAMAT)));
                user.setFotoUrl(cursor.getString(cursor.getColumnIndexOrThrow(LocalDatabase.COL_FOTO_PROFIL)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close cursor & database
            if (cursor != null) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }

        return user;
    }

    /**
     * Cek apakah user sudah login
     */
    public boolean isUserLoggedIn() {
        return getLoggedInUser() != null;
    }

    /**
     * Logout user (hapus dari database)
     */
    public void logoutUser() {
        SQLiteDatabase db = null;

        try {
            db = database.getWritableDatabase();
            db.delete(LocalDatabase.TABLE_USERS,
                    LocalDatabase.COL_IS_LOGGED_IN + " = ?",
                    new String[]{"1"});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    /**
     * Update data user
     */
    public int updateUser(User user) {
        SQLiteDatabase db = null;
        int rowsAffected = 0;

        try {
            db = database.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(LocalDatabase.COL_USERNAME, user.getUsername());
            values.put(LocalDatabase.COL_EMAIL, user.getEmail());
            values.put(LocalDatabase.COL_WHATSAPP, user.getWhatsapp());
            values.put(LocalDatabase.COL_ALAMAT, user.getAlamat());

            rowsAffected = db.update(
                    LocalDatabase.TABLE_USERS,
                    values,
                    LocalDatabase.COL_ID_USER + " = ?",
                    new String[]{String.valueOf(user.getId())}
            );

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }

        return rowsAffected;
    }
}
