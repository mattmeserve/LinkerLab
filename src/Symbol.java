
public class Symbol {
	private boolean isUsed = false;
	private boolean isUsedInModule = false;
	private boolean isDefinedInUse = false;
	private boolean isInUseList = false;
	private int absoluteAddress;
	private int localAddress;
	private String name;
	
	public Symbol(String name, int absoluteAddress) {
		this.absoluteAddress = absoluteAddress;
		this.name = name;
	}
	
	public Symbol() {
		
	}
	
	public void used() {
		isUsed = true;
	}
	
	public void usedInModule(boolean isUsedInModule) {
		this.isUsedInModule = isUsedInModule;
	}
	
	public void definedInUse(boolean isDefinedInUse) {
		this.isDefinedInUse = isDefinedInUse;
	}
	
	public void inUseList() {
		isInUseList = true;
	}
	
	public boolean isUsed() {
		return isUsed;
	}
	
	public boolean isUsedInModule() {
		return isUsedInModule;
	}
	
	public boolean isDefinedInUse() {
		return isDefinedInUse;
	}
	
	public boolean isInUseList() {
		return isInUseList;
	}
	
	public void setAbsoluteAddress(int absoluteAddress) {
		this.absoluteAddress = absoluteAddress;
	}
	
	public int getAbsoluteAddress() {
		return absoluteAddress;
	}
	
	public void setLocalAddress(int localAddress) {
		this.localAddress = localAddress;
	}
	
	public int getLocalAddress() {
		return localAddress;
	}
	
	public String getName() {
		return name;
	}
}
