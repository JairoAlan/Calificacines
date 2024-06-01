package com.example.calificacines;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import androidx.annotation.Nullable;

public class BaseDeDatos extends SQLiteOpenHelper
{
    public static abstract class Datostabla implements BaseColumns {
        public static final String Nombre_Table = "Datos";
        public static final String Columna_Matr = "Matricula";
        public static final String Columna_Nombre = "Nombre";
        public static final String Columna_Materia = "Materia";
        public static final String Columna_Carrera = "Carrera";
        public static final String Columna_Parcial1 = "Parcial1";
        public static final String Columna_Parcial2 = "Parcial2";
        public static final String Columna_Parcial3 = "Parcial3";

        private static final String Crear_Table = "create table " + Nombre_Table + " (" +
                Columna_Matr + " integer primary key, " +
                Columna_Nombre + " text not null, " +
                Columna_Materia + " text not null, " +
                Columna_Carrera + " text not null, " +
                Columna_Parcial1 + " integer, " +
                Columna_Parcial2 + " integer, " +
                Columna_Parcial3 + " integer" + ");";

        private static final String sql_Delete = "drop table if exists " + Nombre_Table;
    }

    public static final int DataBase_Version=2;
    public static final String DataBase_Name="directorio.db";


    public BaseDeDatos(@Nullable Context context) {
        super(context, DataBase_Name, null, DataBase_Version);
    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(Datostabla.Crear_Table);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(Datostabla.sql_Delete);
        onCreate(db);
    }
}