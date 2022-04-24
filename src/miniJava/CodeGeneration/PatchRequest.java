package miniJava.CodeGeneration;

import mJAM.Machine;
import miniJava.AbstractSyntaxTrees.MethodDecl;

/*
 * When generating code, it is frequently the case that we are at a Call Statement/Expression where
 * we are attempting to call a function that we have not yet generated code for. 
 * 
 * The addresses of all functions cannot be known until the code generation has finished. Thus, while 
 * generating code, whenever we encounter a function call we assume the address is unknown and create a PatchRequest
 * and add it to the Collection<PatchRequest> in the Generator class, and then jump to a random 
 * address for the time being. 
 * 
 * At the end of code generation, we can come back and replace the
 * random address we initially jumped to with the now known address of the target function.
 */
public class PatchRequest {
	// Line Number of machine code where the function is called
	public int callLine;
	
	// Function that code is attempting to call at line callLine
	public MethodDecl functionCalled;

	public PatchRequest(int callLine, MethodDecl functionCalled) {
		this.callLine = callLine;
		this.functionCalled = functionCalled;
	}

	// Patches call address once method address is known
	public void fix() {
		Machine.patch(callLine, functionCalled.runtimeEntity.displacement);
	}
}
