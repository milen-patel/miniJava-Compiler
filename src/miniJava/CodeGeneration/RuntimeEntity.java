package miniJava.CodeGeneration;
import mJAM.Machine.Reg;

public class RuntimeEntity {
	Reg baseRegister;
	int displacement;
	
	public RuntimeEntity(Reg baseRegister, int displacement) {
		this.baseRegister = baseRegister;
		this.displacement = displacement;
	}
	
	@Override
	public String toString() {
		return "[Register: " + baseRegister + ", Displacement: " + displacement + "]";
	}
	
	public RuntimeEntity duplicate() {
		return new RuntimeEntity(this.baseRegister, this.displacement);
	}
}
