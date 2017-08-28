package svfmtaplugin.handlers;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.jface.dialogs.MessageDialog;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class BCHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public BCHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		//This is a test
		//this is where you place the exe
	

		try {
			String mtaDest = "/home/a/git/SVF-MTA-Eclipseplugin/svfmtaplugin/folder/mta";
			String mtaArg = "-mhp";
			String bcFile = "/home/a/git/SVF-MTA-Eclipseplugin/svfmtaplugin/folder/example.bc";
			File destination = new File("/home/a/git/SVF-MTA-Eclipseplugin/svfmtaplugin/folder/");
			ProcessBuilder runMTA = new ProcessBuilder(mtaDest, mtaArg, bcFile);
			Process process = runMTA.start();
			OutputStream processOutput = process.getOutputStream();
			
			System.out.println(processOutput);
			//this is the created file, just need to create a file, dump this stream inside it and we'll have an outut.txt
			//for the build handler

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
}
