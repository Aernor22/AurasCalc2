package com.prova.bugad.aurascalc2;

import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by bugad on 6/1/2017.
 */

public class Converter {
    private ArrayList<Aura> auraArray;
    private ArrayList<AuraQuantity> auraQuantities;
    private String[] auraNames;
    public Converter(Cursor cursor, String Statement) {
        auraNames = new String[cursor.getCount()];
        setAuraArray(new ArrayList<Aura>());
        setAuraQuantities(new ArrayList<AuraQuantity>());
        if(cursor.moveToNext()) {
            int k=0;
            do {
                Log.d("Nome", cursor.getString(1));
                String[] items = cursor.getString(5).replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", "").split(",");

                int[] results = new int[items.length];

                for (int i = 0; i < items.length; i++) {
                    try {
                        results[i] = Integer.parseInt(items[i]);
                    } catch (NumberFormatException nfe) {
                        //NOTE: write something here if you need to recover from formatting errors
                    }
                }

                Log.d("RESULTADOS",String.valueOf(results.length));

                Boolean hasUmbra = false ,hasEtheral = false, truelife = false, fakelife = false;
                if(Boolean.valueOf(cursor.getString(6))!=null){truelife= Boolean.valueOf(cursor.getString(6));}
                if(Boolean.valueOf(cursor.getString(7))!=null){fakelife= Boolean.valueOf(cursor.getString(7));}
                if(Boolean.valueOf(cursor.getString(8))!=null){hasUmbra= Boolean.valueOf(cursor.getString(8));}
                if(Boolean.valueOf(cursor.getString(9))!=null){hasEtheral= Boolean.valueOf(cursor.getString(9));}

                Aura aura = new Aura(cursor.getString(1), Boolean.valueOf(cursor.getString(2)), cursor.getString(3), cursor.getString(4), results,truelife,fakelife,hasUmbra,hasEtheral);
                getAuraNames()[k]=cursor.getString(1);
                getAuraArray().add(k,aura);
                getAuraQuantities().add(k,new AuraQuantity(aura));
                Log.d("SIze",String.valueOf(getAuraArray().size()));
                k++;
            } while (cursor.moveToNext());
        }
    }



    public  ArrayList<Aura> getAuraArray() {
        return auraArray;
    }

    public void setAuraArray(ArrayList<Aura> auraArray) {
        this.auraArray = auraArray;
    }

    public String[] getAuraNames() {
        return auraNames;
    }

    public ArrayList<AuraQuantity> getAuraQuantities() {
        return auraQuantities;
    }

    public void setAuraQuantities(ArrayList<AuraQuantity> auraQuantities) {
        this.auraQuantities = auraQuantities;
    }
}
