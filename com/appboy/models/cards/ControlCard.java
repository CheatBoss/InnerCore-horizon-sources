package com.appboy.models.cards;

import org.json.*;
import bo.app.*;
import com.appboy.enums.*;

public class ControlCard extends Card
{
    public ControlCard(final JSONObject jsonObject, final CardKey.Provider provider, final br br, final dn dn, final c c) {
        super(jsonObject, provider, br, dn, c);
    }
    
    @Override
    public CardType getCardType() {
        return CardType.CONTROL;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ControlCard{");
        sb.append(super.toString());
        sb.append("}");
        return sb.toString();
    }
}
