package com.exemple.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CadastroActivity extends AppCompatActivity {
    private EditText editTextNome, editTextSobreNome, editTextIdade, editTextDataNascimento, editTextPais;
    private Button botao;
    private SQLiteDatabase bancoDados;

    // Construtor vazio
    public CadastroActivity() {
    }

    // Getters e Setters
    public EditText getEditTextNome() {
        return editTextNome;
    }

    public void setEditTextNome(EditText editTextNome) {
        this.editTextNome = editTextNome;
    }

    public EditText getEditTextSobreNome() {
        return editTextSobreNome;
    }

    public void setEditTextSobreNome(EditText editTextSobreNome) {
        this.editTextSobreNome = editTextSobreNome;
    }

    public EditText getEditTextIdade() {
        return editTextIdade;
    }

    public void setEditTextIdade(EditText editTextIdade) {
        this.editTextIdade = editTextIdade;
    }

    public EditText getEditTextDataNascimento() {
        return editTextDataNascimento;
    }

    public void setEditTextDataNascimento(EditText editTextDataNascimento) {
        this.editTextDataNascimento = editTextDataNascimento;
    }

    public EditText getEditTextPais() {
        return editTextPais;
    }

    public void setEditTextPais(EditText editTextPais) {
        this.editTextPais = editTextPais;
    }

    public Button getBotao() {
        return botao;
    }

    public void setBotao(Button botao) {
        this.botao = botao;
    }

    public SQLiteDatabase getBancoDados() {
        return bancoDados;
    }

    public void setBancoDados(SQLiteDatabase bancoDados) {
        this.bancoDados = bancoDados;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        // Inicialize os campos de entrada de dados e o botão
        editTextNome = findViewById(R.id.editTextNome);
        editTextSobreNome = findViewById(R.id.editTextSobreNome);
        editTextIdade = findViewById(R.id.editTextIdade);
        editTextDataNascimento = findViewById(R.id.editTextDataNascimento);
        editTextPais = findViewById(R.id.editTextPais);
        botao = findViewById(R.id.buttonCadastrar);

        // Defina o OnClickListener para o botão de Cadastrar
        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastrar();
                //limparTabela();
            }
        });
    }

    // Método para cadastrar os dados
    public void cadastrar(){
        //if(!TextUtils.isEmpty(editTextNome.getText().toString())){
        try{
            // Exibir os dados cadastrados
            String nome = editTextNome.getText().toString();
            String sobrenome = editTextSobreNome.getText().toString();
            String idade = editTextIdade.getText().toString();
            String dataNascimento = editTextDataNascimento.getText().toString();
            String pais = editTextPais.getText().toString();
            System.out.println("Dados cadastrados:");
            System.out.println("Nome: " + nome);
            System.out.println("Sobrenome: " + sobrenome);
            System.out.println("Idade: " + idade);
            System.out.println("Data de Nascimento: " + dataNascimento);
            System.out.println("País: " + pais);

            bancoDados = openOrCreateDatabase("crudapp2", MODE_PRIVATE, null);
            String sql = "INSERT INTO banco2 (nome, sobrenome, idade, data_nascimento, pais) VALUES (?, ?, ?, ?, ?)";
            SQLiteStatement stmt = bancoDados.compileStatement(sql);
            stmt.bindString(1, editTextNome.getText().toString());
            stmt.bindString(2, editTextSobreNome.getText().toString());
            stmt.bindString(3, editTextIdade.getText().toString());
            stmt.bindString(4, editTextDataNascimento.getText().toString());
            stmt.bindString(5, editTextPais.getText().toString());
            stmt.executeInsert();

            bancoDados.close();
            finish();
        } catch (Exception e){
            e.printStackTrace();
        }
        //}
    }

    public void limparTabela() {
        try {
            bancoDados = openOrCreateDatabase("crudapp2", MODE_PRIVATE, null);
            String sql = "DROP TABLE IF EXISTS banco2";
            bancoDados.execSQL(sql);
            bancoDados.close();
            System.out.println("Todos os dados foram removidos da tabela.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
