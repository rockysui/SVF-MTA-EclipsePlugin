package svfmtaplugin.builder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolutionGenerator;

public class QuickFixer implements IMarkerResolutionGenerator {
	
	IMarker otherErr;

	void setOtherMarker(IMarker oth) throws CoreException {
		otherErr = oth;
		System.out.println(otherErr.getAttribute(IMarker.MESSAGE));
	}
	
	@Override
	public IMarkerResolution[] getResolutions(IMarker arg0) {
		// TODO Auto-generated method stub
		String lineNumber = "";
		String file = "";
		String s = "";
		
		String firstOne = "Possible alias on line (.*) in file (.*)";
		String secondOne = "Alias followed from line (.*) in file (.*)";
		Pattern pOne = Pattern.compile(firstOne);
		Pattern pTwo = Pattern.compile(secondOne);
		try {
			s = (String) arg0.getAttribute(IMarker.MESSAGE);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Matcher mOne = pOne.matcher(s);
		
		if(mOne.find()) {
			lineNumber = mOne.group(1);
			file = mOne.group(2);
		} else {
			Matcher mTwo = pTwo.matcher(s);	
			if(mTwo.find()) {
				lineNumber = mTwo.group(1);
				file = mTwo.group(2);
			}
		}
	

		
		String xd = "Go to error on line " + lineNumber + " in file " + file;
		return new IMarkerResolution[] {
				new QuickFix(xd, Integer.parseInt(lineNumber), file),
				new DeleteMarker("Delete this marker and marker on " + lineNumber + " in file " + file)
		};
	}
}
