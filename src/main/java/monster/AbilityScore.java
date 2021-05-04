package monster;

import org.json.simple.JSONObject;

public class AbilityScore {
	private int score;
	boolean proficient;
	private String stat;
	
	public AbilityScore() {
		score = 10;
		proficient = false;
	}
	
	/**
	 * Constructs a monster ability score from json styled string
	 */
	/*AbilityScore(String jsonString) {
		this(new JSONObject(jsonString));
	}*/
	
	/**
	 * Constructs a monster ability from json object
	 */
	AbilityScore(JSONObject jsonObj) {
		try {
			stat = (String) jsonObj.get("stat");
			score = (int) (long) jsonObj.get("score");
			proficient = (boolean) jsonObj.get("proficient");
		} catch (Exception e) {
			System.out.println("Error in monster.Action(JsonObject): " + e.getMessage());
		}
	}
	
	public int getScore() {
		return score;
	}
	
	public boolean getProficient() {
		return proficient;
	}
	
	public void setStat(String stat) {
		this.stat = stat;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public void setProficient(boolean proficient) {
		this.proficient = proficient;
	}
	
	public JSONObject toJson() {
		JSONObject obj = new JSONObject();
		
		try {
			obj.put("stat", stat);
			obj.put("score", score);
			obj.put("proficient", proficient);
		} catch (Exception e) {
			System.out.println("Error in monster.Ability.toJson: " + e.getMessage());
		}
		
		return obj;
	}
}
