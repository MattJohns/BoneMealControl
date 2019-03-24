package mattjohns.minecraft.common.userinterface.control.system;

import java.util.function.Consumer;
import java.util.function.Function;

import mattjohns.common.immutable.list.ListImmutableInteger;
import mattjohns.common.immutable.userinterface.display.DisplayBound;
import mattjohns.common.immutable.userinterface.display.DisplayPosition;
import mattjohns.common.immutable.userinterface.display.DisplaySize;
import mattjohns.minecraft.common.userinterface.device.DeviceSet;
import mattjohns.minecraft.common.userinterface.style.StyleMapContainer;

public class Container<T extends Container<T>> extends Component<T> {

	public final StyleMapContainer containerStyleMap;

	// mutable
	public final IdSupply idSupply;

	protected Container(Component<?> superObject, StyleMapContainer containerStyleMap, IdSupply idSupply) {
		super(superObject, superObject.parentAbsoluteBound);

		this.containerStyleMap = containerStyleMap;
		this.idSupply = idSupply;
	}

	public static Container<?> of(IdSupply idSupply, DeviceSet deviceSet, StyleMapContainer containerStyleMap) {

		Component<?> component = Component.ofComponent(idSupply.get(), deviceSet, containerStyleMap.control);

		return new Container<>(component, containerStyleMap, idSupply);
	}

	protected T concreteCopy(Container<?> source) {
		return concreteTo(new Container<>(source, source.containerStyleMap, source.idSupply));
	}

	@Override
	protected T concreteCopy(Component<?> source) {
		return concreteCopy(new Container<>(source, containerStyleMap, idSupply));
	}

	// clearing done by caller
	protected T withComponentFunctionAndSave(int id, Function<Component<?>, Component<?>> mainFunction) {
		assert componentList().idIsContain(id) : "Attempt to save component that doesn't already exist in container.";

		// switch id to get cast object

		// handle events

		// clear events
		// Component<?> newComponent = component.withEventClear();

		// save

		return concreteThis();
	}

	// exclusive
	protected T withComponentForEachFunctionAndSave(Function<Component<?>, Component<?>> mainFunction) {
		T result = concreteThis();

		for (Component<?> component : componentList()) {
			result = result.withComponentFunctionAndSave(component.id, mainFunction);
		}

		return result;
	}

	@Override
	protected T withDimensionConsumeChange() {
		/// send to components?

		T result = super.withDimensionConsumeChange();

		// retrigger parent bound update

		//// not elegant

		result = result.withParentAbsoluteBound(parentAbsoluteBound);

		return result;
	}

	@Override
	public T withInputMouseConsumeClickDown(DisplayPosition mousePosition, int mouseButton) {
		T result = super.withInputMouseConsumeClickDown(mousePosition, mouseButton);

		result = result.withComponentForEachFunctionAndSave(x -> {
			Component<?> c1 = x.withInputMouseConsumeClickDown(mousePosition, mouseButton);

			return c1;
		});

		// override and do custom code here for specific control type

		return result;
	}

	@Override
	public T withParentAbsoluteBound(DisplayBound parentAbsoluteBound) {
		T result = super.withParentAbsoluteBound(parentAbsoluteBound);

		result = result.withComponentForEachFunctionAndSave(x -> {
			return x.withParentAbsoluteBound(this.boundAbsolute());
		});

		return result;
	}

	@Override
	public T withComponentLayout() {
		T result = withComponentPrepareLayout();

		result = result.withComponentForEachFunctionAndSave(x -> {
			Component<?> c1 = x.withComponentLayout();

			return c1;
		});

		// override and do custom code here for specific control type

		return result;
	}

	@Override
	public void render(DisplayPosition positionOffset) {
		super.render(positionOffset);
	}

	protected int idConsume() {
		return idSupply.get();
	}

	protected ComponentList componentList() {
		return ComponentList.of();
	}

	protected ComponentList componentForEachFunction(Function<Component<?>, Component<?>> function) {
		ComponentList.Builder componentBuilder = ComponentList.Builder.of();

		for (Component<?> component : componentList()) {
			Component<?> newComponent = function.apply(component);

			componentBuilder.add(newComponent);
		}

		return componentBuilder.build();

	}

	protected void componentListLayout() {
		// for (Component<?> component : componentList) {
		// component.componentLayout();
		// }
	}

	public boolean componentIsEmpty() {
		return componentList().isEmpty();
	}

	public int componentListSize() {
		return componentList().size();
	}

	public Component<?> componentGet(int index) {
		return componentList().get(index);
	}

	/**
	 * Starts at top left.
	 */
	protected DisplaySize componentBoundingBox() {
		// exclusive
		int right = 1;
		int bottom = 1;

		for (Component<?> component : componentList()) {
			if (component.bound()
					.rightExclusive() > right) {
				right = component.bound()
						.rightExclusive();
			}

			if (component.bound()
					.bottomExclusive() > bottom) {
				bottom = component.bound()
						.bottomExclusive();
			}
		}

		return DisplaySize.of(right, bottom);
	}

	// every recursive component *not* including this one
	@Override
	public void componentForEachExclusive(Consumer<Component<?>> consume) {
		// super.componentForEachExclusive(consume);

		for (Component<?> component : componentList()) {
			consume.accept(component);

			component.componentForEachExclusive(consume);
		}
	}

	@Override
	public ListImmutableInteger componentIdList() {
		ListImmutableInteger.Builder builder = ListImmutableInteger.Builder.of();

		builder.add(super.componentIdList());

		componentForEachExclusive(x -> builder.add(x.componentIdList()));

		return builder.build();
	}

	@Override
	public ComponentList componentFromId(ListImmutableInteger idList) {
		ComponentList.Builder builder = ComponentList.Builder.of();

		builder.add(super.componentFromId(idList));

		componentForEachExclusive(x -> builder.add(x.componentFromId(idList)));

		return builder.build();
	}

	@Override
	public ListImmutableInteger componentDisplayIdList() {
		ListImmutableInteger.Builder builder = ListImmutableInteger.Builder.of();

		builder.add(super.componentDisplayIdList());

		componentForEachExclusive(x -> builder.add(x.componentDisplayIdList()));

		return builder.build();
	}
}
