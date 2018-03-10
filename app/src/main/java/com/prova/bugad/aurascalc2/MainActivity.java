package com.prova.bugad.aurascalc2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase db;
    StringBuilder statement;
    TextView powerDefense;
    TextView [] aux;
    int basePower;
    int buffPower;
    int buffDefense;
    int baseDefense;
    CheckBox[] lastCkEffects;
    CheckBox[] ckEffects;
    ListView lvAuras;
    Converter converter;
    RadioGroup rbgCreatures;
    CheckBox ckLifelink;

    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buffPower = 0;
        buffDefense = 0;
        aux = new TextView[3];
        aux[0] = (TextView)findViewById(R.id.lblTotemArmor);
        aux[1] = (TextView)findViewById(R.id.lblFakeLifelink);
        aux[2] = (TextView)findViewById(R.id.lblLifeGain);

        ckLifelink = (CheckBox)findViewById(R.id.ckLifeLinkM);

        ckEffects = new CheckBox[8];
        ckEffects [0] = (CheckBox) findViewById(R.id.ckFlyingM);
        ckEffects [1] = (CheckBox) findViewById(R.id.ckFirstStrikeM);
        ckEffects [2] = (CheckBox) findViewById(R.id.ckTrampleM);
        ckEffects [3] = (CheckBox) findViewById(R.id.ckVigilanceM);
        ckEffects [4] = (CheckBox) findViewById(R.id.ckReachM);
        ckEffects [5] = (CheckBox) findViewById(R.id.ckProtectM);
        ckEffects [6] = (CheckBox) findViewById(R.id.ckFichaM);
        ckEffects [7] = (CheckBox) findViewById(R.id.ckHitM);

        ckLifelink.setEnabled(false);
        ckLifelink.setClickable(false);
        for (CheckBox ck:ckEffects) {
            ck.setEnabled(false);
            ck.setClickable(false);
        }


        lastCkEffects = ckEffects;

        statement = new StringBuilder();
        criarBanco();

        powerDefense = (TextView)findViewById(R.id.lblCreaturePower);

        //lvAuras =(ListView)findViewById(R.id.lvAurasPickers);
        //lvAuras.setAdapter(updateAdapter());


        recyclerView = (RecyclerView)findViewById(R.id.lvAurasPickersRecicle);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(updateAdapterRec());
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        RadioButton rbGladecover = (RadioButton)findViewById(R.id.rbGladeBoogles);
        rbGladecover.setChecked(true);

        rbgCreatures = (RadioGroup)findViewById(R.id.rbgCreature);
        rbgCreatures.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switchThatShit(rbgCreatures);
            }
        });
        switchThatShit(rbgCreatures);

        printBasePower();




    }

    public void switchThatShit(RadioGroup rbgCreatures){
        int id = rbgCreatures.getCheckedRadioButtonId();
        switch(id){
            case R.id.rbGladeBoogles:{
                setBasePower(0);
                break;
            }
            case R.id.rbSilhana:{
                setBasePower(1);
                break;
            }
            case R.id.rbKor: {
                setBasePower(2);
                break;
            }
        }
    }

    public CustomAdapterRecycler updateAdapterRec(){
        for (CheckBox ck:ckEffects) {
            ck.setChecked(false);
        }
        ckLifelink.setChecked(false);
        String statement = "SELECT * FROM auras";
        Cursor dados = db.rawQuery(statement,null);
        converter = new Converter(dados,statement);
        return new CustomAdapterRecycler(aux, converter.getAuraArray(),0,powerDefense,buffPower,buffDefense,ckEffects,ckLifelink);
    }

    public void setBasePower(int creatureType){
        Log.d("Creature type main",String.valueOf(creatureType));
        CustomAdapterRecycler ca = (CustomAdapterRecycler) recyclerView.getAdapter();
        ca.updateBasepower(creatureType);
        switch (creatureType){
            case 2:
                this.basePower=0;
                this.baseDefense=2;
                break;
            default:
                this.basePower=1;
                this.baseDefense=1;
        }
    }

    public void printBasePower(){
        powerDefense.setText(String.valueOf(basePower)+"/"+String.valueOf(baseDefense));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_options, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch(item.getItemId()){
            case R.id.addAura:
                i = new Intent(getApplicationContext(),AddAura.class);
                startActivityForResult(i,1); return true;
            case R.id.reset:
                recyclerView.setAdapter(updateAdapterRec());switchThatShit(rbgCreatures); return true;
            case R.id.about:
                i = new Intent(getApplicationContext(), About.class);
                startActivity(i);
                return true;
            case R.id.exit:
                db.close();
                finish();return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                recyclerView.setAdapter(updateAdapterRec());
                //lvAuras.setAdapter(updateAdapter());
                switchThatShit(rbgCreatures);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    private void criarBanco(){
        db = openOrCreateDatabase("Auras.db", Context.MODE_PRIVATE,null);
        statement.setLength(0);
        statement.append("CREATE TABLE IF NOT EXISTS auras (");
        statement.append("_id INTEGER PRIMARY KEY AUTOINCREMENT,");
        statement.append("nome varchar(80),");
        statement.append("buff boolean,");
        statement.append("atk varchar(10),");
        statement.append("df varchar(10),");
        statement.append("type varchar(8),");
        statement.append("truelife boolean,");
        statement.append("fakelife boolean,");
        statement.append("umbra boolean,");
        statement.append("ethereal boolean)");
        try{
            db.execSQL(statement.toString());
        }catch (Exception ex ){
            Toast.makeText(getBaseContext(),"Falha ao criar \n" + ex.getMessage() ,Toast.LENGTH_LONG).show();
        }
    }
}
