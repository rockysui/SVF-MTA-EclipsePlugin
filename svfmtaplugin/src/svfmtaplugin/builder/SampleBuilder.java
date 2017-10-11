package svfmtaplugin.builder;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IMarkerResolution;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class SampleBuilder extends IncrementalProjectBuilder {
	ArrayList<MarkerContainer> markerHolders;

	class SampleDeltaVisitor implements IResourceDeltaVisitor {
		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.core.resources.IResourceDeltaVisitor#visit(org.eclipse.core.resources.IResourceDelta)
		 */
		//to make a button, I don't know how to access the files.
		public boolean visit(IResourceDelta delta) throws CoreException {
			IResource resource = delta.getResource();
			readErrors(resource);	
			
			switch (delta.getKind()) {
			case IResourceDelta.ADDED:
				// handle added resource
				annotateErrors(resource);
				break;
			case IResourceDelta.REMOVED:
				// handle removed resource
				break;
			case IResourceDelta.CHANGED:
				changeError(delta);
				annotateErrors(resource);
				break;
			}
			//return true to continue visiting children.
			return true;
		}
	}

	class SampleResourceVisitor implements IResourceVisitor {
		public boolean visit(IResource resource) {

			return true;
		}
	}

	public static final String BUILDER_ID = "svfmtaplugin.sampleBuilder";

	private static final String MARKER_TYPE = "svfmtaplugin.DataRaceProblem";

	private SAXParserFactory parserFactory;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.internal.events.InternalBuilder#build(int,
	 *      java.util.Map, org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor) throws CoreException {

		if (kind == FULL_BUILD) {
			fullBuild(monitor);
		} else {
			IResourceDelta delta = getDelta(getProject());
			if (delta == null) {
				fullBuild(monitor);
			} else {
				incrementalBuild(delta, monitor);
			}
		}
		return null;
	}

	protected void clean(IProgressMonitor monitor) throws CoreException {
		// delete markers set and files created
		getProject().deleteMarkers(MARKER_TYPE, true, IResource.DEPTH_INFINITE);
	}


	private void deleteMarkers(IFile file) {
		try {
			file.deleteMarkers(MARKER_TYPE, false, IResource.DEPTH_ZERO);
		} catch (CoreException ce) {
		}
	}

	protected void fullBuild(final IProgressMonitor monitor)
			throws CoreException {


		try {
			getProject().accept(new SampleResourceVisitor());
		} catch (CoreException e) {
		}
	}

	protected void incrementalBuild(IResourceDelta delta,
			IProgressMonitor monitor) throws CoreException {
		// the visitor does the work.
		delta.accept(new SampleDeltaVisitor());
	}
	
	
	/////////////////////////////////!@#!@#!@#!@#!@#!@#!#!#

	
	void changeErrors(IResource resource) throws CoreException {
		resource.deleteMarkers(MARKER_TYPE, false, IResource.DEPTH_ZERO);
	}
	void changeError(IResourceDelta resourceDelta) throws	CoreException {
		IMarkerDelta[] imd = resourceDelta.getMarkerDeltas(); 
		System.out.println(imd + " size = " + imd.length);
		for(IMarkerDelta m : imd) {
			System.out.println("printing imarker");
			IMarker n = m.getMarker();

			if(n.exists()) {
				System.out.println(n.getAttribute(IMarker.MESSAGE));	
			}		
		}
	}
	
	//Annotate errors needs to run everytime something occurs in the resource to make it change
	void annotateErrors(IResource resource) throws CoreException {
		if(markerHolders != null) {
			for(MarkerContainer mc : markerHolders) {
				IMarker firstMarker = null;
				IMarker secondMarker = null;
				if(mc.getFirstMarker() != null && mc.getFirstMarker().exists()) {
					firstMarker = mc.getFirstMarker();					
				} else {
					if(mc.getSecondMarker()!= null && mc.getSecondMarker().exists()) {
						mc.getSecondMarker().delete();
					}
					return;
					//delete this mc, since they've changed the resource and we have no way of knowing
					//if it fixed until they run mta again
					//then continue
				}
				
				if(mc.getSecondMarker()!= null && mc.getSecondMarker().exists()) {
					secondMarker = mc.getSecondMarker();
				} else {
					//this marker doesn't exist, so delete its partner
					if(mc.getFirstMarker() != null && mc.getFirstMarker().exists()) {
						firstMarker.delete();
					}
					return;
					//same as above
				}
				int firstErr = (int) firstMarker.getAttribute(IMarker.LINE_NUMBER);
				int secondErr = (int) secondMarker.getAttribute(IMarker.LINE_NUMBER);
				//TODO need to get the file names somehow
				String msg = "Possible alias on line " + secondErr + " in file " + mc.getSecondFile();;
				firstMarker.setAttribute(IMarker.MESSAGE, msg);
				msg = "Alias followed from line " + firstErr + " in file " + mc.getFirstFile();
				secondMarker.setAttribute(IMarker.MESSAGE, msg);
				System.out.println("dude im in here");
				QuickFixer qf = new QuickFixer();
				qf.setOtherMarker(secondMarker);
				qf.getResolutions(firstMarker);
				QuickFixer qff = new QuickFixer();
				qff.setOtherMarker(firstMarker);
				qff.getResolutions(secondMarker);
				System.out.println("out of scope)");
			}
		}
	}

	//need to read file somehow.
	//populate the array with data from the file
	//TODO 
	void readErrors(IResource resource) throws CoreException {
		try {
			
			//gets the output.txt from the java project, regardless if it exists
			String x = resource.getProject().getLocation().toString();
			x = x + "/output.txt"; //need to be sure that this is the path...but linux.
			//creates a file class
			File file = new File(x);
			
			//now does the check if it exists
			if(!file.exists()) {
				//do nothing cause file doesn't exist
				//reading file
				return;
			}
			clean(null); //remove all previous errors
			IWorkspace ws = ResourcesPlugin.getWorkspace();
			markerHolders = new ArrayList<MarkerContainer>();
			//allows scanner to read from the file
			String pattern = "^\\.\\.\\/";
			Pattern patt = Pattern.compile(pattern);
			System.out.println(patt);
			x = resource.getProject().getLocation().toString();

			Scanner sc = new Scanner(new FileReader(file));
			while(sc.hasNext()) {
				String firstErrLine = sc.nextLine();
				String firstFile = sc.nextLine();
				String secondErrLine = sc.nextLine();
				String secondFile = sc.nextLine();
	
				//need to regex ../ lines, for relative paths
				Matcher m = patt.matcher(firstFile);
				if(m.find()) {
					System.out.println("replaced");
					firstFile = firstFile.replaceAll("^..", x);
				}

				Matcher n = patt.matcher(secondFile);
				if(n.find()) {
					secondFile = secondFile.replaceAll("^..", x);
					System.out.println(secondFile);
				}		
				
				//If i get full path, then I just need this
				IPath location = Path.fromOSString(firstFile);
				IFile firstIfile = ws.getRoot().getFileForLocation(location);
				location = Path.fromOSString(secondFile);
				IFile secondIfile = ws.getRoot().getFileForLocation(location);
				
				MarkerContainer mc = new MarkerContainer();

				String msg = "Possible alias on line " + secondErrLine + " in file "+ secondFile;
				IMarker firstMark = addMarker(firstIfile, msg, Integer.parseInt(firstErrLine), 1);
				msg = "Alias followed from line " + firstErrLine + " in file " + firstFile;
				IMarker secondMark = addMarker(secondIfile, msg, Integer.parseInt(secondErrLine), 1);
				if(firstMark != null && secondMark != null) {
					mc.setFirstMarker(firstMark, firstFile);
					mc.setSecondMarker(secondMark, secondFile);
					markerHolders.add(mc);
				}
			}
			sc.close();
			file.delete();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
