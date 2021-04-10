package com.appsflyer.internal;

import java.text.*;
import java.math.*;
import java.util.*;

public final class g
{
    g() {
    }
    
    private static List<BigDecimal> \u0131(final Object o) {
        final ArrayList list = (ArrayList)o;
        final Float n = list.get(0);
        final Float n2 = list.get(1);
        final Float n3 = list.get(2);
        final ArrayList<BigDecimal> list2 = new ArrayList<BigDecimal>();
        list2.add(BigDecimal.valueOf(Double.parseDouble(n.toString())));
        list2.add(BigDecimal.valueOf(Double.parseDouble(n2.toString())));
        list2.add(BigDecimal.valueOf(Double.parseDouble(n3.toString())));
        return list2;
    }
    
    public static Map \u0131(final List<Map<String, Object>> list) {
        final HashMap hashMap = new HashMap<String, HashMap<String, String>>();
        final Iterator<Map<String, Object>> iterator = list.iterator();
        HashMap<String, HashMap<String, String>> hashMap2;
        while (true) {
            hashMap2 = (HashMap<String, HashMap<String, String>>)hashMap;
            if (!iterator.hasNext()) {
                break;
            }
            final Map<String, Object> map = iterator.next();
            final HashMap<String, String> hashMap3 = new HashMap<String, String>();
            final boolean b = map.get("sVS") != null;
            final boolean b2 = map.get("sVE") != null;
            d d;
            if (b && b2) {
                d = g.d.\u03b9;
            }
            else if (b) {
                d = g.d.\u0399;
            }
            else {
                d = g.d.\u0269;
            }
            if (d == g.d.\u0269) {
                hashMap2 = new HashMap<String, HashMap<String, String>>();
                hashMap2.put("er", (HashMap<String, String>)"na");
                break;
            }
            final Integer n = map.get("sT");
            final String s = map.get("sN");
            if (s != null) {
                hashMap3.put("n", s);
            }
            else {
                hashMap3.put("n", "uk");
            }
            final b b3 = g.b.values()[n.intValue()];
            final ArrayList list2 = new ArrayList<BigDecimal>(\u0131(map.get("sVS")));
            if (d == g.d.\u03b9) {
                list2.addAll((Collection<?>)\u0131(map.get("sVE")));
            }
            ArrayList<ArrayList<Double>> list5;
            if (b3 == g.b.\u03b9) {
                final ArrayList<Double> list3 = new ArrayList<Double>();
                final BigDecimal value = BigDecimal.valueOf(Math.atan2(list2.get(1).doubleValue(), list2.get(0).doubleValue()) * 57.29577951308232);
                final DecimalFormat decimalFormat = new DecimalFormat("##.#");
                decimalFormat.setRoundingMode(RoundingMode.DOWN);
                list3.add(Double.parseDouble(decimalFormat.format(value)));
                final BigDecimal bigDecimal = list2.get(2);
                final DecimalFormat decimalFormat2 = new DecimalFormat("##.#");
                decimalFormat2.setRoundingMode(RoundingMode.DOWN);
                list3.add(Double.parseDouble(decimalFormat2.format(bigDecimal)));
                final ArrayList<Double> list4 = new ArrayList<Double>();
                if (list2.size() > 5) {
                    final BigDecimal subtract = BigDecimal.valueOf(Math.atan2(list2.get(4).doubleValue(), list2.get(3).doubleValue()) * 57.29577951308232).subtract(value);
                    final DecimalFormat decimalFormat3 = new DecimalFormat("##.#");
                    decimalFormat3.setRoundingMode(RoundingMode.DOWN);
                    list4.add(Double.parseDouble(decimalFormat3.format(subtract)));
                    final BigDecimal subtract2 = list2.get(5).subtract(list2.get(2));
                    final DecimalFormat decimalFormat4 = new DecimalFormat("##.#");
                    decimalFormat4.setRoundingMode(RoundingMode.DOWN);
                    list4.add(Double.parseDouble(decimalFormat4.format(subtract2)));
                }
                list5 = new ArrayList<ArrayList<Double>>();
                list5.add(list3);
                list5.add(list4);
            }
            else {
                final ArrayList<Double> list6 = new ArrayList<Double>();
                if (list2.size() > 5) {
                    final BigDecimal subtract3 = list2.get(3).subtract(list2.get(0));
                    final DecimalFormat decimalFormat5 = new DecimalFormat("##.#");
                    decimalFormat5.setRoundingMode(RoundingMode.DOWN);
                    list6.add(Double.parseDouble(decimalFormat5.format(subtract3)));
                    final BigDecimal subtract4 = list2.get(4).subtract(list2.get(1));
                    final DecimalFormat decimalFormat6 = new DecimalFormat("##.#");
                    decimalFormat6.setRoundingMode(RoundingMode.DOWN);
                    list6.add(Double.parseDouble(decimalFormat6.format(subtract4)));
                    final BigDecimal subtract5 = list2.get(5).subtract(list2.get(2));
                    final DecimalFormat decimalFormat7 = new DecimalFormat("##.#");
                    decimalFormat7.setRoundingMode(RoundingMode.DOWN);
                    list6.add(Double.parseDouble(decimalFormat7.format(subtract5)));
                }
                final ArrayList<Double> list7 = new ArrayList<Double>();
                final BigDecimal bigDecimal2 = list2.get(0);
                final DecimalFormat decimalFormat8 = new DecimalFormat("##.#");
                decimalFormat8.setRoundingMode(RoundingMode.DOWN);
                list7.add(Double.parseDouble(decimalFormat8.format(bigDecimal2)));
                final BigDecimal bigDecimal3 = list2.get(1);
                final DecimalFormat decimalFormat9 = new DecimalFormat("##.#");
                decimalFormat9.setRoundingMode(RoundingMode.DOWN);
                list7.add(Double.parseDouble(decimalFormat9.format(bigDecimal3)));
                final BigDecimal bigDecimal4 = list2.get(2);
                final DecimalFormat decimalFormat10 = new DecimalFormat("##.#");
                decimalFormat10.setRoundingMode(RoundingMode.DOWN);
                list7.add(Double.parseDouble(decimalFormat10.format(bigDecimal4)));
                list5 = new ArrayList<ArrayList<Double>>();
                list5.add(list7);
                list5.add(list6);
            }
            hashMap3.put("v", (String)list5);
            hashMap.put(c.values()[n.intValue()].\u0131, hashMap3);
            if (d != g.d.\u0399) {
                continue;
            }
            hashMap.put("er", (HashMap<String, String>)"no_svs");
        }
        return hashMap2;
    }
    
    enum b
    {
        \u0131, 
        \u01c3, 
        \u0269, 
        \u0399, 
        \u03b9;
    }
    
    enum c
    {
        \u0196("gs"), 
        \u01c3("am"), 
        \u0269("rs"), 
        \u0399("uk"), 
        \u03b9("mm");
        
        String \u0131;
        
        private c(final String \u0131) {
            this.\u0131 = \u0131;
        }
    }
    
    enum d
    {
        \u0269, 
        \u0399, 
        \u03b9;
    }
}
