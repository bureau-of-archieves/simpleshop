package simpleshop.common.test;

import java.util.ArrayList;

/**
 * Used to test ObjectGraphDFS.
 */
public class EntityTestObject {

    private Integer id;
    private String name;
    private String type;
    private EntityTestObject spouse;
    private ArrayList<EntityTestObject> relatives = new ArrayList<>();

    public ArrayList<EntityTestObject> getRelatives() {
        return relatives;
    }

    public void setRelatives(ArrayList<EntityTestObject> relatives) {
        this.relatives = relatives;
    }

    public EntityTestObject getSpouse() {
        return spouse;
    }

    public void setSpouse(EntityTestObject spouse) {
        this.spouse = spouse;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getScore() {
        return 99;
    }

}


