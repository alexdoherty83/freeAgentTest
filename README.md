# FreeAgent Test Application

Developing this project has been a real eye-opener as to what Google suggests is the recommended way of building Android apps; obliquely referred to as having MAD (Modern Android Developer) Skills.

I opted *not* to use Compose for this project, inspite of Google's insistance that this was the way forward, as it is something I'm completely unfamiliar with.  My knowledge of Kotlin and `ViewModel`s is limited and that's reflected in the time it's taken me to build this project (the recommended time is less than a day).  Also, my knowledge of [Unit Tests](https://developer.android.com/training/testing) and [Dependency Injection](https://developer.android.com/training/dependency-injection) is lacking.

As this is being developed for an Android platform (not necessarily a phone / tablet as unlikely as that will be for this project), the assumption was made to use the back gesture / button and the back affordance on the toolbar in place of adding an actual `Back` button on the comparison screen.

## What Works

The app fufils the brief given; it can successfully download the latest FX rates using the API from fixer.io and use them to convert the entered amount into the appropriate values.  Similarly, tapping the `Compare` button allows the user to compare two (and only two at this time, more on that later) currencies over the last five days.

Responses from the API are being cached; while online, the app will not request an update from the API for *5 minutes*.  If the device is determined to be offline (`HelpfulUtils.hasNetwork` returns `false`), the cached copy will be used.

## What Doesn't

The `RatesListFragment` is firing `onViewCreated()` when the user navigates back from the `ComparisonFragment`.  If I hadn't made use of a `ViewModel` this would have resulted in repeated API calls.  Ideally, the app should not be recreating or reassigning UI elements that already exist.  This was part of the reason the `VerticalSpaceItemDecoration` is no longer added to the `RecyclerView` in `RatesListFragment`; each time the user navigated back from the `ComparisonFragment`, the `ItemDecoration` would be added again despite checking if `SavedInstanceState` was `null`.

## Is It Perfect?

Absolutely not!  :sweat_smile:  There are still many things that could or should be implemented.  For example, the `Compare` button could be replaced with a proper item selection as [recommended](https://developer.android.com/develop/ui/views/layout/recyclerview-custom#select) by Google.

The `Snackbar` that is shown when making API requesrts really should have an action assigned (`Snackbar.setAction()`) to allow the user to cancel an in-flight request.  Ideally, the `FixerIoRetrofitApi` would have functionality to store a list of `Call` objects against the requested URL and expose a function to find the request and do `call.cancel()`.

Most of the custom classes really should implement an interface (part of the [SOLID](https://www.freecodecamp.org/news/solid-principles-explained-in-plain-english/) design principle which I doubt I've followed successfully) instead of using concrete classes.  This would allow future revisions to the project to be made more easily.  For example, swapping out the `FxRepo` for one that uses a completely different endpoint or other means of obtaining the models (i.e. local, hard-coded database file).

Google [recommends](https://developer.android.com/guide/navigation/navigation-custom-back) the use of a dispatcher when trying to handle the user's navigation, specifically for back navigation events.  There is also the [predictive back gesture](https://developer.android.com/guide/navigation/predictive-back-gesture) to take into consideration, but at this time I haven't implemented it and relied on the simpler `activity.onBackPressed()`.

In an ideal world, developers write their tests *before* coding.  In my case, I wrote the test afterwards, which would have saved me a lot of grief when it came to getting the models correct when getting the response from the API.  I've added a pair of simple tests to validate the JSON input for the model, but there are still many more that could be added.  Unit tests are not a panacea, a cure-all, but they do help prevent egregious errors (such as when someone changes the model).
