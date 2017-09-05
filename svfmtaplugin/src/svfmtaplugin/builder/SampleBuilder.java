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
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class SampleBuilder extends IncrementalProjectBuilder {
	
	int xxx;
	int x;
	class SampleDeltaVisitor implements IResourceDeltaVisitor {
		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.core.resources.IResourceDeltaVisitor#visit(org.eclipse.core.resources.IResourceDelta)
		 */
		public boolean visit(IResourceDelta delta) throws CoreException {
			IResource resource = delta.getResource();
			if(xxx == 0) {
				readErrors(resource);				
			}

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

	private void addMarker(IFile file, String message, int lineNumber,
			int severity) {
		try {
			IMarker marker = file.createMarker(MARKER_TYPE);
			marker.setAttribute(IMarker.MESSAGE, message);
			marker.setAttribute(IMarker.SEVERITY, severity);
			if (lineNumber == -1) {
				lineNumber = 1;
			}
			marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
		} catch (CoreException e) {
		}
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

	ArrayList<ErrorContainer> ecs = new ArrayList<ErrorContainer>();

	
	//create class for the population of errors
	class ErrorContainer {
		public String firstFile;
		public String secondFile;
		public int	  firstErrLine;
		public int    secondErrLine;
		public ErrorContainer() {
		}
		public ErrorContainer(String s1, String s2, int err1, int err2) {
			firstFile = s1;
			secondFile = s2;
			firstErrLine = err1;
			secondErrLine = err2;
		}
		
		void setFirstFile(String s) {
			firstFile = s;
		}
	
		void setSecondFile(String s) {
			secondFile = s;
		}
		
		void setFirstErrLine(int x) {
			firstErrLine = x;
		}
		
		void setSecondErrLine(int x) {
			secondErrLine = x;
		}
		
		String getFirstFile() {
			return firstFile;
		}
		
		String getSecondFile() {
			return firstFile;
		}
		
		int getFirstErr() {
			return firstErrLine;
		}
		
		int getSecondErr() {
			return secondErrLine;
		}
		
		@Override
		public boolean equals(Object o) {	
			if(o == null) return false;
			if(!(o instanceof ErrorContainer)) return false;	
			ErrorContainer ec = (ErrorContainer) o;
			if(ec.firstFile == this.firstFile && ec.secondFile == this.secondFile
					&& ec.firstErrLine == this.firstErrLine && ec.secondErrLine == this.secondErrLine) return true;
			
			
			
			return false;
		}
	}

	void removeAnnotation(IResource resource) {
		for(ErrorContainer ec: ecs) {
			IFile firstIfile = this.getProject().getFile("src/" + ec.getFirstFile());
			IFile secondIfile = this.getProject().getFile("src/" + ec.getSecondFile());
			deleteMarkers(firstIfile);
			deleteMarkers(secondIfile);	

		}
	}
	
	
	void annotateErrors(IResource resource) {
		
		for(ErrorContainer ec : ecs) {
			//this is testing java style programs, so remove the string src/asd (assuming that output and cpp files are in the same thing
			
			IFile firstIfile = this.getProject().getFile("src/" + ec.getFirstFile());
			IFile secondIfile = this.getProject().getFile("src/" + ec.getSecondFile());
//			deleteMarkers(firstIfile);
//			deleteMarkers(secondIfile);
//			String fileName = firstIfile.getLocation().toString();
//			System.out.println("first = " + fileName);
//			String sfileName = firstIfile.getLocation().toString();
//			System.out.println("second = " + sfileName);
			//reads the line from output.txt which doesn't update.
			addMarker(firstIfile, "Possible alias on line " + ec.getSecondErr() + " in file "+ ec.getSecondFile(), ec.getFirstErr(), 2);
			addMarker(secondIfile, "Alias followed from line " + ec.getFirstErr() + " in file " + ec.getFirstFile(), ec.getSecondErr(), 2);


		}
		ecs.clear();

	}

	//need to read file somehow.
	//populate the array with data from the file
	//TODO 
	void readErrors(IResource resource) {
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
				System.out.println("Im here, but should not be");
				return;
			}
			//allows scanner to read from the file
			Scanner sc = new Scanner(new FileReader(file));
			while(sc.hasNext()) {
				String err1 = sc.nextLine();
				String s1 = sc.nextLine();
				String err2 = sc.nextLine();
				String s2 = sc.nextLine();
				
				ErrorContainer e = new ErrorContainer(s1, s2, Integer.parseInt(err1), Integer.parseInt(err2));
				ecs.add(e);
			}
			sc.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	

	
}
