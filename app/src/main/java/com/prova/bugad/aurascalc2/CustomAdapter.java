package com.prova.bugad.aurascalc2;

import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolderer> {

    TextView [] theThree;
    TextView powerDefense;

    int basePower;
    int powerBuff;

    int baseDefense;
    int defenseBuff;

    private ArrayList<Aura> dbAuras;
    CheckBox[] effectsList;
    CheckBox lifelink;

    private ArrayList<Aura> activeAuras;
    int creaturetype;

    public CustomAdapter(TextView [] theThree,int creatureType, ArrayList<Aura> dbAuras, CheckBox[] effects, CheckBox lifelink){
        powerBuff =0; defenseBuff = 0;

        this.theThree = theThree;
        this.dbAuras = dbAuras;
        this.effectsList = effects;
        this.lifelink = lifelink;

        powerDefense.setText(basePower+"/"+baseDefense);

        this.theThree[0].setText(0);
        this.theThree[1].setText(0);
        this.theThree[2].setText(0);

        this.activeAuras = new ArrayList<>();

       this.creaturetype = creatureType;
        updateBaseStats(creatureType);

    }

    private void updateBaseStats(int creaturetype){
        switch (creaturetype){
            case 2: basePower = 0; baseDefense = 2; break;
            default: basePower = 1; baseDefense = 1;
        }
    }

    @Override
    public ViewHolderer onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.auras,parent,false);
        return new ViewHolderer(v);
    }

    @Override
    public void onBindViewHolder(ViewHolderer holder, int position) {
        Aura a = dbAuras.get(position);
        holder.aura=a;
        holder.lblAuraName.setText(a.getAuraName());
        holder.creaturetype = this.creaturetype;
        holder.initListeners();
    }

    @Override
    public int getItemCount() {
        return dbAuras.size();
    }

    public class ViewHolderer extends RecyclerView.ViewHolder{

        int qt = 0;
        TextView lblAuraName;
        TextView lblAuraQt;
        Button btnAuraSub;
        Button btnAuraAdd;
        Aura aura;

        int powerBuff;
        int defenseBuff ;

        int creaturetype;

        public Aura getItem() {return aura;}

        private void addbuff(){
            if(creaturetype<=1){
                powerBuff =+powerBuff;
            }
        }

        public ViewHolderer(View itemView) {
            super(itemView);
            lblAuraName = (TextView) itemView.findViewById(R.id.lblAuraNameSpinner);
            btnAuraAdd = (Button)itemView.findViewById(R.id.btnAuraAdd);
            btnAuraSub = (Button)itemView.findViewById(R.id.btnAuraSub);
            lblAuraQt = (TextView)itemView.findViewById(R.id.lblAuraQT);
        }

        public void initListeners (){

            btnAuraSub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(qt>0){
                        qt--; lblAuraQt.setText(String.valueOf(qt));
                    }else{
                        lblAuraQt.setText("0");
                    }

                }
            });

            btnAuraAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(qt<4){
                        qt++; lblAuraQt.setText(String.valueOf(qt));

                    }
                }
            });
        }

    }


}