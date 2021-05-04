package library;

import monster.*;
import encounter.*;
import player.*;
import java.util.ArrayList;

public interface DNDLibrary {
	public Monster getMonster(String name);
	public Encounter getEncounter(String gname);
	public PlayerCharacter getPlayerCharacter(String name);
	public boolean addMonster(String name);
	public boolean addEncounter(String name);
	public boolean addPlayerCharacter(String name);
	public boolean deleteMonster(String name);
	public boolean deleteEncounter(String name);
	public boolean deletePlayerCharacter(String name);
	public boolean renameMonster(String oldName, Monster monster);
	public boolean renameEncounter(String oldName, Encounter encounter);
	public boolean updateMonster(Monster monster);
	public boolean updateEncounter(Encounter encounter);
	public boolean updatePlayerCharacter(PlayerCharacter character);
	public boolean restoreMonster(String name);
	public boolean restoreEncounter(String name);
	public ArrayList<String> getMonsterList();
	public ArrayList<String> getEncounterList();
	public ArrayList<String> getPlayerCharacterList();
	public boolean saveMonster(String name);
	public boolean saveEncounter(String name);
	public boolean savePlayerCharacters();
	public boolean saveAll();
}
