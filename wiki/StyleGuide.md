Code submitted to the project should follow the following guidelines.

# Code Style Guidelines #

## Comments ##
  * All functions must be prefaced with comments.
  * All classes must be prefaced with javadocs.
  * No @author tags in javadocs

## Whitespace ##
  * Indention: 2 spaces (no tabs), 4 spaces for line continuations
  * Line Length: 100 characters
  * Braces: Same line starting, new line ending, space before opening brace
  * Operators: Space before and after
  * Parenthesis: No spacing inside

## Autoformatters: ##
Download the below xml file and save it to your local disk. Import this formatter in eclipse with Window --> Preferences --> Java --> Code Style --> Formatter and choose "Import..." and find this file on disk. Now, when you select code in eclipse, Ctrl + Shift + F will auto format it. Avoid auto-formatting entire classes if you're touching small parts, since it makes the code review unnecessarily large.
  * Eclipse: http://omnidroid.googlecode.com/svn/tools/Eclipse_Formatter-ITP_Conventions.xml

## StyleCheck ##
Also, if you use eclipse, there's an easy way to make sure you stick with the style guide: use a style-check plug-in, here's one:http://eclipse-cs.sourceforge.net/downloads.html

## Testing ##
Tests go in the omnidroid-test project that is a sibling project to the main omnidroid project. A test class should go in the same package as the class under test. For testing FooClass, the test class should be name FooClassTest.

When possible, extend jUnit's TestCase instead of an Android class. jUnit tests run an order of magnitude faster than Android tests. It is appropriate to write two separate test classes for the same class under test, one Android test and one jUnit test, if there are significant portions of the class that can be tested without Android infrastructure.

When naming test methods, the name must begin with "test" and, for testing method doFoo(), should be approximately "testDoFoo". If you have several tests for different code paths in doFoo(), differentiate your test methods with names that describe their differences: testDoFoo\_rejectNull, testDoFoo\_invalidArgument, testDoFoo\_emptyString, etc.

Testing resources and general guidelines:
  * [jUnit - A Cook's Tour](http://junit.sourceforge.net/doc/cookstour/cookstour.htm) - a great getting started guide for jUnit
  * [The Way of Testivus: Unit Testing Wisdom From An Ancient Software Start-up](http://www.artima.com/weblogs/viewpost.jsp?thread=203994) - a fun (and valuable) take on testing best practices


## Other Conventions ##
  * For anything not specified above, follow Sun's [Java code convention document](http://java.sun.com/docs/codeconv/html/CodeConvTOC.doc.html).

## Best Practices for Android Development ##
  * [Designing for Performance](http://developer.android.com/guide/practices/design/performance.html)
  * [User Interface Guidelines](http://developer.android.com/guide/practices/ui_guidelines/index.html)
  * [Designing for Responsiveness](http://developer.android.com/guide/practices/design/responsiveness.html)
  * [Designing for Seamlessness](http://developer.android.com/guide/practices/design/seamlessness.html)