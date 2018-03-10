package com.prova.bugad.aurascalc2;

public class AuraQuantity {
    Aura aura;
    int quantidade;

    public AuraQuantity(Aura aura){
        this.aura = aura;
        this.quantidade = 0;
    }

    public void increaseQT(){ if(this.quantidade<4){this.quantidade++;}}
    public void decreaseQT(){ if(this.quantidade>0){this.quantidade--;}}

    public int getQuantidade(){return this.quantidade;}
}
