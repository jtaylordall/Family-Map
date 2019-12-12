package com.example.familymap.support;

import com.example.familymap.data.DataStash;
import com.example.familymap.model.Person;

import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import static com.example.familymap.data.Values.*;


public class FamilyFinder {

    private DataStash dataStash;

    public FamilyFinder() {
        dataStash = DataStash.getInstance();
    }

    public Map<Integer, Person> getPersonFamily(String personID) {
        Map<Integer, Person> famMapForOrder = new TreeMap<>();
        if (!"".equals(personID)) {
            Person[] allPeople = dataStash.getPersonWrapper().getPeople();
            Person person = findPerson(personID);

            int siblingCounter = 0;
            for (Person p : allPeople) {
                System.out.println(p.toString());
                int relationship = getRelationship(person, p);
                System.out.println(relationship);
                switch (relationship) {
                    case IS_FATHER:
                        famMapForOrder.put(IS_FATHER, p);
                        break;
                    case IS_MOTHER:
                        famMapForOrder.put(IS_MOTHER, p);
                        break;
                    case IS_SPOUSE:
                        famMapForOrder.put(IS_SPOUSE, p);
                        break;
                    case IS_CHILD:
                        famMapForOrder.put(IS_CHILD + siblingCounter, p);
                        siblingCounter++;
                        break;
                    default:
                        // p not related, do nothing
                }
                System.out.println();
            }
            System.out.println(famMapForOrder.toString());
        }
        return famMapForOrder;
    }

    private int getRelationship(Person person, Person relative) {
        String personID = person.getPersonID();
        String fatherID = person.getFatherID();
        String motherID = person.getMotherID();
        String spouseID = person.getSpouseID();

        String relativeID = relative.getPersonID();
        String relativeFatherID = relative.getFatherID();
        String relativeMotherID = relative.getMotherID();

        if (relativeID != null && personID != null && !relativeID.equals(personID)) {
            if (fatherID != null && // relative is father
                    !"".equals(fatherID) &&
                    relativeID.equals(fatherID)) {
                return IS_FATHER;
            } else if (motherID != null && // relative is mother
                    !"".equals(motherID) &&
                    relativeID.equals(motherID)) {
                return IS_MOTHER;
            } else if (spouseID != null && // relative is spouse
                    !"".equals(spouseID) &&
                    relativeID.equals(spouseID)) {
                return IS_SPOUSE;
            } else if (relativeFatherID != null && // relative is child (person is father)
                    relativeFatherID.equals(personID)) {
                return IS_CHILD;
            } else if (relativeMotherID != null && // relative is child (person is mother)
                    relativeMotherID.equals(personID)) {
                return IS_CHILD;
            }
        }
        return ISNT_FAMILY;
    }

    public Person findPerson(String personID) {
        Person[] allPeople = dataStash.getPersonWrapper().getPeople();
        Person person = null;
        if (personID != null && !"".equals(personID)) {
            for (Person p : allPeople) {
                if (personID.equals(p.getPersonID())) {
                    person = p;
                }
            }
        }
        return person;
    }

    public SortedSet<String> getFathersSideIds() {
        Person person = dataStash.getActivePerson();
        String personID = person.getPersonID();
        SortedSet<String> ids = new TreeSet<>();
        ids.add(personID);
        ids.addAll(sideHelper(findPerson(person.getFatherID())));
        return ids;
    }

    public SortedSet<String> getMothersSideIds() {
        Person person = dataStash.getActivePerson();
        String personID = person.getPersonID();
        SortedSet<String> ids = new TreeSet<>();
        ids.add(personID);
        ids.addAll(sideHelper(findPerson(person.getMotherID())));
        return ids;
    }

    private SortedSet<String> sideHelper(Person person) {
        SortedSet<String> out = new TreeSet<>();
        out.add(person.getPersonID());
        String fatherID = person.getFatherID();
        String motherID = person.getMotherID();
        if (fatherID != null && !"".equals(fatherID)) {
            Person father = findPerson(person.getFatherID());
            out.add(fatherID);
            out.addAll(sideHelper(father));
        }
        if (motherID != null && !"".equals(motherID)) {
            Person mother = findPerson(person.getMotherID());
            out.add(motherID);
            out.addAll(sideHelper(mother));
        }
        return out;
    }
}
