package ir.vasl.chatkitlight.database;

import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class DbMigration {

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE TABLE_CONVERSATION "
                    + " ADD COLUMN fileType INTEGER");
            database.execSQL("ALTER TABLE TABLE_CONVERSATION "
                    + " ADD COLUMN fileAddress TEXT");
        }
    };

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE TABLE_CONVERSATION "
                    + " ADD COLUMN imageUrl TEXT");
        }
    };

    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE TABLE_CONVERSATION "
                    + " ADD COLUMN imageRes TEXT");
        }
    };

    static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            //
        }
    };

    static final Migration MIGRATION_5_6 = new Migration(5, 6) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE TABLE_CONVERSATION "
                    + " ADD COLUMN serverSideId TEXT");
        }
    };
}
