package svfmtaplugin.builder;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IMarkerResolution;

public class DeleteMarker  implements IMarkerResolution {

	String label;
	DeleteMarker(String l) {
		label = l;
	}
	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return label;
	}

	@Override
	public void run(IMarker arg0) {
		// TODO Auto-generated method stub
		try {
			arg0.delete();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
