package mattjohns.common.immutable.userinterface.display;

import mattjohns.common.immutable.Immutable;

/**
 * Display and rendering information for a programming model (i.e. done with
 * immutable classes).
 * 
 * Models has several display components that represent each of the graphical
 * elements. The caller's non-model screen class can use that information to
 * work out rendering positions etc.. That way the screen class can be like a
 * 'dumb terminal' and just handle rendering and user input. The model does all
 * the logic.
 * 
 * Models are 'pure' and abstract in that they don't deal with operating
 * specific things like graphic pipelines. So these GUI models represent an
 * abstract layout space and should only include the appropriate information.
 * For example, display components should not call OpenGL drawRectangle() or
 * deal with system fonts.
 * 
 * A component is 'stale' when its display information is out of sync with the
 * actual screen state. When a caller renders a component they should ensure the
 * information is updated if necessary (and reset the 'stale' flag). That way
 * all display components are lazily calculated.
 *
 * @param <TConcrete>
 * The class that is inheriting from this base class.
 */
public abstract class DisplayComponent<TConcrete extends DisplayComponent<TConcrete>> extends Immutable<TConcrete> {
	/**
	 * Component is stale and needs updating before rendering. Does not indicate
	 * anything about child or parent components.
	 * 
	 * Ensure this gets set back to false when the component gets updated.
	 */
	public final boolean isStale;

	/**
	 * Display this component during rendering. For example TextArea model sets
	 * this to false when its text cursor is scrolled off-screen.
	 */
	public final boolean isVisible;

	/**
	 * Position relative to container.
	 * 
	 * Note DisplayBound always has a positive position and a non zero, positive
	 * size.
	 */
	public final DisplayBound bound;

	/**
	 * Position of container. Should be manually updated whenever the container
	 * position changes.
	 */
	public final DisplayPosition containerPosition;

	protected DisplayComponent(boolean isStale, boolean isVisible, DisplayBound bound,
			DisplayPosition containerPosition) {
		this.isStale = isStale;
		this.isVisible = isVisible;
		this.bound = bound;
		this.containerPosition = containerPosition;

		assert this.bound != null;
		assert this.containerPosition != null;
	}

	protected abstract TConcrete copy(boolean isStale, boolean isVisible, DisplayBound bound,
			DisplayPosition containerPosition);

	@Override
	protected TConcrete concreteCopy(Immutable<?> source) {
		return copy(isStale, isVisible, bound, containerPosition);
	}

	public TConcrete withIsStale(boolean isStale) {
		return copy(isStale, isVisible, bound, containerPosition);
	}

	public TConcrete withSetStale() {
		return copy(true, isVisible, bound, containerPosition);
	}

	public TConcrete withIsVisible(boolean isVisible) {
		return copy(isStale, isVisible, bound, containerPosition);
	}

	public TConcrete withBound(DisplayBound bound) {
		return copy(isStale, isVisible, bound, containerPosition);
	}

	public TConcrete withContainerPosition(DisplayPosition containerPosition) {
		return copy(isStale, isVisible, bound, containerPosition);
	}

	/**
	 * Takes all parent containers into account. But containerPosition needs to
	 * be updated any time the parent container changes position.
	 */
	public DisplayPosition positionAbsolute() {
		return bound.topLeft.withTranslate(containerPosition);
	}

	public DisplaySize size() {
		return bound.size();
	}

	public DisplayBound boundAbsolute() {
		return bound.withTranslate(containerPosition);
	}
}
