package com.mojang.minecraftpe;

import android.net.*;
import android.content.*;

public class Minecraft_Market extends MainActivity
{
    @Override
    public void buyGame() {
        this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=com.mojang.minecraftpe")));
    }
}
