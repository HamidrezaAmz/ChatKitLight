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
}
