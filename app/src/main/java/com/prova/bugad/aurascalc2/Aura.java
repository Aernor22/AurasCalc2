package com.prova.bugad.aurascalc2;

/**
 * Created by bugad on 6/1/2017.
 */

public class Aura {

    private String auraName;
    private Boolean hasBuff;

    private Boolean hasEtheral;
    private Boolean hasUmbra;

    private int trueAtk;
    private int trueDef;
    private int[] effects;

    private Boolean trueLife = false; //lifelink as it is
    private Boolean fakeLife = false; //when you gain life based on attack, but there is no lifelink keyword


    public  Aura (String name, Boolean hasBuff, String atk, String def, int[] effects,Boolean trueLife, Boolean fakeLife, Boolean hasUmbra, Boolean hasEtheral){
        this.setAuraName(name);
        this.hasBuff = hasBuff;
        this.setHasUmbra(hasUmbra);
        this.setHasEtheral(hasEtheral);

        setTrueAtk(Integer.valueOf(atk));
        setTrueDef(Integer.valueOf(def));

        this.setTrueLife(trueLife);
        this.setFakeLife(fakeLife);

        this.setEffects(effects);
    }

    public String getAuraName() {
        return auraName;
    }

    public void setAuraName(String auraName) {
        this.auraName = auraName;
    }

    public Boolean getHasBuff() {
        return hasBuff;
    }

    public void setHasBuff(Boolean hasBuff) {
        this.hasBuff = hasBuff;
    }

    public int getTrueAtk() {
        return trueAtk;
    }

    public void setTrueAtk(int trueAtk) {
        this.trueAtk = trueAtk;
    }

    public int getTrueDef() {
        return trueDef;
    }

    public void setTrueDef(int trueDef) {
        this.trueDef = trueDef;
    }

    public int[] getEffects() {
        return effects;
    }

    public int getEffects(int position) {
        return effects[position];
    }

    public void setEffects(int[] effects) {
        this.effects = effects;
    }

    public void setEffects(int effect,int position) {
        this.effects[position] = effect;
    }

    public Boolean getHasEtheral() {
        return hasEtheral;
    }

    public void setHasEtheral(Boolean hasEtheral) {
        this.hasEtheral = hasEtheral;
    }

    public Boolean getHasUmbra() {
        return hasUmbra;
    }

    public void setHasUmbra(Boolean hasUmbra) {
        this.hasUmbra = hasUmbra;
    }

    public Boolean getFakeLife() {
        return fakeLife;
    }

    public void setFakeLife(Boolean fakeLife) {
        this.fakeLife = fakeLife;
    }

    public Boolean getTrueLife() {
        return trueLife;
    }

    public void setTrueLife(Boolean trueLife) {
        this.trueLife = trueLife;
    }
}
