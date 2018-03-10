/**
 * Created by bugad on 6/1/2017.
 */

public class Aura {

    private String auraName;
    private Boolean hasBuff;
    private int trueAtk;
    private int trueDef;
    private int[] effects;


    public  Aura (String name, Boolean hasBuff, String atk, String def, int[] effects){
        this.setAuraName(name);
        String atk1 = atk;
        String def1 = def;
        this.setEffects(effects);

        setTrueAtk(Integer.valueOf(atk));
        setTrueDef(Integer.valueOf(def));
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
}
