package com.project.androidlab.db;

public class UniversityTableHelper {

    public static final String TABLE_NAME = "university";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_UNIVERSITY_NAME = "university_full_name";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_STUDENTS_NUMBER = "students_number";
    public static final String COLUMN_WEBOMETRICS_EXCELLENCE_RATING = "webometrics_excellence_rating";

    public static final String QUERY_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_UNIVERSITY_NAME + " TEXT," +
                    COLUMN_CITY + " TEXT," +
                    COLUMN_STUDENTS_NUMBER + " INTEGER," +
                    COLUMN_WEBOMETRICS_EXCELLENCE_RATING + " INTEGER)";


    public static final String QUERY_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static String getInsertQuery(University university) {
        return "INSERT INTO " + TABLE_NAME + " (" +
                COLUMN_UNIVERSITY_NAME + ", " +
                COLUMN_CITY + ", " +
                COLUMN_STUDENTS_NUMBER + ", " +
                COLUMN_WEBOMETRICS_EXCELLENCE_RATING + ") VALUES('" +
                university.getUniversityName() + "', '" +
                university.getCity() + "', " +
                university.getStudentsNumber() + ", " +
                university.getRating() + ");";
    }

    public static String getAllQuery() {
        return "SELECT * FROM " + TABLE_NAME;
    }

    //  назви університетів не з Києва, показник Excellence яких <5000
    public static String getByVariantConditionQuery() {
        return "SELECT " + COLUMN_UNIVERSITY_NAME + " FROM " + TABLE_NAME +
                " WHERE " + COLUMN_CITY + " <> 'Київ' AND " +
                COLUMN_WEBOMETRICS_EXCELLENCE_RATING + "<5000;";
    }

    //  максимальний і мінімальний показники Webometrics для українських університетів у БД
    public static String getMaxAndMinRatingQuery() {
        return "SELECT MAX(" + COLUMN_WEBOMETRICS_EXCELLENCE_RATING + ") as " + Constants.MAX_RATING_KEY +
                ", MIN(" + COLUMN_WEBOMETRICS_EXCELLENCE_RATING + ") as " + Constants.MIN_RATING_KEY +
                " FROM " + TABLE_NAME;
    }

    public static University[] generateUniversitiesArray() {
        return new University[]{
                new University("Київський національний університет імені Тараса Шевченка", "Київ", 32000, 1504),
                new University("Сумський державний університет", "Суми", 12000, 2030),
                new University("Національний технічний університет України «Київський політехнічний інститут імені Ігоря Сікорського»", "Київ", 20000, 2531),
                new University("Національний авіаційний університет", "Київ", 16000, 3067),
                new University("Київський Національний Університет Технологій та Дизайну", "Київ", 9000, 5112),
                new University("Вінницький національний медичний університет ім. М. І. Пирогова", "Вінниця", 12000, 4700),
                new University("Національний університет «Києво-Могилянська академія»", "Київ", 3500, 5538),
                new University("Харківський національний університет радіоелектроніки", "Харків", 11000, 2854),
                new University("Чернівецький національний університет імені Ю. Федьковича", "Чернівці", 16000, 3407),
                new University("Національний університет харчових технологій", "Київ", 35000, 5316),
        };
    }
}
