**Airlines Info**

This is a simple app which allows you to show a list of airlines from 
https://www.kayak.com/h/mobileapis/directory/airlines and view each airline details and you can add an airline to 
you favorites or remove it.

**Architecture**
The app is built using the MVP pattern, this pattern provides a clear separation of concerns 
which helps us to build a scalable, reusable, maintainable, and testable solution.
 
**RxJava**
I used RxJava to help me build any data processing and loading as streams which can be run off 
the ui thread and manipulated easily providing a great level of abstraction.
https://github.com/ReactiveX/RxJava

**Retrofit**
For networking i preferred to use Retrofit for its simplicity and it can be used with RxJava by 
returning the service calls results as Observables.
https://github.com/square/retrofit

**Butterknife**
I used ButterKnife to simplify how i define views by simply injecting them.
http://jakewharton.github.io/butterknife/

**Realm**
For persistence i prefer to use Realm.
https://github.com/realm/realm-java



