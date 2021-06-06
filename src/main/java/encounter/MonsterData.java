package encounter;

import org.json.simple.JSONObject;

public class MonsterData {
    private String monster;
    private int quantity, xp, initiative;
    private boolean minion, reinforcement;

    public MonsterData(JSONObject json) {
        try {
            monster = (String) json.get("monster");
            quantity = (int) (long) json.get("quantity");
            xp = (int) (long) json.get("xp");
            minion = (boolean) json.get("minion");
            reinforcement = (boolean) json.get("reinforcement");
            initiative = (int) (long) json.get("initiative");
        } catch (Exception e) {
            System.out.println("Error in encounter.MonsterData(json) " + e.getMessage());
        }
    }

    public MonsterData(String monster, int xp) {
        this.monster = monster;
        this.xp = xp;
        quantity = 1;
        minion = false;
        reinforcement = false;
        initiative = 1;
    }

    public String getMonster() {
        return monster;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getXP() {
        return xp;
    }

    public int getInitiative() {
        return initiative;
    }

    public boolean isMinion() {
        return minion;
    }

    public boolean isReinforcement() {
        return reinforcement;
    }

    public void setMonster(String monster) {
        this.monster = monster;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setXP(int xp) {
        this.xp = xp;
    }

    public void setInitiative(int initiative) {
        this.initiative = initiative;
    }

    public void setMinion(boolean minion) {
        this.minion = minion;
    }

    public void setReinforcement(boolean reinforcement) {
        this.reinforcement = reinforcement;
    }

    public String toString() {
        return "monster: " + monster + " quantity: " + quantity;
    }

    public JSONObject toJson() {
        JSONObject obj = new JSONObject();

        try {
            obj.put("monster", monster);
            obj.put("quantity", quantity);
            obj.put("xp", xp);
            obj.put("minion", minion);
            obj.put("reinforcement", reinforcement);
            obj.put("initiative", initiative);
        } catch (Exception e) {
            System.out.println("Error in encounter.MonsterData.toJson: " + e.getMessage());
        }

        return obj;
    }
}
