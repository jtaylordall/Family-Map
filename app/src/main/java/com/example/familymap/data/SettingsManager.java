package com.example.familymap.data;

public class SettingsManager {

    private static SettingsManager single_instance = null;

    private boolean lifeStoryLinesOn;
    private boolean familyTreeLinesOn;
    private boolean spouseLinesOn;
    private boolean fathersSideOn;
    private boolean mothersSideOn;
    private boolean maleEventsOn;
    private boolean femaleEventsOn;

    private SettingsManager(){
        lifeStoryLinesOn = true;
        familyTreeLinesOn = true;
        spouseLinesOn = true;
        fathersSideOn = true;
        mothersSideOn = true;
        maleEventsOn = true;
        femaleEventsOn = true;
    }

    public static SettingsManager getInstance() {
        if (single_instance == null) {
            single_instance = new SettingsManager();
        }
        return single_instance;
    }

    public boolean isFamilyTreeLinesOn() {
        return familyTreeLinesOn;
    }

    public boolean isFathersSideOn() {
        return fathersSideOn;
    }

    public boolean isFemaleEventsOn() {
        return femaleEventsOn;
    }

    public boolean isLifeStoryLinesOn() {
        return lifeStoryLinesOn;
    }

    public boolean isSpouseLinesOn() {
        return spouseLinesOn;
    }

    public boolean isMaleEventsOn() {
        return maleEventsOn;
    }

    public boolean isMothersSideOn() {
        return mothersSideOn;
    }

    public void setFamilyTreeLinesOn(boolean familyTreeLinesOn) {
        this.familyTreeLinesOn = familyTreeLinesOn;
    }

    public void setFathersSideOn(boolean fathersSideOn) {
        this.fathersSideOn = fathersSideOn;
    }

    public void setFemaleEventsOn(boolean femaleEventsOn) {
        this.femaleEventsOn = femaleEventsOn;
    }

    public void setLifeStoryLinesOn(boolean lifeStoryLinesOn) {
        this.lifeStoryLinesOn = lifeStoryLinesOn;
    }

    public void setMaleEventsOn(boolean maleEventsOn) {
        this.maleEventsOn = maleEventsOn;
    }

    public void setMothersSideOn(boolean mothersSideOn) {
        this.mothersSideOn = mothersSideOn;
    }

    public void setSpouseLinesOn(boolean spouseLinesOn) {
        this.spouseLinesOn = spouseLinesOn;
    }

}
