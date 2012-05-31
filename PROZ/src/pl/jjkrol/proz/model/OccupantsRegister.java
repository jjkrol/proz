package pl.jjkrol.proz.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import pl.jjkrol.proz.mockups.OccupantMockup;

public class OccupantsRegister {
	
	private Map<Integer, Occupant> occupants = new HashMap<Integer, Occupant>();
	private int nextId;
	static Logger logger = Logger.getLogger(OccupantsRegister.class);
	private static volatile OccupantsRegister instance = null;

	public static OccupantsRegister getInstance() {
		if (instance == null) {
			synchronized (OccupantsRegister.class) {
				if (instance == null)
					instance = new OccupantsRegister();
			}
		}
		return instance;
	}
	
	private OccupantsRegister(){
		nextId = 0;
		//TODO: read from db
	}
	
	public Occupant createOccupant(String name){
		int id = getNextId();
		Occupant occ =  new Occupant(id, name);
		occupants.put(id, occ);
		return occ;
	}
	
	public Occupant createOccupant(OccupantMockup moc){
		int id = getNextId();
		Occupant occ = new Occupant(id, moc);
		occupants.put(id, occ);
		return occ;
	}
	
	public Occupant findOccupant(OccupantMockup moc){
		return occupants.get(moc.id);
		//TODO get by other if not null
	}
	private int getNextId(){
		return nextId++;
	}
	
	public void editOccupant(int id, OccupantMockup moc){
		occupants.get(id).setAttributes(moc);
	}
	
	public void deleteOccupant(int id){
		occupants.remove(id);
	}

	public List<Occupant> getOccupants(){
		List<Occupant> retList = new ArrayList<Occupant>();
		for(Occupant occ : occupants.values()){
			retList.add(occ);
		}
		return retList;
	}
	
	public List<OccupantMockup> getOccupantsMockups(){
		List<OccupantMockup> retList = new ArrayList<OccupantMockup>();
		for(Occupant occ : occupants.values()){
			retList.add(occ.getMockup());
		}
		return retList;
	}

}
