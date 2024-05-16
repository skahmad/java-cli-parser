package creator.cli;

import java.util.*;

/**
 * this class is used to manage command line argument
 */
public class Arguments {
	protected Map<String, List<String>> argsAndValue = new HashMap<>();
	private String[] args;
	protected String appName = "";
	protected String argName = "";
	protected List<String> values = new ArrayList<>();
	protected List<String> mandatory = new ArrayList<>();
	
	private StringBuilder stringBuilder = new StringBuilder();
	protected String note = "";
	protected String appDetails = "";
	//String allUsage = "";
	
	public enum ArgType {
		Optional,
		Mandatory
	};
	
	
	public Arguments(String appName) {
		this.appName = appName;
		this.argName = appName;
	}
	
	public Arguments(String appName, String[] args) throws Exception {
		if(appName == null || appName.length() == 0) {
			throw new Exception("App name should not empty!");
		}
		
		this.appName = appName;
		this.argName = appName;
		this.args = args;
	}
	
	public Arguments setMandatory(String... mandatory) {
		this.mandatory = Arrays.asList(mandatory);
		return this;
	}
	
	public boolean parse() throws Exception {
		for(String s: this.args) {
			if(s.startsWith("-")) {
				argsAndValue.put(argName.toLowerCase(), values);
				
				values = new ArrayList<>();
				argName = s;
			} else {
				values.add(s.toLowerCase());
			}
		}
		
		argsAndValue.put(argName.toLowerCase(), values);
		
		if(!mandatory.isEmpty()) {
			List<String> args = this.getArguments();
			for(String m:this.mandatory) {
				if( !args.contains("-" + m) ){
					throw new Exception("argument missing -" + m);
				}
			}
		}
		return true;
	}
	
	public void setArgs(String[] args) {
		this.args = args;
	}
	
	public List<String> getValueByArg(String arg) {
		try {
			return this.argsAndValue.get(appName.equalsIgnoreCase(arg)? arg.toLowerCase(): ("-" + arg.toLowerCase()));
		}catch (Exception e)  {
			return new ArrayList<>();
		}
	}
	
	public List<String> getArguments() {
		List<String> keys = new ArrayList<>(this.argsAndValue.keySet());
		Collections.reverse(keys);
		return keys;
	}
	
	public String getArgument(int index) {
		return new ArrayList<>(this.argsAndValue.keySet()).get(index);
	}
	
	public Map<String, List<String>> getArgAndValue() {
		return this.argsAndValue;
	}
	
	
	
	// for help
	public Arguments setAppDetails(String details) {
		this.appDetails = details;
		return this;
	}
	
	public Arguments setNote(String note) {
		this.note = note;
		return this;
	}
	
	public Arguments addAvailableAgrAndDetails(String arg, ArgType type, String description) {
		stringBuilder
			.append("-")
			.append(arg)
			.append("\t")
			.append(type.name())
			.append("\t")
			.append(description)
			.append("\n");
		return this;
	}
	
	
	public boolean find(String arg) {
		//return this.argsAndValue.keySet().stream().anyMatch(s->s.equals("-" + arg));
		return this.argsAndValue.containsKey('-' + arg);
	}
	
	public void help() {
		System.out.println("");
		System.out.println(this.appName);
		System.out.println(appDetails);
		System.out.println(stringBuilder.toString());
		System.out.println(note);
	}
	
	public void info() {
		System.out.println("### App name: " + this.appName);
		System.out.println("### App details: " + this.appDetails);
		System.out.println("### Mandatory : ");
		this.mandatory.forEach(m->{
			System.out.print("-" + m + " ");
		});
		
		System.out.println("\n### Argument and value : ");
		this.argsAndValue.forEach((a,v)->{
			System.out.print(a + ": ");
			v.forEach(p -> {
				System.out.print(p + " ");
			});
			System.out.println("");
		});
		
		System.out.println("\n### Usage : ");
		System.out.println(this.stringBuilder.toString());
		System.out.println(this.note);
	}
	
	public void clear() {
		argsAndValue.clear();
		mandatory.clear();
		values.clear();
		this.argName = this.appName;
	}
	
	public String[] getArgs() {
		return args;
	}
}
