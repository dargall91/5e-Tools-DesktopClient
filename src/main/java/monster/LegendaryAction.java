package monster;

import org.json.simple.JSONObject;

public class LegendaryAction {
	private	String name, description;
	private int cost;


	public LegendaryAction() {
		name = "name";
		description = "description";
		cost = 1;
	}

	LegendaryAction(JSONObject jsonObj) {
		try {
			name = (String) jsonObj.get("name");
			description = (String) jsonObj.get("description");
			cost = (int) (long) jsonObj.get("cost");
		} catch (Exception e) {
			System.out.println("Error in monster.LegendaryAction(JsonObject): " + e.getMessage());
		}
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public int getCost() {
		return cost;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public JSONObject toJson() {
		JSONObject obj = new JSONObject();

		try {
			obj.put("name", name);
			obj.put("description", description);
			obj.put("cost", cost);

			System.out.println(obj.toString());
		} catch (Exception e) {
			System.out.println("Error in monster.LegendaryAction.toJson: " + e.getMessage());
		}

		return obj;
	}
}