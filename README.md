# Extensions Using Lifecycle Callback

### Navigable
> Navigate directly using:

> - Fragment class `FirstFragment.navigate()`
> - Activity class `MainActivity.navigate()`
> - Your own Navigation Utils `NavigationUtils.main.navigate()`

`Application` Initialization:

```kotlin
Navigable.init(this)
```
> **Navigable** use container for navigation
> **Activity** have to be empty if `navigableContainerId ` is not set

> **NavigableActivity** implement `onNavigate ` & `onNavigateTransaction ` for custom animation ect..

```kotlin
class MainActivity: LifecycleActivity(), NavigableActivity {

	// If not set: Will use activity container /!\ only empty activty !
	override val navigableContainerId: Int = R.id.fragment_container
	
	// Register all Lifecycle Extensions when implementing them
	override fun registerLifecycleCallback() {
		NavigableActivity.register(this)
	}
}
```

> Implement `Navigable` in your, `Activity `, `Fragment ` or **Custom Class** 

```kotlin
companion object: Navigable {
   override val activityAffinity = MainActivity::class.java
   override val fragmentClass = FirstFragment::class.java

	// navigate is inside Navigable
 	navigate(clearBackStack: Boolean, args: Bundle)
}

```
```kotlin
companion object {
	val main = object: Navigable {
		override val activityAffinity = MainActivity::class.java
		override val fragmentClass = FirstFragment::class.java

		// navigate is inside Navigable
 		navigate(clearBackStack: Boolean, args: Bundle)
}

```


### ShareableElement

> `NavigableActivity ` can deals with **SharedElements**
> Just implement `ShareableElement` in your `Fragment` and it's work !

```kotlin
class FirstFragment: Fragment(), ShareableElement {

	override val sharedElements: ArrayList<View?>? 
		get() = arrayListOf(thumbnailImageView, titleTextView, ...)
		
	override val sharedElementEnterTransitionId = ShareableElement.Transtion.NONE R.transition.custom_transition
	override val sharedElementReturnTransitionId = R.transition.custom_transition
}
```

SharedElement could be between:

- `Fragment1` (Activity1) **~>** `Fragment2` (Activity1)
- `Fragment1` (Activity1) **~>** `Fragment2` (Activity2) 
- `Fragment1` (Activity) **~>** `Activity` & `Fragment2` (Activity) 

Shared Transition Field:
`sharedElementEnterTransitionId ` and `sharedElementReturnTransitionId `

> - Not override **~>** default transition
> - 0 **~>** No Transition
> - Value **~>** Transition
> - Null **~>** `NavigableActivity` Transition / No Transition

> default transition can be also override
`Navigable`.defautlSharedElementEnterTransitionId = 0
`Navigable`.defautlSharedElementReturnTransitionId = 0

### AutoLayout
> Automaticly load layout based on ClassName
- `MainActivity` **~>** `activity_main`
- `SecondDetailFragment` **~>** `fragment_second_detail`



```kotlin
class MainActivity: LifecycleActivity, AutoLayout {
====================================================
class FirstFragment: LifecycleFragment, AutoLayout {

	override fun registerLifecycleCallback() {
		AutoLayout.register(this)
	}
}
```

-> Or override defined `layoutId`

```kotlin
override val layoutId: Int = R.layout.activity_custom_main
```