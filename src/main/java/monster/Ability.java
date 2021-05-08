package monster;

import org.json.simple.JSONObject;

public class Ability {
    private String name;
    private String description;

    public Ability() {
        name = "name";
        description = "description";
    }

    /**
     * Constructs a monster ability from json styled string
     */
    /*Ability(String jsonString) {
        this(new JSONObject(jsonString));
    }*/

    /**
     * Constructs a monster ability from json object
     */
    Ability(JSONObject jsonObj) {
        try {
            name = (String) jsonObj.get("name");
            description = (String) jsonObj.get("description");
        } catch (Exception e) {
            System.out.println("Error in monster.Action(JsonObject): " + e.getMessage());
        }
    }

    /**
     * Gets the name of this ability
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the descripton of this ability
     */
    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String desc) {
        description = desc;
    }

    public JSONObject toJson() {
        JSONObject obj = new JSONObject();

        try {
            obj.put("name", name);
            obj.put("description", description);
        } catch (Exception e) {
            System.out.println("Error in monster.Ability.toJson: " + e.getMessage());
        }

        return obj;
    }
}
