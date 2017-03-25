package com.gwk.pikodoveexample;

import com.gwk.pikodove.annotation.PikoFixedLength;
import com.gwk.pikodove.annotation.PikoString;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * User Model to demonstrate data parser
 * Created by Zavyra on 2017-03-25.
 */

public class User {
    public boolean isAdmin;
    public byte accessLevel;
    public short type;
    public int currentBalance;
    public long totalPoint;
    public char initial;
    public float avgBalance;
    public double avgPoint;
    @PikoFixedLength(6) public String kodepos;
    @PikoString('\n') public String address;
    @PikoString public String name;

    public User() {

    }

    public void setByDefaultValue() {
        this.isAdmin = true;
        this.accessLevel = 0x12;
        this.type = 41;
        this.currentBalance = 123431;
        this.totalPoint = 123456789098L;
        this.initial = 'c';
        this.avgBalance = 2.5f;
        this.avgPoint = 203040.50607D;
        this.kodepos = "123456";
        this.address = "Bandung Kota";
        this.name = "Garuda Wisnu Kencana";
    }


    public void setByEmptyValue() {
        this.isAdmin = false;
        this.accessLevel = 0;
        this.type = 0;
        this.currentBalance = 0;
        this.totalPoint = 0;
        this.initial = 0;
        this.avgBalance = 0;
        this.avgPoint = 0;
        this.kodepos = "";
        this.address = null;
        this.name = null;
    }

    public void fromJsonObject(String jsonObject) throws JSONException {
        JSONObject j = new JSONObject(jsonObject);
        this.isAdmin = j.getBoolean("isAdmin");
        this.accessLevel = (byte) j.getInt("accessLevel");
        this.type = (short) j.getInt("type");
        this.currentBalance = j.getInt("currentBalance");
        this.totalPoint = j.getLong("totalPoint");
        this.initial = (char) j.getInt("initial");
        this.avgBalance = (float) j.getDouble("avgBalance");
        this.avgPoint = j.getDouble("avgPoint");
        this.kodepos = j.has("kodepos") ? j.getString("kodepos") : "";
        this.address = j.has("address") ? j.getString("address") : null;
        this.name = j.has("name") ? j.getString("name") : null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("isAdmin : ").append(isAdmin ? "true" : "false");
        sb.append("\naccessLevel : ").append(accessLevel);
        sb.append("\ntype : ").append(type);
        sb.append("\ncurrentBalance : ").append(currentBalance);
        sb.append("\ntotalPoint : ").append(totalPoint);
        sb.append("\ninitial : ").append(initial);
        sb.append("\navgBalance : ").append(avgBalance);
        sb.append("\navgPoint : ").append(avgPoint);
        sb.append("\nkodepos : ").append(kodepos);
        sb.append("\naddress : ").append(address);
        sb.append("\nname : ").append(name);
        return sb.toString();
    }

    public String toJsonObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("isAdmin",isAdmin);
            jsonObject.put("accessLevel",accessLevel);
            jsonObject.put("type",type);
            jsonObject.put("currentBalance",currentBalance);
            jsonObject.put("totalPoint",totalPoint);
            jsonObject.put("initial",initial);
            jsonObject.put("avgBalance",avgBalance);
            jsonObject.put("avgPoint",avgPoint);
            jsonObject.put("kodepos",kodepos);
            jsonObject.put("address",address);
            jsonObject.put("name",name);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
}
