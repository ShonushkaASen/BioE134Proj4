public class Box{
	private HashMap<Name, Hashmap<Concentration, location>> everythings_loc = new HashMap<Name, Hashmap<Concentration, location>>;
	private Location box_loc;
	private Queue<Location> emptyspots = new Queue;
	private Name box_name;
	private Experimentalthrd currthrd;

	public void Box(Hashmap<Concentration, Hashmap<Concentration, Location>> data, Location boxs_loc, )
	public Location get(Location loc){

	}
	public void put(Name name, Concentration conc){
		Location filled = emptyspots.pop();
		HashMap newstuff = new HashMap<Concentration, Location>();
		newstuff.put(conc, filled);
		everythings_loc.put(name, newstuff);
	}
	public Location getlab_loc(){
		return box_loc;
	}
	public boolean containsName(Name name){
		return everythings_loc.contains(name);
	}
	public Name getbox_name(){
		return box_name;
	}

}