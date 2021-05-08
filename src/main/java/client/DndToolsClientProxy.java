package client;

import encounter.Encounter;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import library.DNDLibrary;
import monster.Monster;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import player.PlayerCharacter;

/**
 * DndToolsClientProxy is a proxy for communication with the DndToolsServer which contains a
 * library of Monster, Encounter, and Player Character data that can be edited by the client
 * application. This proxy has the methods required for getting and sending the aforementioned
 * objects to and from the server, saving the data on the server, and communicating with the server
 * to run Combat.
 */
public class DndToolsClientProxy implements DNDLibrary {
    private static final int buffSize = 6144;
    private String host;
    private int port;

    /**
     * Creates a client proxy that connects to a specified host and port.
     *
     * @param host The host address
     * @param port The host port number
     */
    public DndToolsClientProxy(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private String callMethod(String library, String method, Object[] params) {
        JSONObject call = new JSONObject();
        String result = "";

        try {
            ArrayList<Object> list = new ArrayList();

            call.put("library", library);
            call.put("method", method);

            for (int i = 0; i <  params.length; i++) {
                list.add(params[i]);
            }

            JSONArray jsonParams = new JSONArray();
            jsonParams.addAll(list);
            call.put("params", jsonParams);

            Socket sock = new Socket(host, port);
            OutputStream out = sock.getOutputStream();

            String request = call.toString();
            byte[] bytesToSend = request.getBytes();
            System.out.println();
            System.out.println("Request: " + request);
            //System.out.println("length: " + bytesToSend.length);
            //out.write(bytesToSend, 0, bytesToSend.length);
            PrintWriter writer = new PrintWriter(out, true);
            writer.println(request);

            //TODO: update read to use BufferedReader to maintain consistency with server
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[buffSize];

            InputStream in = sock.getInputStream();

            for (int i; (i = in.read(buffer)) != -1; ) {
                baos.write(buffer, 0, i);
            }

            result = baos.toString();

            out.close();
            in.close();
            sock.close();
        } catch (Exception e) {
            System.out.println("Exception in callMethod: " + e.getMessage());
            result = "{}";
        }

        //Debug
        //System.out.println("Received " + result + " from server.");

        return result;
    }

    /**
     * Gets a Monster from the library server.
     *
     * @param name The name of the Monster
     * @return The Monster object
     */
    public Monster getMonster(String name) {
        String result = callMethod("monster", "get", new Object[]{name});
        JSONParser parser = new JSONParser();
        JSONObject jsonResult = null;

        try {
            jsonResult = (JSONObject) parser.parse(result);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Monster();
        }

        return new Monster((JSONObject) jsonResult.get("result"));
    }

    /**
     * Gets an Encounter from the library server.
     *
     * @param name The name of the Encounter
     * @return The Encounter object
     */
    public Encounter getEncounter(String name) {
        String result = callMethod("encounter", "get", new Object[]{name});
        JSONParser parser = new JSONParser();
        JSONObject jsonResult = null;

        try {
            jsonResult = (JSONObject) parser.parse(result);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Encounter();
        }

        return new Encounter((JSONObject) jsonResult.get("result"));
    }

    /**
     * Gets a PlayerCharacter from the library server.
     *
     * @param name The name of the PlayerCharacter
     * @return The PlayerCharacter object
     */
    public PlayerCharacter getPlayerCharacter(String name) {
        String result = callMethod("pc", "get", new Object[]{name});
        JSONParser parser = new JSONParser();
        JSONObject jsonResult = null;

        try {
            jsonResult = (JSONObject) parser.parse(result);
        } catch (ParseException e) {
            e.printStackTrace();
            return new PlayerCharacter();
        }

        return new PlayerCharacter((String) jsonResult.get("result"));
    }

    /**
     * Adds a new Monster to the server library.
     *
     * @param name THe name of the monster to add
     * @return True if the Monster was added, false if it was not. False will usually be returned
     *      only if a Monster with the specified name already exists on the server.
     */
    public boolean addMonster(String name) {
        String result = callMethod("monster", "add", new Object[]{name});
        JSONParser parser = new JSONParser();
        JSONObject jsonResult = null;

        try {
            jsonResult = (JSONObject) parser.parse(result);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        return (boolean) jsonResult.get("result");
    }

    /**
     * Adds a new Encounter to the server library.
     *
     * @param name The name of the new Encounter
     * @return True if the Encounter was added, false if it was not. False will usually be returned
     *      only if an Encounter with the specified name already exists on the server.
     */
    public boolean addEncounter(String name) {
        String result = callMethod("encounter", "add", new Object[]{name});
        JSONParser parser = new JSONParser();
        JSONObject jsonResult = null;

        try {
            jsonResult = (JSONObject) parser.parse(result);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        return (boolean) jsonResult.get("result");
    }

    /**
     * Adds a new PlayerCharacter to the server library.
     *
     * @param name The name of the PlayerCharacter
     * @return True if the Player was added, false if it was not. False will usually be returned
     *      only if a Player with the specified name already exists on the server.
     */
    public boolean addPlayerCharacter(String name) {
        String result = callMethod("pc", "add", new Object[]{name});
        JSONParser parser = new JSONParser();
        JSONObject jsonResult = null;

        try {
            jsonResult = (JSONObject) parser.parse(result);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        return (boolean) jsonResult.get("result");
    }

    /**
     * Deletes a Monster from the server library.
     *
     * @param name The name of the Monster to delete
     * @return True if the Monster was deleted, false if it was not.
     */
    public boolean deleteMonster(String name) {
        String result = callMethod("monster", "delete", new Object[]{name});
        JSONParser parser = new JSONParser();
        JSONObject jsonResult = null;

        try {
            jsonResult = (JSONObject) parser.parse(result);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        return (boolean) jsonResult.get("result");
    }

    /**
     * Deletes an Encounter from the server library.
     *
     * @param name The name of the encounter to be deleted
     * @return True if the Encounter was deleted, false if it was not.
     */
    public boolean deleteEncounter(String name) {
        String result = callMethod("encounter", "delete", new Object[]{name});
        JSONParser parser = new JSONParser();
        JSONObject jsonResult = null;

        try {
            jsonResult = (JSONObject) parser.parse(result);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        return (boolean) jsonResult.get("result");
    }

    /**
     * Deletes a PlayerCharacter from the server library.
     *
     * @param name The name of the PlayerCharacter to be deleted
     * @return True if the PlayerCharacter was deleted, false if it was not.
     */
    public boolean deletePlayerCharacter(String name) {
        String result = callMethod("pc", "delete", new Object[]{name});
        JSONParser parser = new JSONParser();
        JSONObject jsonResult = null;

        try {
            jsonResult = (JSONObject) parser.parse(result);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        return (boolean) jsonResult.get("result");
    }

    /**
     * Renames a Monster on the server library.
     *
     * @param oldName The original name of the monster
     * @param monster The Monster object
     * @return True if the Monster was renamed, false if it was not.
     */
    public boolean renameMonster(String oldName, Monster monster) {
        String result = callMethod("monster", "change", new Object[]{oldName, monster.toJson()});
        JSONParser parser = new JSONParser();
        JSONObject jsonResult = null;

        try {
            jsonResult = (JSONObject) parser.parse(result);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        return (boolean) jsonResult.get("result");
    }

    /**
     * Renames a n Encounter on the server library.
     *
     * @param oldName The original name of the encounter
     * @param encounter The Encounter object
     * @return True if the Encounter was renamed, false if it was not.
     */
    public boolean renameEncounter(String oldName, Encounter encounter) {
        String result = callMethod("encounter", "change",
                new Object[]{oldName, encounter.toJson()});
        JSONParser parser = new JSONParser();
        JSONObject jsonResult = null;

        try {
            jsonResult = (JSONObject) parser.parse(result);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        return (boolean) jsonResult.get("result");
    }

    /**
     * Updates a Monster on the server library.
     *
     * @param monster The Monster object
     * @return True if the Monster was updated, false if it was not.
     */
    public boolean updateMonster(Monster monster) {
        String result = callMethod("monster", "update", new Object[]{monster.toJson()});
        JSONParser parser = new JSONParser();
        JSONObject jsonResult = null;

        try {
            jsonResult = (JSONObject) parser.parse(result);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        return (boolean) jsonResult.get("result");
    }

    /**
     * Updates an Encounter on the server library.
     *
     * @param encounter The Encounter object
     * @return True if the Encounter was updated, false if it was not.
     */
    public boolean updateEncounter(Encounter encounter) {
        String result = callMethod("encounter", "update", new Object[]{encounter.toJson()});
        JSONParser parser = new JSONParser();
        JSONObject jsonResult = null;

        try {
            jsonResult = (JSONObject) parser.parse(result);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        return (boolean) jsonResult.get("result");
    }

    /**
     * Updates a PlayerCharacter on the server library.
     *
     * @param pc The PlayerCharacter object
     * @return True if the PlayerCharacter was updated, false if it was not.
     */
    public boolean updatePlayerCharacter(PlayerCharacter pc) {
        String result = callMethod("pc", "update", new Object[]{pc.toJson()});
        JSONParser parser = new JSONParser();
        JSONObject jsonResult = null;

        try {
            jsonResult = (JSONObject) parser.parse(result);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        return (boolean) jsonResult.get("result");
    }

    /**
     * Restores a Monster from it's save saved state on the server library.
     *
     * @param name The name of the Monster to restore
     * @return True if the Monster was restored, false if it was not
     */
    public boolean restoreMonster(String name) {
        String result = callMethod("monster", "restore", new Object[]{name});
        JSONParser parser = new JSONParser();
        JSONObject jsonResult = null;

        try {
            jsonResult = (JSONObject) parser.parse(result);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        return (boolean) jsonResult.get("result");
    }

    /**
     * Restores an Encounter from it's save saved state on the server library.
     *
     * @param name The name of the Encounter to restore
     * @return True if the Encounter was restored, false if it was not
     */
    public boolean restoreEncounter(String name) {
        String result = callMethod("encounter", "restore", new Object[]{name});
        JSONParser parser = new JSONParser();
        JSONObject jsonResult = null;

        try {
            jsonResult = (JSONObject) parser.parse(result);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        return (boolean) jsonResult.get("result");
    }

    /**
     * Gets a list of all the Monsters stored on the server library.
     *
     * @return The names of all the Monsters in the library
     */
    public ArrayList<String> getMonsterList() {
        String result = callMethod("monster", "list", new Object[0]);
        JSONParser parser = new JSONParser();
        JSONObject jsonResult = null;

        try {
            jsonResult = (JSONObject) parser.parse(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<String>();
        }

        JSONArray arr = (JSONArray) jsonResult.get("result");
        ArrayList<String> list = new ArrayList();

        for (int i = 0; i < arr.size(); i++) {
            list.add((String) arr.get(i));
        }

        return list;
    }

    /**
     * Gets a list of all the Encounters stored on the server library.
     *
     * @return The names of all the Encounters in the library
     */
    public ArrayList<String> getEncounterList() {
        String result = callMethod("encounter", "list", new Object[0]);
        JSONParser parser = new JSONParser();
        JSONObject jsonResult = null;

        try {
            jsonResult = (JSONObject) parser.parse(result);
        } catch (ParseException e) {
            e.printStackTrace();
            return new ArrayList<String>();
        }

        JSONArray arr = (JSONArray) jsonResult.get("result");
        ArrayList<String> list = new ArrayList();

        for (int i = 0; i < arr.size(); i++) {
            list.add((String) arr.get(i));
        }

        return list;
    }

    /**
     * Gets a list of all the PlayerCharacters stored on the server library.
     *
     * @return The names of all the PlayerCharacters in the library
     */
    public ArrayList<String> getPlayerCharacterList() {
        String result = callMethod("pc", "list", new Object[0]);
        JSONParser parser = new JSONParser();
        JSONObject jsonResult = null;

        try {
            jsonResult = (JSONObject) parser.parse(result);
        } catch (ParseException e) {
            e.printStackTrace();
            return new ArrayList<String>();
        }

        JSONArray arr = (JSONArray) jsonResult.get("result");
        ArrayList<String> list = new ArrayList();

        for (int i = 0; i < arr.size(); i++) {
            list.add((String) arr.get(i));
        }

        return list;
    }

    /**
     * Saves a Monster to the server library.
     *
     * @param name The name of the Monster to save
     * @return True if the Monster was saved, false if it was not
     */
    public boolean saveMonster(String name) {
        String result = callMethod("monster", "save", new Object[]{name});
        JSONParser parser = new JSONParser();
        JSONObject jsonResult = null;

        try {
            jsonResult = (JSONObject) parser.parse(result);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        return (boolean) jsonResult.get("result");
    }

    /**
     * Saves an Encounter to the server library.
     *
     * @param name The name of the Encounter to save
     * @return True if the Encounter was saved, false if it was not
     */
    public boolean saveEncounter(String name) {
        String result = callMethod("encounter", "save", new Object[]{name});
        JSONParser parser = new JSONParser();
        JSONObject jsonResult = null;

        try {
            jsonResult = (JSONObject) parser.parse(result);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        return (boolean) jsonResult.get("result");
    }

    /**
     * Gets a list of all the music tracks on the sever library.
     *
     * @return A list of the names of all the music tracks
     */
    public ArrayList<String> getMusicList() {
        String result = callMethod("encounter", "music", new Object[0]);
        JSONParser parser = new JSONParser();
        JSONObject jsonResult = null;

        try {
            jsonResult = (JSONObject) parser.parse(result);
        } catch (ParseException e) {
            e.printStackTrace();
            return new ArrayList<String>();
        }

        JSONArray arr = (JSONArray) jsonResult.get("result");
        ArrayList<String> list = new ArrayList();

        for (int i = 0; i < arr.size(); i++) {
            list.add((String) arr.get(i));
        }

        return list;
    }

    /**
     * Saves all the Monster and Encounter data on the server library.
     *
     * @return True if everything was saved, false if it was not.
     */
    public boolean saveAll() {
        String result = callMethod("all", "save", new Object[0]);
        JSONParser parser = new JSONParser();
        JSONObject jsonResult = null;

        try {
            jsonResult = (JSONObject) parser.parse(result);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        return (boolean) jsonResult.get("result");
    }

    /**
     * Begins running Combat on the server.
     *
     * @param name The name of the Encounter to run
     * @return True if Combat started, false if it did not
     */
    public boolean startCombat(String name) {
        String result = callMethod("combat", "begin", new Object[]{name});
        JSONParser parser = new JSONParser();
        JSONObject jsonResult = null;

        try {
            jsonResult = (JSONObject) parser.parse(result);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        return (boolean) jsonResult.get("result");
    }

    /**
     * Updates COmbat data on the server.
     *
     * @param array A JSONArray of the Combatants' new data
     * @return True if Combat was updated, false if it was not
     */
    public boolean updateCombat(JSONArray array) {
        String result = callMethod("combat", "update", new Object[]{array});
        JSONParser parser = new JSONParser();
        JSONObject jsonResult = null;

        try {
            jsonResult = (JSONObject) parser.parse(result);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        return (boolean) jsonResult.get("result");
    }

    /**
     * Ends the Combat on the server.
     *
     * @return True if Combat ended, false if it did not
     */
    public boolean endCombat() {
        String result = callMethod("combat", "end", new Object[0]);
        JSONParser parser = new JSONParser();
        JSONObject jsonResult = null;

        try {
            jsonResult = (JSONObject) parser.parse(result);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        return (boolean) jsonResult.get("result");
    }
}
