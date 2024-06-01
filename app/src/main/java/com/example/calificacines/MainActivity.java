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
    // Todos los Edittext que se ocuparan
    EditText etMatricula, etNombre, etMateria, etCarrera, etParcial1, etParcial2, etParcial3, etPromedio;
    // Asi como los botones
    Button btnBuscar, btnInsertar, btnCalificaciones;

    // Objeto de la clase Base de Datos
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

        // Referenciamos la SQlHelper
        ayudaDB = new BaseDeDatos(getApplicationContext());

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Metodo para buscarel alumno en la base de datos
                buscarAlumno();
            }
        });

        btnInsertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Metodo para insertar el alumno en la base de datos
                insertarAlumno();
            }
        });

        btnCalificaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Metodo para registrar la calificacion del alumno en la base de datos
                registrarCalificacion();
            }
        });
    }

    // Metodo buscarAlumno
    private void buscarAlumno() {
        // La busqueda se hace con la matricula del alumno
        String matricula = etMatricula.getText().toString();
        // Si la matricula esta vacia
        if (matricula.isEmpty()) {
            // Imprime un mensaje
            Toast.makeText(this, "Ingrese una matrícula", Toast.LENGTH_LONG).show();
            return;
        }
        // Si no, se crea una variable db que lea la base de datos
        SQLiteDatabase db = ayudaDB.getReadableDatabase();
        // Objeto cursor que ejecuta el query para la base de datos. En nuestro caso,
        // Es el nombre de la tabla donde se va a realizar la consulta.
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
            // Este código verifica si el valor de la columna es NULL. Si es NULL, se establece un texto vacío (""). Si no es NULL, se obtiene el valor de la columna y se establece en el EditText correspondiente.
            etParcial1.setText(cursor.isNull(cursor.getColumnIndexOrThrow(BaseDeDatos.Datostabla.Columna_Parcial1)) ? "" : cursor.getString(cursor.getColumnIndexOrThrow(BaseDeDatos.Datostabla.Columna_Parcial1)));
            etParcial2.setText(cursor.isNull(cursor.getColumnIndexOrThrow(BaseDeDatos.Datostabla.Columna_Parcial2)) ? "" : cursor.getString(cursor.getColumnIndexOrThrow(BaseDeDatos.Datostabla.Columna_Parcial2)));
            etParcial3.setText(cursor.isNull(cursor.getColumnIndexOrThrow(BaseDeDatos.Datostabla.Columna_Parcial3)) ? "" : cursor.getString(cursor.getColumnIndexOrThrow(BaseDeDatos.Datostabla.Columna_Parcial3)));
            // Y calcula el promedio con el metodo..
            calcularPromedio();
        } else {
            // Mesaje si no se encuentra el alumno
            Toast.makeText(this, "Alumno no encontrado", Toast.LENGTH_LONG).show();
        }
        // Cierra el cursor para liberar los recursos asociados.
        cursor.close();
    }

    // Metodo para insertar el alumno
    private void insertarAlumno() {
        String matricula = etMatricula.getText().toString();
        String nombre = etNombre.getText().toString();
        String materia = etMateria.getText().toString();
        String carrera = etCarrera.getText().toString();

        // Si la matricula y los demas edittext estan vacios, imprime un mensaje
        if (matricula.isEmpty() || nombre.isEmpty() || materia.isEmpty() || carrera.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_LONG).show();
            return;
        }
        // Si no...
        SQLiteDatabase db = ayudaDB.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(BaseDeDatos.Datostabla.Columna_Matr, matricula);
        valores.put(BaseDeDatos.Datostabla.Columna_Nombre, nombre);
        valores.put(BaseDeDatos.Datostabla.Columna_Materia, materia);
        valores.put(BaseDeDatos.Datostabla.Columna_Carrera, carrera);

        // Variable que guarda un "identificador", si es -1...
        long newRowId = db.insert(BaseDeDatos.Datostabla.Nombre_Table, null, valores);
        if (newRowId == -1) {
            Toast.makeText(this, "Error al guardar los datos", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Alumno registrado exitosamente", Toast.LENGTH_LONG).show();
        }
    }

    // Metodo para registrar la claificacion
    private void registrarCalificacion() {

        String matricula = etMatricula.getText().toString();
        // Si la matricula esta vacia (Es necesario con la matricula)
        if (matricula.isEmpty()) {
            Toast.makeText(this, "Ingrese una matrícula", Toast.LENGTH_LONG).show();
            return;
        }
        // Si no...
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
            // Los edittext se guardan en las variables
            String parcial1 = etParcial1.getText().toString();
            String parcial2 = etParcial2.getText().toString();
            String parcial3 = etParcial3.getText().toString();

            // parcial3 es diferente de vacio
            if (!parcial3.isEmpty()) {
                // y si es nulo, la columna del parcial 2 ("Busca en la base de datos")
                if (cursor.isNull(cursor.getColumnIndexOrThrow(BaseDeDatos.Datostabla.Columna_Parcial2))) {
                    Toast.makeText(this, "Debe registrar el segundo parcial antes de registrar el tercero", Toast.LENGTH_LONG).show();
                    cursor.close();
                    return;
                }
                // Si no...
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

            // Esta línea usa el operador ternario (? :) para verificar si la cadena parcial1 está
            // vacía y, en función de eso, asigna un valor a p1, al comenzar es 0.
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
