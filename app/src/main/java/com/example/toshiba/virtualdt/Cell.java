package com.example.toshiba.virtualdt;

import android.telephony.TelephonyManager;

/**
 * Created by TOSHIBA on 05/12/2016.
 *
 */

public class Cell  implements CellImpl{
    private String sigle;
    private int id;
    private int mcc;
    private int mnc;
    private int tac;
    private int lac;
    private String lat;
    private String lon;
    private String averageSignalStrength;
    private String radio;
    private String range;
    private String Samples;
    private String changeable;
    private String method_access;
    private String method_access_type;

    private String cellSignalStrength;
    private double cellSignalStrengthwWatt;
    private int cellSignalStrengthLevel;
    private int CellSignalStrengthDbm;
    private int asuLevel;

    //wcdma
    private int psc;
    private int ucid;
    private int rnc;

    //lte

    private int pci; //exist also in opencellid
    private int cqi;
    private int eci;
    private int rsrp;
    private int rsrq;
    private int rssnr;



    public Cell (){
    }


    public Cell(int id, int mcc, int mnc, int tac) {
        this.id = id;
        this.mcc = mcc;
        this.mnc = mnc;
        this.tac = tac;
    }

    public int getCqi() {
        return cqi;
    }

    public void setCqi(int cqi) {
        this.cqi = cqi;
    }

    public int getEci() {
        return eci;
    }

    public void setEci(int eci) {
        this.eci = eci;
    }

    public int getRsrp() {
        return rsrp;
    }

    public void setRsrp(int rsrp) {
        this.rsrp = rsrp;
    }

    public int getRsrq() {
        return rsrq;
    }

    public void setRsrq(int rsrq) {
        this.rsrq = rsrq;
    }

    public int getRssnr() {
        return rssnr;
    }

    public void setRssnr(int rssnr) {
        this.rssnr = rssnr;
    }



    public int getRnc() {
        return rnc;
    }

    public void setRnc(int rnc) {
        this.rnc = rnc;
    }

    public int getPsc() {
        return psc;
    }

    public void setPsc(int psc) {
        this.psc = psc;
    }

    public int getUcid() {
        return ucid;
    }

    public void setUcid(int ucid) {
        this.ucid = ucid;
    }

    public String getMethod_access_type() {
        return method_access_type;
    }

    public void setMethod_access_type(int  method_access_type) {

        switch (method_access_type) {
            case TelephonyManager.NETWORK_TYPE_1xRTT: this.method_access_type="1xRTT" ;break;
            case TelephonyManager.NETWORK_TYPE_CDMA: this.method_access_type="CDMA" ;break;
            case TelephonyManager.NETWORK_TYPE_EDGE: this.method_access_type="EDGE" ;break;
            case TelephonyManager.NETWORK_TYPE_EHRPD: this.method_access_type="eHRPD" ;break;
            case TelephonyManager.NETWORK_TYPE_EVDO_0: this.method_access_type="EVDO rev. 0" ;break;
            case TelephonyManager.NETWORK_TYPE_EVDO_A: this.method_access_type="EVDO rev. A" ;break;
            case TelephonyManager.NETWORK_TYPE_EVDO_B: this.method_access_type="EVDO rev. B" ;break;
            case TelephonyManager.NETWORK_TYPE_GPRS: this.method_access_type=" GPRS" ;break;
            case TelephonyManager.NETWORK_TYPE_HSDPA: this.method_access_type="HSDPA" ;break;
            case TelephonyManager.NETWORK_TYPE_HSPA: this.method_access_type="HSPA" ;break;
            case TelephonyManager.NETWORK_TYPE_HSPAP: this.method_access_type="HSPA+" ;break;
            case TelephonyManager.NETWORK_TYPE_HSUPA: this.method_access_type="HSUPA" ;break;
            case TelephonyManager.NETWORK_TYPE_IDEN: this.method_access_type="iDen" ;break;
            case TelephonyManager.NETWORK_TYPE_LTE: this.method_access_type="LTE" ;break;
            case TelephonyManager.NETWORK_TYPE_UMTS: this.method_access_type="UMTS" ;break;
            default:
            case TelephonyManager.NETWORK_TYPE_UNKNOWN: this.method_access_type="deconnecté" ;break;
        }
        
    }

