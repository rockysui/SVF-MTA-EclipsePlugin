package svfmtaplugin.builder;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.ui.text.java.IInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jdt.ui.text.java.IProblemLocation;
import org.eclipse.jdt.ui.text.java.IQuickFixProcessor;

public class MyQuickFixProcessor implements IQuickFixProcessor {

	@Override
	public IJavaCompletionProposal[] getCorrections(IInvocationContext arg0, IProblemLocation[] arg1)
			throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasCorrections(ICompilationUnit arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}


}
