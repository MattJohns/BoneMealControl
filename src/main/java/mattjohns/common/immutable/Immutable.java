package mattjohns.common.immutable;

/**
 * Base class for all immutables.
 * 
 * Immutables are classes that cannot be modified. All of their fields are set
 * during construction and no changes can be made after that. In order to modify
 * a field an entirely new instance needs to be created.
 * 
 * They allow method chaining which is calling multiple methods for an
 * object on a single line.
 * 
 * All fields should be final and set during construction.
 * 
 * @param <T>
 * The class that is being instantiated and used in code. This is normally a
 * leaf class on the hierarchy tree.
 * 
 * That class type should be passed all the down to all subclasses including
 * this one.
 * 
 * This is needed for with() methods so they can return the proper type.
 */
public class Immutable<T> {
	/**
	 * Pass all fields as parameters in this constructor.
	 * 
	 * Should not do any processing because this needs to be fast for copy
	 * operations.
	 * 
	 * However all validation should be done here (assert keeps it fast for
	 * production).
	 */
	protected Immutable() {
	}

	/**
	 * Make a copy by instantiating a new object.
	 * 
	 * Only the inheriting class should override this. That inheriting class
	 * should also have a concreteCopy() method that accepts its own type.
	 * 
	 * In that way the copy is 'chained' from base class upwards.
	 * 
	 * @param source
	 * The object to copy.
	 * 
	 * @return The newly instantiated copy.
	 */
	protected T concreteCopy(Immutable<?> source) {
		return concreteTo(new Immutable<>());
	}

	/**
	 * Return 'this' of the leaf class. It is always valid despite the unchecked
	 * cast.
	 */
	protected T concreteThis() {
		return concreteTo(this);
	}

	/**
	 * Cast source to leaf type for when you know it won't fail.
	 */
	@SuppressWarnings("unchecked")
	protected <TSource extends Immutable<?>> T concreteTo(TSource superObject) {
		return (T)superObject;
	}
}