package svfmtaplugin.builder;

import org.eclipse.core.resources.IMarker;
import org.eclipse.ui.IMarkerResolution;

public class QuickFix implements IMarkerResolution {
	String label;
	
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
		System.out.println("not yet implemented");
		// TODO Auto-generated method stub
		
	}
	
}
