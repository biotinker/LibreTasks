# Introduction #

To make the new changes made on the database (either a new table entry or a change is schema) visible, one has to either:

  1. Uninstall and re-install omnidroid so that the database would be destroyed and DbHelper.onCreate() would be triggered when Omnidroid is initialized.
  1. Change the value of DbHelper.DATABASE\_VERSION so DbHelper.onUpgrade() would be triggered when Omnidroid is initialized. Please refer to the [SQLiteOpenHelper reference](http://developer.android.com/reference/android/database/sqlite/SQLiteOpenHelper.html#SQLiteOpenHelper(android.content.Context,%20java.lang.String,%20android.database.sqlite.SQLiteDatabase.CursorFactory,%20int)) for more details on how this is triggered.

# Problem #

Prior to [r744](https://code.google.com/p/omnidroid/source/detail?r=744), DbHelper.onUpgrade() is dropping all the existing tables and reconstructing them again from scratch. This means that whenever a user tries to upgrade his Omnidroid to a newer version, all the rules he created will be lost.

# Counter Measure #

The solution that the team came up with is to perform a database migration instead, therefore, all the previous existing data would still remain.

# Details #

To make changes on the database, just do the following:
  * Create a new private method in DbMigration that will perform the database update and call the new method inside DbMigration.migrateToLatest()
  * Increment the value of the DATABASE\_VERSION at DbHelper.java

The database migration implementation is made in [issue 71](https://code.google.com/p/omnidroid/issues/detail?id=71) and details on the code changes with sample database update can be seen on this [code review](http://codereview.appspot.com/1683041) (Just check DbHelper.java and DbMigration.java for the gist).