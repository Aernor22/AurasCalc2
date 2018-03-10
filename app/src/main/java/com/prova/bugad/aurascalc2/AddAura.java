package com.prova.bugad.aurascalc2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Arrays;

public class AddAura extends AppCompatActivity {
    String auraName;
    StringBuilder statement;
    int[] efeitosNumber =new int[8];
    SQLiteDatabase db;

    CheckBox ckUmbra;
    CheckBox ckEtheral;
    CheckBox ckLifeLink;
    CheckBox ckFakeLifeLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_aura);
        statement = new StringBuilder();

        db = openOrCreateDatabase("Auras.db", Context.MODE_PRIVATE,null);

        for (int i =4; i<4;i++){
            efeitosNumber[i]=0;
        }

        final EditText edtAuraName = (EditText)findViewById(R.id.edtAuraName);

        final CheckBox ckBuff = (CheckBox)findViewById(R.id.ckBonus);
        final EditText edtAtk = (EditText)findViewById(R.id.edtBuffAttack);
        final EditText edtDefense = (EditText)findViewById(R.id.edtBuffDefense);

        CheckBuff(false,edtAtk,edtDefense);

        ckBuff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBuff(ckBuff.isChecked(),edtAtk,edtDefense);
            }
        });

        ckUmbra = (CheckBox)findViewById(R.id.ckUmbra);
        ckEtheral = (CheckBox)findViewById(R.id.ckEtheral);
        ckLifeLink = (CheckBox)findViewById(R.id.ckLifeLink);
        ckFakeLifeLink = (CheckBox)findViewById(R.id.ckLifeLinkFake);

        final CheckBox[] effects = new CheckBox[8];
        effects[0] = (CheckBox)findViewById(R.id.ckFlying);
        effects[1] = (CheckBox)findViewById(R.id.ckFirstStrike);
        effects[2] = (CheckBox)findViewById(R.id.ckTrample);
        effects[3] = (CheckBox)findViewById(R.id.ckVigilance);
        effects[4] = (CheckBox)findViewById(R.id.ckReach);
        effects[5] = (CheckBox)findViewById(R.id.ckProtect);
        effects[6] = (CheckBox)findViewById(R.id.ckFicha);
        effects[7] = (CheckBox)findViewById(R.id.ckHit);



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


        effects[6].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                effects[1].setEnabled(!effects[6].isChecked());
                effects[1].setChecked(effects[6].isChecked());
            }
        });

        Button btnSalvar = (Button)findViewById(R.id.btnOKNewAura);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statement.setLength(0);
                if(!edtAuraName.getText().toString().isEmpty()){
                    if(!ckBuff.isChecked()&& !checkEffects(effects) && !ckEtheral.isChecked()){
                        Toast.makeText(AddAura.this, "You dumb bitch, the aura has to do something!", Toast.LENGTH_SHORT).show();
                    }else{
                        try{
                        statement.append("INSERT INTO auras(nome,buff,atk,df,type,truelife,fakelife,umbra,ethereal) VALUES (");
                        statement.append("'"+ edtAuraName.getText().toString()+"'");
                        statement.append(",");
                        if(ckEtheral.isChecked()&&!ckBuff.isChecked()){
                           throw new Exception("Please insert a number on Atk/Defense.");
                        }
                        if(ckBuff.isChecked()){
                            if(!edtAtk.getText().toString().isEmpty()&&!edtDefense.getText().toString().isEmpty()){
                                statement.append("'true','"+edtAtk.getText().toString()+"','"+edtDefense.getText().toString()+"'");
                            }else{
                                throw new Exception("Please insert a number on Atk/Defense.");
                            }
                        }else{
                            statement.append("'false','0','0'");
                        }
                        statement.append(",");

                        int j = 0;
                        for (CheckBox b:effects) {
                            if(b.isChecked()){
                                efeitosNumber[j] =1;
                            }
                            j++;
                        }

                        statement.append( "'"+Arrays.toString(efeitosNumber)+"',");

                        if(ckLifeLink.isChecked()){statement.append("'true',");}else{statement.append("'false',");}
                        if(ckFakeLifeLink.isChecked()){statement.append("'true',");}else{statement.append("'false',");}

                        if(ckUmbra.isChecked()){statement.append("'true',");}else{statement.append("'false',");}
                        if(ckEtheral.isChecked()){statement.append("'true')");}else{statement.append("'false')");}

                        db.execSQL(statement.toString());
                        Log.d("Statement",statement.toString());
                        Toast.makeText(getApplicationContext(), "Inserido", Toast.LENGTH_SHORT).show();

                            edtAuraName.setText("");
                            ckBuff.setChecked(false);
                            CheckBuff(ckBuff.isChecked(),edtAtk,edtDefense);
                            for (CheckBox c:effects) {
                                c.setChecked(false);
                            }


                            ckLifeLink.setChecked(false);
                            ckFakeLifeLink.setChecked(false);

                            ckEtheral.setChecked(false);
                            ckUmbra.setChecked(false);
                        }catch (Exception ex){
                            Toast.makeText(getApplicationContext(),"Falha ao inserir, \n"+ex.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }

                }else{
                    Toast.makeText(AddAura.this, "Please give the new Aura a name", Toast.LENGTH_SHORT).show();
                }
            }
        });


        Button btnView = (Button)findViewById(R.id.btnView);
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),ViewAuras.class);
                startActivity(i);
            }
        });

    }
    @Override
    public void onBackPressed(){
        Intent returnResult = new Intent();
        returnResult.putExtra("result",1);
        setResult(Activity.RESULT_OK,returnResult);
        finish();
    }



    public void CheckBuff(Boolean b,View atk,View defense){
        EditText edtAtk= (EditText)atk;
        EditText edtDefense = (EditText)defense;

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
