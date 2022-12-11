package com.project.androidlab.db;

public class ContactsTableHelper {

    public static final String TABLE_NAME = "contact";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_LAST_NAME = "last_name";
    public static final String COLUMN_NUMBER = "number";

    public static final String QUERY_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_FIRST_NAME + " TEXT," +
                    COLUMN_LAST_NAME + " TEXT," +
                    COLUMN_NUMBER + " TEXT)";


    public static final String QUERY_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static String getInsertQuery(Contact contact) {
        return "INSERT INTO " + TABLE_NAME + " (" +
                COLUMN_FIRST_NAME + ", " +
                COLUMN_LAST_NAME + ", " +
                COLUMN_NUMBER + ") VALUES('" +
                contact.getFirstName() + "', '" +
                contact.getLastName() + "', " +
                contact.getPhoneNumber() + ");";
    }

    public static String getAllQuery() {
        return "SELECT * FROM " + TABLE_NAME;
    }

    // Контакти, у яких прізвище починається на "Т"
    public static String getContactsSelectionByVariantQuery() {
        return "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_LAST_NAME + " LIKE 'Т%';";
    }

    public static Contact[] generateContactsArray() {
        return new Contact[]{
                new Contact("Віктор", "Куценко", "1234567890"),
                new Contact("Олександра", "Тарасенко", "2345678901"),
                new Contact("Надія", "Олешко", "3456789012"),
                new Contact("Іван", "Тополя", "4567890123"),
                new Contact("Наталія", "Томчук", "5678901234"),
                new Contact("Юрій", "Коваленко", "6789012345"),
                new Contact("Павло", "Вознюк", "7890123456"),
                new Contact("Лідія", "Голінченко", "8901234567"),
        };
    }
}
