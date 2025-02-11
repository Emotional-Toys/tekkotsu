/*
 * Created on Nov 19, 2004
 */
package org.tekkotsu.ui.editor;

import java.util.List;

import org.eclipse.draw2d.AnchorListener;
import org.eclipse.draw2d.ArrowLocator;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.ConnectionLocator;
import org.eclipse.draw2d.ConnectionRouter;
import org.eclipse.draw2d.DelegatingLayout;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @author asangpet
 *
 */
public class BezierCurveConnection extends BezierCurve implements Connection,TransitionConnection,
		AnchorListener {

	private ConnectionAnchor startAnchor, endAnchor;
	private ConnectionRouter connectionRouter = ConnectionRouter.NULL;
	private RotatableDecoration startArrow, endArrow;

	{
		setLayoutManager(new DelegatingLayout());
		addPoint(new Point(0, 0));
		addPoint(new Point(100, 100));
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Connection#setPoints(org.eclipse.draw2d.geometry.PointList)
	 */
	public void setPoints(PointList list) {		
		PointList control = new PointList();
		control.addPoint(list.getFirstPoint());
		
		int dx = list.getLastPoint().x - list.getFirstPoint().x;
		int dy = list.getLastPoint().y - list.getFirstPoint().y;
		float ds = (int)Math.sqrt(dx*dx+dy*dy);
		int du = 20;
		
		control.addPoint(list.getMidpoint().getTranslated(Math.round(dy*du/ds),Math.round(-dx*du/ds)));
		
		control.addPoint(list.getLastPoint());
		super.setPoints(control);
	}
	
	/**
	 * Hooks the source and target anchors.
	 * @see Figure#addNotify()
	 */
	public void addNotify() {
		super.addNotify();
		hookSourceAnchor();
		hookTargetAnchor();
	}

	/**
	 * Called by the anchors of this connection when they have moved, revalidating this 
	 * polyline connection.
	 * @param anchor the anchor that moved
	 */
	public void anchorMoved(ConnectionAnchor anchor) {
		revalidate();
	}

	/**
	 * Returns the bounds which holds all the points in this polyline connection. Returns any 
	 * previously existing bounds, else calculates by unioning all the children's
	 * dimensions.
	 * @return the bounds
	 */
	public Rectangle getBounds() {
		if (bounds == null) {
			super.getBounds();
			for (int i = 0; i < getChildren().size(); i++) {
				IFigure child = (IFigure)getChildren().get(i);
				bounds.union(child.getBounds());
			}
		}
		return bounds;
	}

	/**
	 * Returns the <code>ConnectionRouter</code> used to layout this connection. Will not 
	 * return <code>null</code>.
	 * @return this connection's router
	 */
	public ConnectionRouter getConnectionRouter() {
		return connectionRouter;
	}

	/**
	 * Returns this connection's routing constraint from its connection router.  May return 
	 * <code>null</code>.
	 * @return the connection's routing constraint
	 */
	public Object getRoutingConstraint() {
		if (getConnectionRouter() != null)
			return (List)getConnectionRouter().getConstraint(this);
		else
			return null;
	}

	/**
	 * @return the anchor at the start of this polyline connection (may be null)
	 */
	public ConnectionAnchor getSourceAnchor() {
		return startAnchor;
	}

	/**
	 * @return the source decoration (may be null)
	 */
	protected RotatableDecoration getSourceDecoration() {
		return startArrow;
	}

	/**
	 * @return the anchor at the end of this polyline connection (may be null)
	 */
	public ConnectionAnchor getTargetAnchor() {
		return endAnchor;
	}

	/**
	 * @return the target decoration (may be null)
	 * 
	 * @since 2.0
	 */
	protected RotatableDecoration getTargetDecoration() {
		return endArrow;
	}

	private void hookSourceAnchor() {
		if (getSourceAnchor() != null)
			getSourceAnchor().addAnchorListener(this);
	}

	private void hookTargetAnchor() {
		if (getTargetAnchor() != null)
			getTargetAnchor().addAnchorListener(this);
	}

	/**
	 * Layouts this polyline. If the start and end anchors are present, the connection router 
	 * is used to route this, after which it is laid out. It also fires a moved method.
	 */
	public void layout() {
		if (getSourceAnchor() != null && getTargetAnchor() != null)
			getConnectionRouter().route(this);

		Rectangle oldBounds = bounds;
		super.layout();
		bounds = null;
		
		if (!getBounds().contains(oldBounds)) {
			getParent().translateToParent(oldBounds);
			getUpdateManager().addDirtyRegion(getParent(), oldBounds);
		}
		
		repaint();
		fireMoved();
	}

	/**
	 * Called just before the receiver is being removed from its parent. Results in removing 
	 * itself from the connection router.
	 * 
	 * @since 2.0
	 */
	public void removeNotify() {
		unhookSourceAnchor();
		unhookTargetAnchor();
		getConnectionRouter().remove(this);
		super.removeNotify();
	}

	/**
	 * @see org.eclipse.draw2d.IFigure#revalidate()
	 */
	public void revalidate() {
		super.revalidate();
		getConnectionRouter().invalidate(this);
	}

	/**
	 * Sets the connection router which handles the layout of this polyline. Generally set by 
	 * the parent handling the polyline connection.
	 * @param cr the connection router
	 */
	public void setConnectionRouter(ConnectionRouter cr) {
		if (cr == null)
			cr = ConnectionRouter.NULL;
		if (connectionRouter != cr) {
			connectionRouter.remove(this);
			Object old = connectionRouter;
			connectionRouter = cr;
			firePropertyChange(Connection.PROPERTY_CONNECTION_ROUTER, old, cr);
			revalidate();
		}
	}

	/**
	 * Sets the routing constraint for this connection.
	 * @param cons the constraint
	 */
	public void setRoutingConstraint(Object cons) {
		if (getConnectionRouter() != null)
			getConnectionRouter().setConstraint(this, cons);
		revalidate();
	}

	/**
	 * Sets the anchor to be used at the start of this polyline connection.
	 * @param anchor the new source anchor
	 */
	public void setSourceAnchor(ConnectionAnchor anchor) {
		unhookSourceAnchor();
		//No longer needed, revalidate does this.
		//getConnectionRouter().invalidate(this);
		startAnchor = anchor;
		if (getParent() != null)
			hookSourceAnchor();
		revalidate(); 
	}

	/**
	 * Sets the decoration to be used at the start of the {@link Connection}.
	 * @param dec the new source decoration
	 * @since 2.0
	 */
	public void setSourceDecoration(RotatableDecoration dec) {
		if (getSourceDecoration() != null)
			remove(getSourceDecoration());
		startArrow = dec;
		if (dec != null)
			add(dec, new ArrowLocator(this, ConnectionLocator.SOURCE));
	}

	/**
	 * Sets the anchor to be used at the end of the polyline connection. Removes this listener 
	 * from the old anchor and adds it to the new anchor.
	 * @param anchor the new target anchor
	 */
	public void setTargetAnchor(ConnectionAnchor anchor) {
		unhookTargetAnchor();
		//No longer needed, revalidate does this.
		//getConnectionRouter().invalidate(this);
		endAnchor = anchor;
		if (getParent() != null)
			hookTargetAnchor();
		revalidate();
	}

	/**
	 * Sets the decoration to be used at the end of the {@link Connection}.
	 * @param dec the new target decoration
	 */
	public void setTargetDecoration(RotatableDecoration dec) {
		if (getTargetDecoration() != null)
			remove(getTargetDecoration());
		endArrow = dec;
		if (dec != null)
			add(dec, new ArrowLocator(this, ConnectionLocator.TARGET));
	}

	private void unhookSourceAnchor() {
		if (getSourceAnchor() != null)
			getSourceAnchor().removeAnchorListener(this);
	}

	private void unhookTargetAnchor() {
		if (getTargetAnchor() != null)
			getTargetAnchor().removeAnchorListener(this);
	}



}
