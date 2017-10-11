package svfmtaplugin.builder;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

public class QuickFix implements IMarkerResolution {
	String label;
	IMarker otherMarker;
	int lineNumber;
	String file;
	
	QuickFix(String str, int ln, String f) {
		label = str;
		lineNumber = ln;
		this.file = f;
		
	}
	
	QuickFix(String str) {
		label = str;
	}
	void setLabel(String label) {
		this.label = label;
	}
	
	@Override	
	public String getLabel() {
		// TODO Auto-generated method stub
		return label;
	}

	@Override
	public void run(IMarker arg0) {

		IWorkspace ws = ResourcesPlugin.getWorkspace();
		IPath location = Path.fromOSString(file);
		IFile firstIfile = ws.getRoot().getFileForLocation(location);
		
		
		IMarker tmp = addMarker(firstIfile, "tmp", lineNumber, 1);
		System.out.println("implemented");
		IWorkbenchPage iwp = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		try {
			if(tmp != null && tmp.exists()) {
				IDE.openEditor(iwp, tmp);
			} else {
			}
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			tmp.delete();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		
	}
	private static final String MARKER_TYPE = "svfmtaplugin.DataRaceProblem";

	private IMarker addMarker(IFile file, String message, int lineNumber,
			int severity) {
		IMarker marker = null;
		try {
			marker = file.createMarker(MARKER_TYPE);
			marker.setAttribute(IMarker.USER_EDITABLE, true);
			marker.setAttribute(IMarker.MESSAGE, message);
			marker.setAttribute(IMarker.SEVERITY, severity);

			if (lineNumber == -1) {
				lineNumber = 1;
			}
			marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
			
		} catch (CoreException e) {
		}
		return marker;
	}
	
}
