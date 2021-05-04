package monster;

import org.json.simple.JSONObject;

public class Action {
	private String name;
	private String description;
	
	public Action() {
		name = "name";
		description = "description";
	}
	
	/**
	 * Constructs a monster action from json styled string
	 */
	/*Action(String jsonString) {
		this(new JSONObject(jsonString));
	}*/
	
	/**
	 * Constructs a monster action from json object
	 */
	Action(JSONObject jsonObj) {
		try {
			name = (String) jsonObj.get("name");
			description = (String) jsonObj.get("description");
		} catch (Exception e) {
			System.out.println("Error in monster.Action(JsonObject): " + e.getMessage());
		}
	}
	
	/**
	 * Gets the name of this action
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the descripton of this action
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Sets the name of this action
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Sets the descripton of this action
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	public JSONObject toJson() {
		JSONObject obj = new JSONObject();
		
		try {
			obj.put("name", name);
			obj.put("description", description);
		} catch (Exception e) {
			System.out.println("Error in monster.Action.toJson: " + e.getMessage());
		}
		
		return obj;
	}
}
