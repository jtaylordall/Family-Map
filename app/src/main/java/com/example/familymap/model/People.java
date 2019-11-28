package com.example.familymap.model;

public class People {
    private Person[] data;

    public People(){
        data = new Person[]{};
    }

    public void setPeople(Person[] data) {
        this.data = data;
    }

    public Person[] getPeople() {
        return data;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(Person p : data){
            sb.append(p.toString()).append("\n---\n");
        }
        return sb.toString();
    }
}