    public String getSigle() {
        return sigle;
    }

    public void setSigle(String sigle) {

        this.sigle = sigle;
    }

    public int getAsuLevel() {
        return asuLevel;
    }

    public void setAsuLevel(int asuLevel) {
        this.asuLevel = asuLevel;
    }

    public double getCellSignalStrengthwWatt() {
        return cellSignalStrengthwWatt;
    }

    public void setCellSignalStrengthwWatt(double cellSignalStrengthwWatt) {
        this.cellSignalStrengthwWatt = cellSignalStrengthwWatt;
    }

    public int getCellSignalStrengthLevel() {
        return cellSignalStrengthLevel;
    }

    public void setCellSignalStrengthLevel(int cellSignalStrengthLevel) {
        this.cellSignalStrengthLevel = cellSignalStrengthLevel;
    }

    public int getCellSignalStrengthDbm() {
        return CellSignalStrengthDbm;
    }

    public void setCellSignalStrengthDbm(int cellSignalStrengthDbm) {
        CellSignalStrengthDbm = cellSignalStrengthDbm;
    }

    public String getCellSignalStrength() {
        return cellSignalStrength;
    }

    public void setCellSignalStrength(String cellSignalStrength) {
        this.cellSignalStrength = cellSignalStrength;
    }

    public int getLac() {
        return lac;
    }

    public void setLac(int lac) {
        this.lac = lac;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMethod_access() {
        return method_access;
    }

    public void setMethod_access(String method_access) {
        this.method_access = method_access;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public int getMcc() {
        return mcc;
    }

    public void setMcc(int mcc) {
        this.mcc = mcc;
    }

    public int getMnc() {
        return mnc;
    }

    public void setMnc(int mnc) {
        this.mnc = mnc;
    }

    public String getAverageSignalStrength() {
        return averageSignalStrength;
    }

    public void setAverageSignalStrength(String averageSignalStrength) {
        this.averageSignalStrength = averageSignalStrength;
    }

    public int getPci() {
        return pci;
    }

    public void setPci(int pci) {
        this.pci = pci;
    }

    public String getRadio() {
        return radio;
    }

    public void setRadio(String radio) {
        this.radio = radio;
    }

    public int getTac() {
        return tac;
    }

    public void setTac(int tac) {
        this.tac = tac;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getSamples() {
        return Samples;
    }

    public void setSamples(String samples) {
        Samples = samples;
    }

    public String getChangeable() {
        return changeable;
    }

    public void setChangeable(String changeable) {
        this.changeable = changeable;
    }


    @Override
    public String toString() {
        return "Cell{" +
                "sigle='" + sigle + '\'' +
                ", id=" + id +
                ", mcc=" + mcc +
                ", mnc=" + mnc +
                ", tac=" + tac +
                ", lac=" + lac +
                ", lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                ", averageSignalStrength='" + averageSignalStrength + '\'' +
                ", radio='" + radio + '\'' +
                ", range='" + range + '\'' +
                ", Samples='" + Samples + '\'' +
                ", changeable='" + changeable + '\'' +
                ", method_access='" + method_access + '\'' +
                ", method_access_type='" + method_access_type + '\'' +
                ", cellSignalStrength='" + cellSignalStrength + '\'' +
                ", cellSignalStrengthwWatt=" + cellSignalStrengthwWatt +
                ", cellSignalStrengthLevel=" + cellSignalStrengthLevel +
                ", CellSignalStrengthDbm=" + CellSignalStrengthDbm +
                ", asuLevel=" + asuLevel +
                ", psc=" + psc +
                ", ucid=" + ucid +
                ", rnc=" + rnc +
                ", pci=" + pci +
                ", cqi=" + cqi +
                ", eci=" + eci +
                ", rsrp=" + rsrp +
                ", rsrq=" + rsrq +
                ", rssnr=" + rssnr +
                '}';
    }
}
