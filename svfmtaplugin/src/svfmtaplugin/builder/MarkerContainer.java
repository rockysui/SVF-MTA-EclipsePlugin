package svfmtaplugin.builder;

import org.eclipse.core.resources.IMarker;

public class MarkerContainer {

		IMarker firstLine;
		IMarker secondLine;
		String firstFile;
		String secondFile;
		
		IMarker getFirstMarker() {
			return firstLine;
		}
		IMarker getSecondMarker() {
			return secondLine;
		}
		
		String getFirstFile() {
			return firstFile;
		}
		
		String getSecondFile() {
			return secondFile;
		}
		void setFirstMarker(IMarker marker, String file) {
			firstLine = marker;
			firstFile = file;
		}
		void setSecondMarker(IMarker marker, String file) {
			secondLine = marker;
			secondFile = file;
		}
		
		
		
		
		
}
