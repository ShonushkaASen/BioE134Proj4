public class Inventory{
	private List<Box> boxes;
	private Hashmap<Box> threadtobox;
	public void initiate(){
		ParseBoxFile parser = new ParseBoxFile;
		parser.initiate();
		File folder = new File("address of file folder");
		File[] listofboxes = folder.listFiles();
		threadtobox = new Hashmap(); 
		boxes = new ArrayList<>();
		for(File file; listofboxes){
			Box curr = parser.run(file);
			//We need the box's thread attribute in a seperate class
			Experimentalthrd currthrd = curr.getthrd();
			threadtobox.put(currthrd, curr);
			boxes.add(curr);
		}
	}
	//Maybe include thread info here to eliminate the need to iterate. 
	public String get(Name name, Concentration con){
		Location currloc = null;
		String lab_loc = null;	
		for(Box box: boxes){
			if (box.containsName(name)){
				Hashmap<Concentration, Location> loc = box.get(name);
				if (loc.contiansKey(con)){
					currloc = loc.get(con);
					lab_loc = box.getlab_loc();	
				} else {
					Set<Concentration> concentrations = loc.keySet();
					if (concentration.isEmpty()){
						//for now I am just going to print out these outputs... later on we can workout how we wanna handle these.
						System.out.println("There were no documented concentrations for this object");
						// -1 since that what we decided for those that have a null concentration
						currloc = loc.get(-1);
						lab_loc = box.getlab_loc();
					} else{
						for(Concentration curr: concentrations){
							//Concentration class should have a to Integer method
							if (curr % con == 0){
								currloc = loc.get(curr);
								lab_loc = box.getlab_loc();
								break;
							}
							currloc = loc.get(curr);
							lab_loc = box.getlab_loc();
							Double a = new double(curr);
							Double b = new double(con);
							//make a dilution object that higher levels can look for to output the below print statement
							System.out.println(a/b + " X solution of " + name + " needs to be made");
						}
						
					}
					
				}
			}
			continue;
		} 
		if (currloc == null && lab_loc = null){
			throw new Error("The thing you want does not exist in the inventory");
		}
		StringBuilder returnable = new StringBuilder();
		returnable.add(lab_loc + "/");
		returnable.add(box.getbox_name() + "/");
		returnable.add(currloc.getRow() + "/" + currloc.getCol());
		return returnable.toString();
	}
	public void put(Name name, Concentration concentration, Experimentalthrd thrd){
		Box currplace = threadtobox.get(thrd);
		//box itself should have a put method that handles the internals of changing that box's data
		currplace.put(name, concentration);
	}
	public File write(){
		//Updates the Inventory by creating updated copies of Inventory files

	}
}