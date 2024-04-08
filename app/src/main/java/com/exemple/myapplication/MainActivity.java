package com.exemple.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase bancoDados;
    private ListView listViewDados;
    private Button botao;
    private ArrayList<Integer> arrayIds;
    private Integer idSelecionado;

    // Construtor vazio
    public MainActivity() {
    }

    // Getters e Setters
    public SQLiteDatabase getBancoDados() {
        return bancoDados;
    }

    public void setBancoDados(SQLiteDatabase bancoDados) {
        this.bancoDados = bancoDados;
    }

    public ListView getListViewDados() {
        return listViewDados;
    }

    public void setListViewDados(ListView listViewDados) {
        this.listViewDados = listViewDados;
    }

    public Button getBotao() {
        return botao;
    }

    public void setBotao(Button botao) {
        this.botao = botao;
    }

    public ArrayList<Integer> getArrayIds() {
        return arrayIds;
    }

    public void setArrayIds(ArrayList<Integer> arrayIds) {
        this.arrayIds = arrayIds;
    }

    public Integer getIdSelecionado() {
        return idSelecionado;
    }

    public void setIdSelecionado(Integer idSelecionado) {
        this.idSelecionado = idSelecionado;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        listViewDados = findViewById(R.id.listViewDados);
        botao = findViewById(R.id.buttonCadastrar);
        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTelaCadastro();
            }
        });

        listViewDados.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                idSelecionado = arrayIds.get(position);
                confirmaExcluir();
                return true;
            }
        });

        listViewDados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                idSelecionado = arrayIds.get(position);
                abrirTelaAlterar();
            }
        });

        criarBancoDados();
        listarDados();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        listarDados();
    }

    public void criarBancoDados() {
        try {
            bancoDados = openOrCreateDatabase("crudapp2", MODE_PRIVATE, null);
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS banco2("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nome TEXT, sobrenome TEXT, " +
                    "idade INTEGER, " +
                    "data_nascimento DATE, " +
                    "pais TEXT)");
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void apagarBancoDados() {
        try {
            bancoDados = openOrCreateDatabase("crudapp2", MODE_PRIVATE, null);
            String sql = "DROP TABLE IF EXISTS banco2";
            bancoDados.execSQL(sql);
            bancoDados.close();
            recreate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listarDados() {
        try {
            arrayIds = new ArrayList<>();
            bancoDados = openOrCreateDatabase("crudapp2", MODE_PRIVATE, null);
            Cursor meuCursor = bancoDados.rawQuery("SELECT id, nome FROM banco2", null);
            ArrayList<String> linhas = new ArrayList<String>();
            if (meuCursor != null && meuCursor.moveToFirst()) {
                do {
                    linhas.add("ID: " + meuCursor.getInt(0) + ", Nome: " + meuCursor.getString(1));
                    arrayIds.add(meuCursor.getInt(0));
                } while (meuCursor.moveToNext());
            }
            ArrayAdapter meuAdapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1,
                    linhas
            );
            listViewDados.setAdapter(meuAdapter);
            meuCursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void abrirTelaCadastro() {
        try {
            Intent intent = new Intent(this, CadastroActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void excluir() {
        try {
            bancoDados = openOrCreateDatabase("crudapp2", MODE_PRIVATE, null);
            String sql = "DELETE FROM banco2 WHERE id=?";
            SQLiteStatement stmt = bancoDados.compileStatement(sql);
            stmt.bindLong(1, idSelecionado);
            stmt.executeUpdateDelete();
            listarDados();
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void confirmaExcluir() {
        AlertDialog.Builder msgBox = new AlertDialog.Builder(MainActivity.this);
        msgBox.setTitle("Excluir");
        msgBox.setIcon(android.R.drawable.ic_menu_delete);
        msgBox.setMessage("Realmente deseja excluir o registro?");
        msgBox.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                excluir();
            }
        });
        msgBox.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        msgBox.show();
    }

    public void abrirTelaAlterar() {
        Intent intent = new Intent(this, AlterarActivity.class);
        intent.putExtra("id", idSelecionado);
        startActivity(intent);
    }
}
