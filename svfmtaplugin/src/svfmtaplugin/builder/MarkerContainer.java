package svfmtaplugin.builder;

import org.eclipse.core.resources.IMarker;

public class MarkerContainer {

		IMarker firstLine;
		IMarker secondLine;
		
		IMarker getFirstMarker() {
			return firstLine;
		}
		IMarker getSecondMarker() {
			return secondLine;
		}
		void setFirstMarker(IMarker marker) {
			firstLine = marker;
		}
		void setSecondMarker(IMarker marker) {
			secondLine = marker;
		}
		
}
