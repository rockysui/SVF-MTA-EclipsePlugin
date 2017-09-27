package svfmtaplugin.builder;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
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
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class SampleBuilder extends IncrementalProjectBuilder {
	ArrayList<MarkerContainer> markerHolders;
	int xxx;
	int x;
	class SampleDeltaVisitor implements IResourceDeltaVisitor {
		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.core.resources.IResourceDeltaVisitor#visit(org.eclipse.core.resources.IResourceDelta)
		 */
		//to make a button, I don't know how to access the files.
		public boolean visit(IResourceDelta delta) throws CoreException {
			IResource resource = delta.getResource();
System.out.println("readingerrs");
				readErrors(resource);	


			switch (delta.getKind()) {
			case IResourceDelta.ADDED:
				// handle added resource
				
//maybs donno, how'd that work... but k lets see
				annotateErrors(resource);
				break;
			case IResourceDelta.REMOVED:
				// handle removed resource
				System.out.println("Im removing thins?");
				removeAnnotation((IFile) resource);
				//clean(null);
				break;
			case IResourceDelta.CHANGED:
				// handle changed resource
				//checkXML(resource);
			//	checkwords(resource);
				annotateErrors(resource);
				System.out.println("Im changing thins?");
				//deleteMarkers((IFile) resource);
				break;
			}
			//return true to continue visiting children.
			return true;
		}
	}

	class SampleResourceVisitor implements IResourceVisitor {
		public boolean visit(IResource resource) {
			//checkXML(resource);
			annotateErrors(resource);
			//return true to continue visiting children.
			return true;
		}
	}

	public static final String BUILDER_ID = "svfmtaplugin.sampleBuilder";

	private static final String MARKER_TYPE = "svfmtaplugin.xmlProblem";

	private SAXParserFactory parserFactory;

	private IMarker addMarker(IFile file, String message, int lineNumber,
			int severity) {
		IMarker marker = null;
		try {
			marker = file.createMarker(MARKER_TYPE);
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

	private void modifyMarker(IFile file, String message, int severity) {
		
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.internal.events.InternalBuilder#build(int,
	 *      java.util.Map, org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor)
	
			throws CoreException {


		if (kind == FULL_BUILD) {
			System.out.println("full built");
			fullBuild(monitor);
		} else {
			IResourceDelta delta = getDelta(getProject());
			if (delta == null) {
				fullBuild(monitor);
			} else {
				System.out.println("incre built");

				incrementalBuild(delta, monitor);
			}
		}
		return null;
	}

	protected void clean(IProgressMonitor monitor) throws CoreException {
		// delete markers set and files created
		getProject().deleteMarkers(MARKER_TYPE, true, IResource.DEPTH_INFINITE);
	}


//run the tool everytime something is changed? for single files. 
// i think give them the option to unmark something and then when they think theyve fixed everything run the tool
	// and =if its not fixed then theyll see it again
	//if they change it, it'll annotate things that may not be there, cause output.txt
	//they change something, remove it all?
	//maybe it shouldnt annotate markers everytime u save but only once or allow them to press abbutton to anotate
	//kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk 
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

	private SAXParser getParser() throws ParserConfigurationException,
			SAXException {
		if (parserFactory == null) {
			parserFactory = SAXParserFactory.newInstance();
		}
		return parserFactory.newSAXParser();
	}

	protected void incrementalBuild(IResourceDelta delta,
			IProgressMonitor monitor) throws CoreException {
		// the visitor does the work.
		delta.accept(new SampleDeltaVisitor());
	}
	
	
	/////////////////////////////////!@#!@#!@#!@#!@#!@#!#!#



	void removeAnnotation(IResource resource) {
		IWorkspace ws = ResourcesPlugin.getWorkspace();
		for(MarkerContainer ec : markerHolders) {
			//this is testing java style programs, so remove the string src/asd (assuming that output and cpp files are in the same thing

//			
//			IPath location = Path.fromOSString(ec.getFirstFile());
//			IFile firstIfile = ws.getRoot().getFileForLocation(location);
//			location = Path.fromOSString(ec.getSecondFile());
//			IFile secondIfile = ws.getRoot().getFileForLocation(location);
//			
//			deleteMarkers(firstIfile);
//			deleteMarkers(secondIfile);	

		}
	}
	
	
	void annotateErrors(IResource resource) {


		IWorkspace ws = ResourcesPlugin.getWorkspace();
		//for(MarkerContainer mc : markerHolders) {
			//this is testing java style programs, so remove the string src/asd (assuming that output and cpp files are in the same thing

			
//			IPath location = Path.fromOSString(ec.getFirstFile());
//			IFile firstIfile = ws.getRoot().getFileForLocation(location);
//			location = Path.fromOSString(ec.getSecondFile());
//			IFile secondIfile = ws.getRoot().getFileForLocation(location);
//			
////			deleteMarkers(firstIfile);
////			deleteMarkers(secondIfile);
////			String fileName = firstIfile.getLocation().toString();
////			System.out.println("first = " + fileName);
////			String sfileName = firstIfile.getLocation().toString();
////			System.out.println("second = " + sfileName);
//			//reads the line from output.txt which doesn't update.
//			addMarker(firstIfile, "Possible alias on line " + ec.getSecondErr() + " in file "+ ec.getSecondFile(), ec.getFirstErr(), 2);
//			addMarker(secondIfile, "Alias followed from line " + ec.getFirstErr() + " in file " + ec.getFirstFile(), ec.getSecondErr(), 2);


		//}

	}

	//need to read file somehow.
	//populate the array with data from the file
	//TODO 
	void readErrors(IResource resource) throws CoreException {
		try {
			
			//gets the output.txt from the java project, regardless if it exists

			//gets the string of the path
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
			Scanner sc = new Scanner(new FileReader(file));
			while(sc.hasNext()) {
				String firstErrLine = sc.nextLine();
				String firstFile = sc.nextLine();
				String secondErrLine = sc.nextLine();
				String secondFile = sc.nextLine();
				
				//can remove this line.
				//ecs.add(e);
				IPath location = Path.fromOSString(firstFile);
				IFile firstIfile = ws.getRoot().getFileForLocation(location);
				location = Path.fromOSString(secondFile);
				IFile secondIfile = ws.getRoot().getFileForLocation(location);
				

//				String fileName = firstIfile.getLocation().toString();
//				System.out.println("first = " + fileName);
//				String sfileName = firstIfile.getLocation().toString();
//				System.out.println("second = " + sfileName);
				//reads the line from output.txt which doesn't update.
				//TODO !@#!#!@#!@#!@#@#!#!#!@#!@#!@#@!!@@!#@!#!@#
				//Need to add this marker into the markerHolder, so everytime we can update the line numbers
				MarkerContainer mc = new MarkerContainer();

				String msg = "Possible alias on line " + secondErrLine + " in file "+ secondFile;
				IMarker firstMark = addMarker(firstIfile, msg, Integer.parseInt(firstErrLine), 2);
				msg = "Alias followed from line " + firstErrLine + " in file " + firstFile;
				IMarker secondMark = addMarker(secondIfile, msg, Integer.parseInt(secondErrLine), 2);
				if(firstMark != null && secondMark != null) {
					mc.setFirstMarker(firstMark);
					mc.setSecondMarker(secondMark);
					markerHolders.add(mc);
				}

			}
			sc.close();
			System.out.println("Im tryna delete stuff here");
			boolean xd = file.delete();

			if(xd) {
				System.out.println("detelet");
			} else {
				System.out.println("you got adis");
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	

	
}
