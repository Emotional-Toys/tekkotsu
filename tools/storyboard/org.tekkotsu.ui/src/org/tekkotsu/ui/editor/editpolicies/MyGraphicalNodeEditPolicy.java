/*
 * Created on Sep 18, 2004
 */
package org.tekkotsu.ui.editor.editpolicies;


import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;
import org.tekkotsu.ui.editor.model.commands.CreateConnectionCommand;
import org.tekkotsu.ui.editor.model.commands.ReconnectConnectionCommand;
import org.tekkotsu.ui.editor.resources.Debugger;
/**
 * @author asangpet
 */
public class MyGraphicalNodeEditPolicy extends GraphicalNodeEditPolicy {

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#getConnectionCompleteCommand(org.eclipse.gef.requests.CreateConnectionRequest)
	 */
	protected Command getConnectionCompleteCommand(
			CreateConnectionRequest request) {
		CreateConnectionCommand command =
			(CreateConnectionCommand) request.getStartCommand();
		command.setTarget(getHost().getModel());
		command.setContentsModel(getHost().getParent().getModel());
		Debugger.printDebug(Debugger.DEBUG_ALL,getHost().getParent().toString());
		return command;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#getConnectionCreateCommand(org.eclipse.gef.requests.CreateConnectionRequest)
	 */
	protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
		CreateConnectionCommand command = new CreateConnectionCommand();
		command.setConnection(request.getNewObject());
		command.setSource(getHost().getModel());		
		request.setStartCommand(command);
		return command;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#getReconnectTargetCommand(org.eclipse.gef.requests.ReconnectRequest)
	 */
	protected Command getReconnectTargetCommand(ReconnectRequest request) {
		ReconnectConnectionCommand command = new ReconnectConnectionCommand();
		command.setConnectionModel(request.getConnectionEditPart().getModel());
		command.setNewTarget(getHost().getModel());
		return command;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#getReconnectSourceCommand(org.eclipse.gef.requests.ReconnectRequest)
	 */
	protected Command getReconnectSourceCommand(ReconnectRequest request) {
		ReconnectConnectionCommand command = new ReconnectConnectionCommand();
		command.setConnectionModel(request.getConnectionEditPart().getModel());
		command.setNewSource(getHost().getModel());
		return command;		
	}

}
