package com.example.roomdatabaseretrofit.Database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.roomdatabaseretrofit.Dao.ActorDao;
import com.example.roomdatabaseretrofit.Modal.Actor;

@Database(entities = {Actor.class},version = 1)
public abstract class ActorDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "ActorDatabase";

    public abstract ActorDao actorDao();

    private static volatile ActorDatabase INSTANCE;

    public static ActorDatabase getInstance(Context context){
        if (INSTANCE == null){
            synchronized (ActorDatabase.class){
                if (INSTANCE == null){
                    INSTANCE= Room.databaseBuilder(context,ActorDatabase.class
                            ,DATABASE_NAME)
                            .addCallback(callback)
                            .build();

                }
            }
        }
        return INSTANCE;
    }

    static Callback callback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateAsynTask(INSTANCE);

        }
    };

    static class PopulateAsynTask extends AsyncTask<Void,Void,Void>
    {
        private ActorDao actorDao;
        PopulateAsynTask(ActorDatabase actorDatabase){
            actorDao=actorDatabase.actorDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            actorDao.deleteAll();
            return null;
        }
    }
}
