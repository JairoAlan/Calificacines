package com.example.calificacines;

// Librerias que se utilizaran al crear una base de datos
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import androidx.annotation.Nullable;
// Clase BaseDeDatos que hereda de SQLiteOpenHelper
public class BaseDeDatos extends SQLiteOpenHelper
{
    // Clase abstracta que contendra todas los dato de la tabla asi como su nombre
    public static abstract class Datostabla implements BaseColumns {
        // Atributos la base de datos
        public static final String Nombre_Table = "Datos";
        public static final String Columna_Matr = "Matricula";
        public static final String Columna_Nombre = "Nombre";
        public static final String Columna_Materia = "Materia";
        public static final String Columna_Carrera = "Carrera";
        public static final String Columna_Parcial1 = "Parcial1";
        public static final String Columna_Parcial2 = "Parcial2";
        public static final String Columna_Parcial3 = "Parcial3";

        // Crea una tabla que configura los atrubutos de cada columna
        private static final String Crear_Table = "create table " + Nombre_Table + " (" +
                Columna_Matr + " integer primary key, " +
                Columna_Nombre + " text not null, " +
                Columna_Materia + " text not null, " +
                Columna_Carrera + " text not null, " +
                Columna_Parcial1 + " integer, " +
                Columna_Parcial2 + " integer, " +
                Columna_Parcial3 + " integer" + ");";

        // instruccion que borra la tabla
        private static final String sql_Delete = "drop table if exists " + Nombre_Table;
    }

    // Version de la tabla, en mi caso es 2, por que la 1 tenia errores
    public static final int DataBase_Version=2;
    // Nombre de la base de datos, que se guardara en el telefono
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