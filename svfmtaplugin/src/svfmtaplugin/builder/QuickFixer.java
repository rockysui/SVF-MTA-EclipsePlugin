package svfmtaplugin.builder;

import org.eclipse.core.resources.IMarker;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolutionGenerator;

public class QuickFixer implements IMarkerResolutionGenerator {

	@Override
	public IMarkerResolution[] getResolutions(IMarker arg0) {
		// TODO Auto-generated method stub
		return new IMarkerResolution[] {
				new QuickFix("lololol")
		};
	}

}
