package com.prova.bugad.aurascalc2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

public class EditAuras extends AppCompatActivity {
    int[] efeitosNumber =new int[8];
    SQLiteDatabase db;
    StringBuilder statement;

    CheckBox ckUmbra;
    CheckBox ckEtheral;
    CheckBox ckLifeLink;
    CheckBox ckFakeLifeLink;
    String  atk;
    String  defense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_auras);

        String auraName = "";
        Boolean hasBuff = false;
        atk = "";
        defense = "";
        String  effectsString = "";
        Boolean hasUmbra = false;
        Boolean hasEtheral = false;
        Boolean trueLifeLink = false;
        Boolean fakeLifeLink = false;

        ckUmbra = (CheckBox)findViewById(R.id.ckUmbraE);
        ckEtheral = (CheckBox)findViewById(R.id.ckEtheralE);
        ckLifeLink = (CheckBox)findViewById(R.id.ckLifeLinkE);
        ckFakeLifeLink = (CheckBox)findViewById(R.id.ckLifeLinkFakeE);

        db = openOrCreateDatabase("Auras.db", Context.MODE_PRIVATE,null);

        statement = new StringBuilder();

        statement.append("SELECT * FROM auras WHERE _id ="+ getIntent().getStringExtra("id"));

        Cursor c  = db.rawQuery(statement.toString(),null);
        if(c.moveToNext()){
            do{
                auraName = c.getString(c.getColumnIndex("nome"));
                hasBuff = Boolean.valueOf(c.getString(c.getColumnIndex("buff")));
                Log.d("uhu",c.getString(2));
                atk = c.getString(c.getColumnIndex("atk"));
                defense = c.getString(c.getColumnIndex("df"));
                Log.d("uhu",c.getString(3) + c.getString(4));
                effectsString = c.getString(c.getColumnIndex("type"));
                trueLifeLink = Boolean.valueOf(c.getString(c.getColumnIndex("truelife")));
                fakeLifeLink = Boolean.valueOf(c.getString(c.getColumnIndex("fakelife")));
                hasUmbra = Boolean.valueOf(c.getString(c.getColumnIndex("umbra")));
                hasEtheral = Boolean.valueOf(c.getString(c.getColumnIndex("ethereal")));
            }while (c.moveToNext());
        }

        String[] items = effectsString.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", "").split(",");

        int[] results = new int[items.length];

        for (int i = 0; i < items.length; i++) {
            try {
                results[i] = Integer.parseInt(items[i]);
            } catch (NumberFormatException nfe) {
                //NOTE: write something here if you need to recover from formatting errors
            }
        }

        final EditText edtName = (EditText)findViewById(R.id.edtAuraNameE);
        edtName.setText(auraName);

        final EditText edtAtk = (EditText)findViewById(R.id.edtBuffAttackE);
        final EditText edtDefense = (EditText)findViewById(R.id.edtBuffDefenseE);
        final CheckBox ckBuff = (CheckBox)findViewById(R.id.ckBonusE);

        ckBuff.setChecked(hasBuff);
        if(hasBuff){
            edtAtk.setText(atk);
            edtDefense.setText(defense);
        }

        checkBuff(hasBuff,edtAtk,edtDefense);



        ckBuff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkBuff(ckBuff.isChecked(),edtAtk,edtDefense);            }
        });



        final CheckBox ckUmbra = (CheckBox)findViewById(R.id.ckUmbraE);
        ckUmbra.setChecked(hasUmbra);


        final CheckBox ckEtheral = (CheckBox)findViewById(R.id.ckEtheralE);
        ckEtheral.setChecked(hasEtheral);

        efeitosNumber = results;

        final CheckBox[] effects = new CheckBox[8];
        effects[0] = (CheckBox)findViewById(R.id.ckFlyingE);
        effects[1] = (CheckBox)findViewById(R.id.ckFirstStrikeE);
        effects[2] = (CheckBox)findViewById(R.id.ckTrampleE);
        effects[3] = (CheckBox)findViewById(R.id.ckVigilanceE);
        effects[4] = (CheckBox)findViewById(R.id.ckReachE);
        effects[5] = (CheckBox)findViewById(R.id.ckProtectE);
        effects[6] = (CheckBox)findViewById(R.id.ckFichaE);
        effects[7] = (CheckBox)findViewById(R.id.ckHitE);

        int j=0;
        for (int i:efeitosNumber) {
            if(i==0){
                effects[j].setChecked(false);
            }else{
                effects[j].setChecked(true);
            }
            j++;
        }

        ckLifeLink.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ckFakeLifeLink.setEnabled(!ckLifeLink.isChecked());
                if(ckLifeLink.isChecked()){
                    ckFakeLifeLink.setChecked(false);
                }
            }
        });
        ckFakeLifeLink.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ckLifeLink.setEnabled(!ckFakeLifeLink.isChecked());
                if(ckFakeLifeLink.isChecked()){
                    ckLifeLink.setChecked(false);
                }
            }
        });

        ckLifeLink.setChecked(trueLifeLink);
        ckFakeLifeLink.setChecked(fakeLifeLink);

        effects[6].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                effects[1].setEnabled(!effects[6].isChecked());
                effects[1].setChecked(effects[6].isChecked());
            }
        });



        Button btnEditar = (Button)findViewById(R.id.btnOKNewAuraE);
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statement.setLength(0);
                if(!edtName.getText().toString().isEmpty()){
                    if(!ckBuff.isChecked()&& !checkEffects(effects)){
                        Toast.makeText(getApplicationContext(), "You dumb bitch, the aura has to do something!", Toast.LENGTH_SHORT).show();
                    }else{
                        try{
                            statement.append("UPDATE auras SET nome = '"+ edtName.getText().toString()+ "',");
                            if(ckEtheral.isChecked()&&!ckBuff.isChecked()){
                                throw new Exception("Please insert a number on Atk/Defense.");
                            }
                            if(ckBuff.isChecked()){
                                if(!edtAtk.getText().toString().isEmpty()&&!edtDefense.getText().toString().isEmpty()){
                                    statement.append("buff = 'true',atk ='" +edtAtk.getText().toString()+"',df = '"+edtDefense.getText().toString()+"'");
                                }else{
                                    throw new Exception("Please insert a number on Atk/Defense.");
                                }
                            }else{
                                statement.append("buff = 'false',atk ='0',df ='0'");
                            }
                            statement.append(",type = ");

                            int j = 0;
                            for (CheckBox b:effects
                                    ) {
                                if(b.isChecked()){
                                    efeitosNumber[j] =1;
                                }else{
                                    efeitosNumber[j]=0;
                                }
                                j++;
                            }

                            statement.append( "'"+ Arrays.toString(efeitosNumber)+"',");


                            statement.append("truelife = ");
                            if(ckLifeLink.isChecked()){statement.append("'true',");}else{statement.append("'false',");}

                            statement.append("fakelife = ");
                            if(ckFakeLifeLink.isChecked()){statement.append("'true',");}else{statement.append("'false',");}

                            statement.append("umbra = ");
                            if(ckUmbra.isChecked()){statement.append("'true',");}else{statement.append("'false',");}

                            statement.append("ethereal = ");
                            if(ckEtheral.isChecked()){statement.append("'true'");}else{statement.append("'false'");}

                            statement.append(" WHERE _id = "+ getIntent().getStringExtra("id"));
                            db.execSQL(statement.toString());
                            Toast.makeText(getApplicationContext(), "Editado", Toast.LENGTH_SHORT).show();
                            Log.d("Success",statement.toString());
                            Intent returnResult = new Intent();
                            returnResult.putExtra("result",1);
                            setResult(Activity.RESULT_OK,returnResult);
                            finish();
                        }catch (Exception ex){
                            Toast.makeText(getApplicationContext(),"Falha ao inserir, \n"+ex.getMessage(),Toast.LENGTH_LONG).show();
                            Log.d("ErrorData",ex.getMessage());
                        }
                    }

                }else{
                    Toast.makeText(getApplicationContext(), "Please give the new Aura a name", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void checkEthereal(CheckBox ckEtheral, CheckBox ckBuff, TextView edtAtk, TextView edtDefense){

        ckBuff.setEnabled(!ckEtheral.isChecked()); //true
        ckBuff.setClickable(!ckEtheral.isChecked()); //true

        if(ckEtheral.isChecked()){
            edtAtk.setClickable(false);
            edtAtk.setEnabled(false);
            edtAtk.setText("1");

            edtDefense.setText("1");
            edtDefense.setClickable(false);
            edtDefense.setEnabled(false);
        }else {
            checkBuff(ckBuff.isChecked(),edtAtk,edtDefense);
        }
    }

    public void checkBuff(Boolean b, View edtatk, View edtdefense){
        EditText edtAtk= (EditText)edtatk;
        EditText edtDefense = (EditText)edtdefense;

        edtAtk.setEnabled(b);
        edtAtk.setClickable(b);

        edtDefense.setEnabled(b);
        edtDefense.setClickable(b);

        if(!b){
            edtAtk.setText("");
            edtDefense.setText("");
        }
    }
    private Boolean checkEffects(CheckBox[] effects ){
        Boolean any = false;
        for (CheckBox ck:effects) {
            if (ck.isChecked()){
                any=true;
            }
        }

        if(ckLifeLink.isChecked() || ckFakeLifeLink.isChecked()|| ckUmbra.isChecked()|| ckEtheral.isChecked()){
            any=true;
        }

        return any;
    }

}
