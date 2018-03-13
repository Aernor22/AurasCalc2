package com.prova.bugad.aurascalc2;

import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by God on 6/29/2017.
 */

public class CustomAdapterRecycler extends RecyclerView.Adapter<CustomAdapterRecycler.ViewHolder> {


    private int buffPower;
    private int basePower;
    private int buffDefense;
    private int baseDefense;

    private int generalQT;
    private int etherealQT;
    private int totemQT;
    private int lifelinkQT;

    private CheckBox[] currentEffects;
    private CheckBox lifelink;
    private ArrayList<AuraQuantity> activeAuras;

    private ArrayList<Aura> auras;
    private TextView powerDefense;
    private TextView totemArmor;
    private TextView fakeLife;
    private TextView totalGain;
    private int creatureType;

    public CustomAdapterRecycler(TextView[] auxEffects, ArrayList<Aura> auras,int creatureType,TextView powerDefense,int buffPower,int buffDefense, CheckBox[] effects,CheckBox lifelink){
        this.auras = auras;
        this.creatureType = creatureType;

        this.powerDefense = powerDefense;
        this.buffPower = buffPower;
        this.buffDefense=buffDefense;

        this.totemArmor = auxEffects[0];
        this.fakeLife = auxEffects[1];
        this.totalGain = auxEffects[2];

        this.lifelink = lifelink;
        this.currentEffects = effects;

        generalQT=0;
        etherealQT=0;
        totemQT = 0;
        lifelinkQT=0;

        this.activeAuras = new ArrayList<>();
        setHasStableIds(true);
        switchAPower(creatureType);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void switchAPower(int creatureType){
        switch (creatureType){
            case 2:
                this.basePower = 0;
                this.baseDefense = 2;
                break;
            default:
                this.basePower = 1;
                this.baseDefense = 1;
        }
    }

    public void activeAurasBuff(ArrayList activeAuras,int creatureType, Boolean hasLifeLink){
        ArrayList<AuraQuantity> newqt = activeAuras;
        buffPower =0;
        buffDefense=0;
        generalQT=0;

        for (AuraQuantity aq:newqt) {
            generalQT+=aq.getQuantidade();
        }
        for (AuraQuantity aq:newqt) {
            if(aq.aura.getHasBuff()){
                if(aq.aura.getHasEtheral()){
                    buffPower+=((aq.aura.getTrueAtk()*generalQT)*aq.getQuantidade());
                    buffDefense+=((aq.aura.getTrueDef()*generalQT)*aq.getQuantidade());
                }else{
                    buffPower+=(aq.aura.getTrueAtk()*aq.getQuantidade());
                    buffDefense+=(aq.aura.getTrueDef()*aq.getQuantidade());
                }
            }
        }
        int atk = printPower(creatureType);
        printLifeGain(hasLifeLink,atk,totalGain);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.auras,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d("TAGE", "Element " + position + " set."+ auras.get(position).getAuraName());

        final Aura aura = auras.get(position);
        holder.aura=aura;
        holder.currentEffects=currentEffects;
        holder.lblAuraName.setText(aura.getAuraName());
        holder.auraList=auras;
        holder.activeAuras= activeAuras;
        holder.lblPowerDefense = this.powerDefense;
        holder.totemArmor = this.totemArmor;
        holder.fakeLife = this.fakeLife;
        holder.lifelink = this.lifelink;
        holder.creatureType = this.creatureType;
        final TextView lblAuraQt = holder.lblAuraQt;



        holder.btnAuraAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = findIndex(aura);
                if(index==-1){
                    activeAuras.add(new AuraQuantity(aura));
                    index = findIndex(aura);
                }

                activeAuras.get(index).increaseQT();


                if(aura.getHasEtheral()){etherealQT++;}
                if(aura.getHasUmbra()){totemQT++;}
                if(aura.getFakeLife()){lifelinkQT++;}

                lblAuraQt.setText(String.valueOf(activeAuras.get(index).getQuantidade()));

                Boolean hasLifeLink =effectsAndLifelink(currentEffects);
                activeAurasBuff(activeAuras, creatureType, hasLifeLink);
            }
        });

        holder.btnAuraSub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = findIndex(aura);
                    Boolean hasLifeLink;
                    if(index!=-1){
                        if(aura.getFakeLife()){lifelinkQT--; if(lifelinkQT<0){lifelinkQT=0;}}
                        if(aura.getHasEtheral() && etherealQT>0){etherealQT--;}
                        if(aura.getHasUmbra()){
                            totemQT--;
                            if(totemQT<0){totemQT=0;}
                        }
                        if(activeAuras.get(index).getQuantidade()>0){
                            activeAuras.get(index).decreaseQT();
                            lblAuraQt.setText(String.valueOf(activeAuras.get(index).getQuantidade()));
                            if(activeAuras.get(index).getQuantidade()==0){
                                activeAuras.remove(index);
                                lblAuraQt.setText(String.valueOf(0));
                            }
                        }
                    }
                    hasLifeLink=effectsAndLifelink(currentEffects);
                    activeAurasBuff(activeAuras,creatureType,hasLifeLink);
                }
            });

    }

    private void printLifeGain(Boolean lifeLink, int power,TextView totalGain){
        Log.d("LifeLinkQT",String.valueOf(lifelinkQT));
        fakeLife.setText(String.valueOf(lifelinkQT));

        int lifegain;
        if(lifeLink){lifegain = (lifelinkQT * power) + power;}else{lifegain = lifelinkQT * power;}
        totalGain.setText(String.valueOf(lifegain));
    }

    @Override
    public int getItemCount() {
        return auras.size();
    }

    public void updateBasepower(int creatureType){
        this.creatureType=creatureType;
        switchAPower(creatureType);
        printPower(creatureType);
    }
    public int printPower(int creatureType){
        totemArmor.setText(String.valueOf(totemQT));

        int power;
        int defense;

        if (creatureType<=1){
            power=(this.basePower+this.buffPower);
            defense=(this.baseDefense+this.buffDefense);
        }else{
            power = (this.basePower+this.buffPower+(2*generalQT));
            defense= (this.baseDefense+this.buffDefense+(2*generalQT));
        }

        this.powerDefense.setText(power+"/"+defense);

        Log.d("BITCH",power+"/"+defense);
        return power;
    }
    public Boolean effectsAndLifelink(CheckBox [] currentEffects){
        if(activeAuras.isEmpty()){
            generalQT = 0;
            etherealQT=0;
            lifelinkQT=0;
            totemQT=0;
            switchAPower(creatureType);
        }
        for (CheckBox ck:currentEffects) {
            ck.setChecked(false);
        }
        Boolean any = false;
        for (AuraQuantity a:activeAuras) {
            int pos = 0;
            if(a.aura.getTrueLife()){any=true;}
            for (int i:a.aura.getEffects()) {
                Log.d("effects:",String.valueOf(i));
                if (i == 1) {
                    currentEffects[pos].setChecked(true);
                }
                pos++;
            }
        }
        lifelink.setChecked(any);
        return any;
    }

    public int findIndex(Aura currentAura){
        if(activeAuras.size()!=0){
            int index=-1;
            for (AuraQuantity a:activeAuras) {
                if(a.aura.getAuraName().equals(currentAura.getAuraName())){
                    index= activeAuras.indexOf(a);
                }
            }
            return index;
        }else{
            return -1;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        TextView lblAuraName;
        TextView lblAuraQt;
        TextView lblPowerDefense;

        TextView totemArmor;
        TextView fakeLife;

        LinearLayout llAura;

        ArrayList<AuraQuantity> activeAuras;
        CheckBox[] currentEffects;
        CheckBox lifelink;
        Aura aura;
        ArrayList<Aura> auraList;

        Button btnAuraSub;

        Button btnAuraAdd;

        int creatureType;

        public ViewHolder(View itemView) {
            super(itemView);
            lblAuraName = (TextView) itemView.findViewById(R.id.lblAuraNameSpinner);
            btnAuraAdd = (Button)itemView.findViewById(R.id.btnAuraAdd);
            btnAuraSub = (Button)itemView.findViewById(R.id.btnAuraSub);
            lblAuraQt = (TextView)itemView.findViewById(R.id.lblAuraQT);
            llAura = (LinearLayout)itemView.findViewById(R.id.llNames);
            itemView.setOnCreateContextMenuListener(this);

        }

        public int getItem() {return auraList.indexOf(aura);}

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v,
                                        ContextMenu.ContextMenuInfo menuInfo) {

            PopupMenu pm = new PopupMenu(v.getContext(),v);
            MenuInflater inflater = pm.getMenuInflater();
            inflater.inflate(R.menu.context_menu, menu);

        }
    }
}
