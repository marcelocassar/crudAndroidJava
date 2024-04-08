package com.exemple.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.exemple.myapplication.R;

public class AlterarActivity extends AppCompatActivity {

    private SQLiteDatabase bancoDados;
    private Button buttonAlterar;
    private EditText editTextNome, editTextSobreNome, editTextNumberIdade, editTextNacionalidade, editTextDateNascimento;
    private Integer id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar);

        buttonAlterar = findViewById(R.id.buttonCadastrar);
        editTextNome = findViewById(R.id.editTextNome);
        editTextSobreNome = findViewById(R.id.editTextSobreNome);
        editTextNumberIdade = findViewById(R.id.editTextNumberIdade);
        editTextNacionalidade = findViewById(R.id.editTextNacionalidade);
        editTextDateNascimento = findViewById(R.id.editTextDateNascimento);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);

        carregarDados();

        buttonAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alterar();
            }
        });
    }

    public void carregarDados() {
        try {
            bancoDados = openOrCreateDatabase("crudapp2", MODE_PRIVATE, null);
            Cursor cursor = bancoDados.rawQuery("SELECT * FROM banco2 WHERE id = ?", new String[]{String.valueOf(id)});
            if (cursor.moveToFirst()) {
                editTextNome.setText(cursor.getString(cursor.getColumnIndex("nome")));
                editTextSobreNome.setText(cursor.getString(cursor.getColumnIndex("sobrenome")));
                editTextNumberIdade.setText(cursor.getString(cursor.getColumnIndex("idade")));
                editTextNacionalidade.setText(cursor.getString(cursor.getColumnIndex("pais")));
                editTextDateNascimento.setText(cursor.getString(cursor.getColumnIndex("data_nascimento")));
            }
            cursor.close();
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void alterar() {
        try {
            bancoDados = openOrCreateDatabase("crudapp2", MODE_PRIVATE, null);
            String sql = "UPDATE banco2 SET nome=?, sobrenome=?, idade=?, pais=?, data_nascimento=? WHERE id=?";
            SQLiteStatement stmt = bancoDados.compileStatement(sql);
            stmt.bindString(1, editTextNome.getText().toString());
            stmt.bindString(2, editTextSobreNome.getText().toString());
            stmt.bindString(3, editTextNumberIdade.getText().toString());
            stmt.bindString(4, editTextNacionalidade.getText().toString());
            stmt.bindString(5, editTextDateNascimento.getText().toString());
            stmt.bindLong(6, id);
            stmt.executeUpdateDelete();
            bancoDados.close();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
