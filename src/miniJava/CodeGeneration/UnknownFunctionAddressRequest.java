package miniJava.CodeGeneration;

import mJAM.Machine;
import miniJava.AbstractSyntaxTrees.MethodDecl;

public class UnknownFunctionAddressRequest {
	public int callLine;
	public MethodDecl functionCalled;
	
	public UnknownFunctionAddressRequest (int callLine, MethodDecl functionCalled) {
		this.callLine = callLine;
		this.functionCalled = functionCalled;
	}
	
	public void fix() {
		Machine.patch(callLine, functionCalled.runtimeEntity.displacement);
	}
}
