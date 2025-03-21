package com.example.examenpracticochavezap1;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText etMatricula, etNombre, etCarrera, etCal1, etCal2;
    private Button btnAlta, btnConsulta, btnCalcular;
    private TextView tvPromedio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etMatricula = findViewById(R.id.etMatricula);
        etNombre = findViewById(R.id.etNombre);
        etCarrera = findViewById(R.id.etCarrera);
        etCal1 = findViewById(R.id.etCal1);
        etCal2 = findViewById(R.id.etCal2);
        tvPromedio = findViewById(R.id.tvPromedio);

        btnCalcular = findViewById(R.id.btnCalcular);
        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcularPromedioFinal();
            }
        });
    }
    public void altaAlumnos(View view){
        //Generea un objeto admin que pasa por el com¿nstructor y es donde se trabaja la base de datos, factory queda null porque automaticamente
        //ya se hacen las operaciones de sql
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        //queremos que la bd sea rescribible
        SQLiteDatabase bd = admin.getWritableDatabase();//objetos de base de datos se reescribible

        //para guardar valor de variables del formulario
        //toammos las variables del formulario y las guradmos en otras varibles
        String matricula = etMatricula.getText().toString();
        String nombre = etNombre.getText().toString();
        String carrera = etCarrera.getText().toString();
        Double calificacion1 = Double.valueOf(etCal1.getText().toString());
        Double calificacion2 = Double.valueOf(etCal2.getText().toString());


        //se crea contenedor para almacenar los valores
        ContentValues registro = new ContentValues();
        //se integran variables de java con valores y campos de la tabla empleado
        registro.put("matricula", matricula);
        registro.put("nombre", nombre);
        registro.put("carrera", carrera);
        registro.put("calificacion1", calificacion1);
        registro.put("calificacion2", calificacion2);
        //se inserta registro en tabla alumnos
        //objetos orientdas a aspectos para insertarel valor
        bd.insert("alumnos", null, registro);
        // Se cierra BD
        bd.close();
        //Se limpian los campos de texto
        etMatricula.setText(null);
        etNombre.setText(null);
        etCarrera.setText(null);
        etCal1.setText(null);
        etCal2.setText(null);

        //Imprimir datos de registro exitoso en ventana emergente tipo TOAST
        Toast.makeText(this, "Exito al ingresar el registro\n\nMatricula:"+matricula+"\nNombre:"+nombre+"\nCarrera:"+carrera+"Calificacion 1:"+calificacion1+"Calificacion 2"+calificacion2,Toast.LENGTH_LONG).show();
        //Limpia cajas de texto
        this.etMatricula.setText("");
        this.etNombre.setText("");
        this.etCarrera.setText("");
        this.etCal1.setText("");
        this.etCal2.setText("");
    }
    //metodo para consultar
    public void consultaAlumnos(View view){
        //vamos a la ubicacion de la base de datos
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        //reescribimos la base de datos para consultar
        SQLiteDatabase bd = admin.getWritableDatabase();//objetos de base de datos se reescribible

        //se asigna una variable para busqueda y consulta por campo distintivo
        String matriculaConsulta = etMatricula.getText().toString();
        //Cursor recorre los campos d euna tabla hasta encontralo por campo distintivo
        //cursor busqeda a detalle, se maneja a nivel PLSQL
        //ultimo campo nulo para que no haga otra busqueda
        Cursor fila = bd.rawQuery("SELECT nombre,carrera,calificacion1,calificacion2 from alumnos where matricula="+matriculaConsulta,null);

        if(fila.moveToFirst()){//si condicion es verdadera, es decir, encontro un campo y sus datos
            etNombre.setText(fila.getString(0));
            etCarrera.setText(fila.getString(1));
            etCal1.setText(fila.getString(2));
            etCal2.setText(fila.getString(3));
            Toast.makeText(this,"Registro encontrado de forma EXITOSA",Toast.LENGTH_LONG).show();
        }else{//condicion falsa si no encontro un registro
            Toast.makeText(this,"No existe el alumno con esa matricula\nVerifica",Toast.LENGTH_LONG).show();
            bd.close();
        }
    }
    public void calcularPromedioFinal(){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        //reescribimos la base de datos para consultar
        SQLiteDatabase bd = admin.getWritableDatabase();//objetos de base de datos se reescribible
        Double calificacion1 = Double.valueOf(etCal1.getText().toString());
        Double calificacion2 = Double.valueOf(etCal2.getText().toString());
        Double promedioFinal;
        ContentValues registro = new ContentValues();
            promedioFinal = (calificacion1 + calificacion2) / 2;

            Toast.makeText(this, "Promedio final: $" + promedioFinal, Toast.LENGTH_LONG).show();

            tvPromedio.setText("Promedio: " + promedioFinal);
        registro.put("Promedio", promedioFinal);
        bd.insert("alumnos", null, registro);
        // Se cierra BD
        bd.close();


    }
}