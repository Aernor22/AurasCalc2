package com.prova.bugad.aurascalc2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ViewAuras extends AppCompatActivity {
    ListView lvDados;
    SQLiteDatabase db;
    String[] from;
    int[] to;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_auras);

        db = openOrCreateDatabase("Auras.db", Context.MODE_PRIVATE,null);

        to = new int[]{R.id.tvId, R.id.tvNome};
        from = new String[]{"_id", "nome"};


        try
        {
            lvDados =(ListView)findViewById(R.id.lvDados);
            lvDados.setAdapter(updateCursor());
            lvDados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    View v = lvDados.getChildAt(i);
                    TextView vId = (TextView)v.findViewById(R.id.tvId);
                    Intent j = new Intent(getApplicationContext(),EditAuras.class);
                    j.putExtra("id",vId.getText().toString());
                    startActivityForResult(j,1);

                }
            });

        }
        catch(Exception ex) {
            Toast.makeText(getBaseContext()," Erro = "+ ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        registerForContextMenu(lvDados);
        Button btnVoltar = (Button)findViewById(R.id.btnVoltarAdd);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public SimpleCursorAdapter updateCursor(){
        String statement = "SELECT * FROM auras";
        Cursor dados = db.rawQuery(statement,null);
        SimpleCursorAdapter apt = new SimpleCursorAdapter(getBaseContext(), R.layout.dados, dados, from, to,0);
        return apt;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.editAura:
                Intent i = new Intent(this,EditAuras.class);
                i.putExtra("id",String.valueOf(info.id));
                startActivityForResult(i,1);

                return true;
            case R.id.deleteAura:
                String id = String.valueOf(info.id);
                String statement = ("DELETE FROM auras WHERE _id = "+id);
                try{
                    db.execSQL(statement);
                    lvDados.setAdapter(updateCursor());
                    Toast.makeText(getApplicationContext(), "DONE", Toast.LENGTH_SHORT).show();
                }catch (Exception ex){

                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                lvDados.setAdapter(updateCursor());
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }
}
