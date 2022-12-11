package com.project.androidlab.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context) {
        super(context, "universities_and_contacts.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(UniversityTableHelper.QUERY_CREATE_TABLE);
        sqLiteDatabase.execSQL(ContactsTableHelper.QUERY_CREATE_TABLE);

        University[] universitiesToInsert = UniversityTableHelper.generateUniversitiesArray();
        for (University u : universitiesToInsert) {
            sqLiteDatabase.execSQL(UniversityTableHelper.getInsertQuery(u));
        }

        Contact[] contactsToInsert = ContactsTableHelper.generateContactsArray();
        for (Contact c: contactsToInsert) {
            sqLiteDatabase.execSQL(ContactsTableHelper.getInsertQuery(c));
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(UniversityTableHelper.QUERY_DELETE_TABLE);
        sqLiteDatabase.execSQL(ContactsTableHelper.QUERY_DELETE_TABLE);
        onCreate(sqLiteDatabase);
    }

    public List<University> getAllUniversities() {
        try(SQLiteDatabase database = getReadableDatabase();
            Cursor cursor = database.rawQuery(UniversityTableHelper.getAllQuery(), null)) {
            List<University> universities = new ArrayList<>();
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(UniversityTableHelper.COLUMN_ID));
                String universityName = cursor.getString(cursor.getColumnIndexOrThrow(UniversityTableHelper.COLUMN_UNIVERSITY_NAME));
                String city = cursor.getString(cursor.getColumnIndexOrThrow(UniversityTableHelper.COLUMN_CITY));
                int studentsNumber = cursor.getInt(cursor.getColumnIndexOrThrow(UniversityTableHelper.COLUMN_STUDENTS_NUMBER));
                int rating = cursor.getInt(cursor.getColumnIndexOrThrow(UniversityTableHelper.COLUMN_WEBOMETRICS_EXCELLENCE_RATING));
                universities.add(new University(id, universityName, city, studentsNumber, rating));
            }
            return universities;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<String> getUniversitiesNamesByVariantCondition() {
        try(SQLiteDatabase database = getReadableDatabase();
            Cursor cursor = database.rawQuery(UniversityTableHelper.getByVariantConditionQuery(), null)) {
            List<String> universitiesNames = new ArrayList<>();
            while (cursor.moveToNext()) {
                String universityName = cursor.getString(cursor.getColumnIndexOrThrow(UniversityTableHelper.COLUMN_UNIVERSITY_NAME));
                universitiesNames.add(universityName);
            }
            return universitiesNames;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public Map<String, Integer> getMaxAndMinRating() {
        try(SQLiteDatabase database = getReadableDatabase();
            Cursor cursor = database.rawQuery(UniversityTableHelper.getMaxAndMinRatingQuery(), null)) {
            if (cursor.moveToNext()) {
                int maxValue = cursor.getInt(cursor.getColumnIndexOrThrow(Constants.MAX_RATING_KEY));
                int minValue = cursor.getInt(cursor.getColumnIndexOrThrow(Constants.MIN_RATING_KEY));
                return new HashMap<String, Integer>() {{
                    put(Constants.MAX_RATING_KEY, maxValue);
                    put(Constants.MIN_RATING_KEY, minValue);
                }};
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    public List<Contact> getAllContacts() {
        try(SQLiteDatabase database = getReadableDatabase();
            Cursor cursor = database.rawQuery(ContactsTableHelper.getAllQuery(), null)) {
            List<Contact> contacts = new ArrayList<>();
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(ContactsTableHelper.COLUMN_ID));
                String firstName = cursor.getString(cursor.getColumnIndexOrThrow(ContactsTableHelper.COLUMN_FIRST_NAME));
                String lastName = cursor.getString(cursor.getColumnIndexOrThrow(ContactsTableHelper.COLUMN_LAST_NAME));
                String number = cursor.getString(cursor.getColumnIndexOrThrow(ContactsTableHelper.COLUMN_NUMBER));
                contacts.add(new Contact(id, firstName, lastName, number));
            }
            return contacts;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    // Контакти, у яких прізвище починається на "Т"
    public List<Contact> getContactsSelectionByVariantCondition() {
        try(SQLiteDatabase database = getReadableDatabase();
            Cursor cursor = database.rawQuery(ContactsTableHelper.getContactsSelectionByVariantQuery(), null)) {
            List<Contact> contacts = new ArrayList<>();
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(UniversityTableHelper.COLUMN_ID));
                String firstName = cursor.getString(cursor.getColumnIndexOrThrow(ContactsTableHelper.COLUMN_FIRST_NAME));
                String lastName = cursor.getString(cursor.getColumnIndexOrThrow(ContactsTableHelper.COLUMN_LAST_NAME));
                String number = cursor.getString(cursor.getColumnIndexOrThrow(ContactsTableHelper.COLUMN_NUMBER));
                contacts.add(new Contact(id, firstName, lastName, number));
            }
            return contacts;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
