package com.example.calificacines;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    EditText etMatricula, etNombre, etMateria, etCarrera, etParcial1, etParcial2, etParcial3, etPromedio;
    Button btnBuscar, btnInsertar, btnCalificaciones;
    BaseDeDatos ayudaDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        etMatricula = findViewById(R.id.etMatricula);
        etNombre = findViewById(R.id.etNombre);
        etMateria = findViewById(R.id.etMateria);
        etCarrera = findViewById(R.id.etCarrera);
        etParcial1 = findViewById(R.id.etParcial1);
        etParcial2 = findViewById(R.id.etParcial2);
        etParcial3 = findViewById(R.id.etParcial3);
        etPromedio = findViewById(R.id.etPromedio);

        btnBuscar = findViewById(R.id.btnBuscar);
        btnInsertar = findViewById(R.id.btnInsertar);
        btnCalificaciones = findViewById(R.id.btnCalificaciones);

        ayudaDB = new BaseDeDatos(getApplicationContext());

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarAlumno();
            }
        });

        btnInsertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertarAlumno();
            }
        });

        btnCalificaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarCalificacion();
            }
        });
    }

    private void buscarAlumno() {
        String matricula = etMatricula.getText().toString();
        if (matricula.isEmpty()) {
            Toast.makeText(this, "Ingrese una matrícula", Toast.LENGTH_LONG).show();
            return;
        }

        SQLiteDatabase db = ayudaDB.getReadableDatabase();
        Cursor cursor = db.query(BaseDeDatos.Datostabla.Nombre_Table,
                null,
                BaseDeDatos.Datostabla.Columna_Matr + " = ?",
                new String[]{matricula},
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            etNombre.setText(cursor.getString(cursor.getColumnIndexOrThrow(BaseDeDatos.Datostabla.Columna_Nombre)));
            etMateria.setText(cursor.getString(cursor.getColumnIndexOrThrow(BaseDeDatos.Datostabla.Columna_Materia)));
            etCarrera.setText(cursor.getString(cursor.getColumnIndexOrThrow(BaseDeDatos.Datostabla.Columna_Carrera)));
            etParcial1.setText(cursor.isNull(cursor.getColumnIndexOrThrow(BaseDeDatos.Datostabla.Columna_Parcial1)) ? "" : cursor.getString(cursor.getColumnIndexOrThrow(BaseDeDatos.Datostabla.Columna_Parcial1)));
            etParcial2.setText(cursor.isNull(cursor.getColumnIndexOrThrow(BaseDeDatos.Datostabla.Columna_Parcial2)) ? "" : cursor.getString(cursor.getColumnIndexOrThrow(BaseDeDatos.Datostabla.Columna_Parcial2)));
            etParcial3.setText(cursor.isNull(cursor.getColumnIndexOrThrow(BaseDeDatos.Datostabla.Columna_Parcial3)) ? "" : cursor.getString(cursor.getColumnIndexOrThrow(BaseDeDatos.Datostabla.Columna_Parcial3)));

            calcularPromedio();
        } else {
            Toast.makeText(this, "Alumno no encontrado", Toast.LENGTH_LONG).show();
        }
        cursor.close();
    }

    private void insertarAlumno() {
        String matricula = etMatricula.getText().toString();
        String nombre = etNombre.getText().toString();
        String materia = etMateria.getText().toString();
        String carrera = etCarrera.getText().toString();

        if (matricula.isEmpty() || nombre.isEmpty() || materia.isEmpty() || carrera.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_LONG).show();
            return;
        }

        SQLiteDatabase db = ayudaDB.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(BaseDeDatos.Datostabla.Columna_Matr, matricula);
        valores.put(BaseDeDatos.Datostabla.Columna_Nombre, nombre);
        valores.put(BaseDeDatos.Datostabla.Columna_Materia, materia);
        valores.put(BaseDeDatos.Datostabla.Columna_Carrera, carrera);

        long newRowId = db.insert(BaseDeDatos.Datostabla.Nombre_Table, null, valores);
        if (newRowId == -1) {
            Toast.makeText(this, "Error al guardar los datos", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Alumno registrado exitosamente", Toast.LENGTH_LONG).show();
        }
    }


    private void registrarCalificacion() {
        String matricula = etMatricula.getText().toString();
        if (matricula.isEmpty()) {
            Toast.makeText(this, "Ingrese una matrícula", Toast.LENGTH_LONG).show();
            return;
        }

        SQLiteDatabase db = ayudaDB.getWritableDatabase();
        Cursor cursor = db.query(BaseDeDatos.Datostabla.Nombre_Table,
                null,
                BaseDeDatos.Datostabla.Columna_Matr + " = ?",
                new String[]{matricula},
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            ContentValues valores = new ContentValues();
            String parcial1 = etParcial1.getText().toString();
            String parcial2 = etParcial2.getText().toString();
            String parcial3 = etParcial3.getText().toString();

            if (!parcial3.isEmpty()) {
                if (cursor.isNull(cursor.getColumnIndexOrThrow(BaseDeDatos.Datostabla.Columna_Parcial2))) {
                    Toast.makeText(this, "Debe registrar el segundo parcial antes de registrar el tercero", Toast.LENGTH_LONG).show();
                    cursor.close();
                    return;
                }
                valores.put(BaseDeDatos.Datostabla.Columna_Parcial3, Integer.parseInt(parcial3));
            } else if (!parcial2.isEmpty()) {
                if (cursor.isNull(cursor.getColumnIndexOrThrow(BaseDeDatos.Datostabla.Columna_Parcial1))) {
                    Toast.makeText(this, "Debe registrar el primer parcial antes de registrar el segundo", Toast.LENGTH_LONG).show();
                    cursor.close();
                    return;
                }
                valores.put(BaseDeDatos.Datostabla.Columna_Parcial2, Integer.parseInt(parcial2));
            } else if (!parcial1.isEmpty()) {
                valores.put(BaseDeDatos.Datostabla.Columna_Parcial1, Integer.parseInt(parcial1));
            } else {
                Toast.makeText(this, "Ingrese una calificación", Toast.LENGTH_LONG).show();
                cursor.close();
                return;
            }

            int rowsUpdated = db.update(BaseDeDatos.Datostabla.Nombre_Table, valores,
                    BaseDeDatos.Datostabla.Columna_Matr + " = ?", new String[]{matricula});
            if (rowsUpdated > 0) {
                Toast.makeText(this, "Calificación registrada exitosamente", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Error al registrar la calificación", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Alumno no encontrado", Toast.LENGTH_LONG).show();
        }
        cursor.close();
    }

    private void calcularPromedio() {
        try {
            String parcial1 = etParcial1.getText().toString();
            String parcial2 = etParcial2.getText().toString();
            String parcial3 = etParcial3.getText().toString();

            int p1 = parcial1.isEmpty() ? 0 : Integer.parseInt(parcial1);
            int p2 = parcial2.isEmpty() ? 0 : Integer.parseInt(parcial2);
            int p3 = parcial3.isEmpty() ? 0 : Integer.parseInt(parcial3);

            int count = 0;
            if (!parcial1.isEmpty()) count++;
            if (!parcial2.isEmpty()) count++;
            if (!parcial3.isEmpty()) count++;

            if (count > 0) {
                int promedio = (p1 + p2 + p3) / count;
                etPromedio.setText(String.valueOf(promedio));
            } else {
                etPromedio.setText("");
            }
        } catch (NumberFormatException e) {
            etPromedio.setText("");
        }
    }
}
